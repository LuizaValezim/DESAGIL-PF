//
// NÃO MODIFIQUE ESTE ARQUIVO!
//
// Este arquivo não precisa ser modificado. Se pensa
// em fazer isso, sua resposta está na direção errada.
//

package br.edu.insper.desagil.backend.core.firestore;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.ExecutionException;

import com.google.cloud.firestore.CollectionReference;
import com.google.cloud.firestore.DocumentReference;
import com.google.cloud.firestore.DocumentSnapshot;
import com.google.cloud.firestore.FieldPath;
import com.google.cloud.firestore.Firestore;
import com.google.cloud.firestore.Query;
import com.google.cloud.firestore.QuerySnapshot;
import com.google.cloud.firestore.WriteBatch;
import com.google.cloud.firestore.WriteResult;
import com.google.firebase.cloud.FirestoreClient;

import br.edu.insper.desagil.backend.core.DAO;
import br.edu.insper.desagil.backend.core.exception.APIException;
import br.edu.insper.desagil.backend.core.exception.BadRequestException;
import br.edu.insper.desagil.backend.core.exception.DBException;
import br.edu.insper.desagil.backend.core.exception.NotFoundException;
import br.edu.insper.desagil.backend.core.firestore.exception.FirestoreExecutionException;
import br.edu.insper.desagil.backend.core.firestore.exception.FirestoreInterruptedException;

public abstract class FirestoreDAO<T extends FirestoreEntity> implements DAO<String, T> {
	private final Class<T> klass;
	private final Firestore firestore;
	private final CollectionReference collection;

	@SuppressWarnings("unchecked")
	public FirestoreDAO(String path) throws APIException {
		ParameterizedType type = (ParameterizedType) getClass().getGenericSuperclass();
		Type[] types = type.getActualTypeArguments();
		this.klass = (Class<T>) types[0];

		this.firestore = FirestoreClient.getFirestore();

		try {
			this.collection = this.firestore.collection(path);
		} catch (IllegalArgumentException exception) {
			throw new BadRequestException("Firestore connection failed:" + exception.getMessage());
		}
	}

	private final List<T> execute(Query query) throws DBException, APIException {
		List<T> values = new ArrayList<>();
		QuerySnapshot documents;
		try {
			documents = query.get().get();
		} catch (ExecutionException exception) {
			throw new FirestoreExecutionException(exception);
		} catch (InterruptedException exception) {
			throw new FirestoreInterruptedException(exception);
		}
		for (DocumentSnapshot document : documents) {
			T value;
			try {
				value = document.toObject(klass);
			} catch (RuntimeException exception) {
				throw new DBException("Firestore deserialization failed", exception);
			}
			values.add(value);
		}
		return values;
	}

	public boolean exists(String key) throws DBException, APIException {
		if (key == null) {
			throw new BadRequestException("Key cannot be null");
		}
		if (key.isEmpty()) {
			throw new BadRequestException("Key cannot be empty");
		}
		DocumentSnapshot document;
		try {
			document = collection.document(key).get().get();
		} catch (ExecutionException exception) {
			throw new FirestoreExecutionException(exception);
		} catch (InterruptedException exception) {
			throw new FirestoreInterruptedException(exception);
		}
		return document.exists();
	}

	public boolean exists(List<String> keys) throws DBException, APIException {
		if (keys.isEmpty()) {
			throw new BadRequestException("List of keys cannot be empty");
		}
		for (String key: keys) {
			if (key == null) {
				throw new BadRequestException("Key cannot be null");
			}
			if (key.isEmpty()) {
				throw new BadRequestException("Key cannot be empty");
			}
		}
		Query query = collection.whereIn(FieldPath.documentId(), keys);
		QuerySnapshot documents;
		try {
			documents = query.get().get();
		} catch (ExecutionException exception) {
			throw new FirestoreExecutionException(exception);
		} catch (InterruptedException exception) {
			throw new FirestoreInterruptedException(exception);
		}
		return keys.size() == documents.size();
	}

	@Override
	public Date create(T value) throws DBException, APIException {
		if (value == null) {
			throw new BadRequestException("Value cannot be null");
		}
		WriteResult result;
		try {
			DocumentReference document;
			if (value instanceof FirestoreAutokeyEntity) {
				document = collection.add(value).get();
				((FirestoreAutokeyEntity) value).setKey(document.getId());
			} else {
				String key = value.key();
				if (key == null) {
					throw new BadRequestException("Key cannot be null");
				}
				if (key.isEmpty()) {
					throw new BadRequestException("Key cannot be empty");
				}
				document = collection.document(key);
				if (document.get().get().exists()) {
					throw new BadRequestException("Key " + key + " already exists");
				}
			}
			result = document.set(value).get();
		} catch (ExecutionException exception) {
			throw new FirestoreExecutionException(exception);
		} catch (InterruptedException exception) {
			throw new FirestoreInterruptedException(exception);
		}
		return result.getUpdateTime().toDate();
	}

	@Override
	public T retrieve(String key) throws DBException, APIException {
		if (key == null) {
			throw new BadRequestException("Key cannot be null");
		}
		if (key.isEmpty()) {
			throw new BadRequestException("Key cannot be empty");
		}
		DocumentSnapshot document;
		try {
			document = collection.document(key).get().get();
		} catch (ExecutionException exception) {
			throw new FirestoreExecutionException(exception);
		} catch (InterruptedException exception) {
			throw new FirestoreInterruptedException(exception);
		}
		if (!document.exists()) {
			throw new NotFoundException("Key " + key + " not found");
		}
		T value;
		try {
			value = document.toObject(klass);
		} catch (RuntimeException exception) {
			throw new DBException("Firestore deserialization failed", exception);
		}
		return value;
	}

	@Override
	public List<T> retrieve(List<String> keys) throws DBException, APIException {
		if (keys.isEmpty()) {
			throw new BadRequestException("List of keys cannot be empty");
		}
		for (String key: keys) {
			if (key == null) {
				throw new BadRequestException("Key cannot be null");
			}
			if (key.isEmpty()) {
				throw new BadRequestException("Key cannot be empty");
			}
		}
		return execute(collection.whereIn(FieldPath.documentId(), keys));
	}

	@Override
	public List<T> retrieveLt(String key, Object value) throws DBException, APIException {
		if (key == null) {
			throw new BadRequestException("Key cannot be null");
		}
		if (key.isEmpty()) {
			throw new BadRequestException("Key cannot be empty");
		}
		return execute(collection.whereLessThan(key, value));
	}

	@Override
	public List<T> retrieveLeq(String key, Object value) throws DBException, APIException {
		if (key == null) {
			throw new BadRequestException("Key cannot be null");
		}
		if (key.isEmpty()) {
			throw new BadRequestException("Key cannot be empty");
		}
		return execute(collection.whereLessThanOrEqualTo(key, value));
	}

	@Override
	public List<T> retrieveEq(String key, Object value) throws DBException, APIException {
		if (key == null) {
			throw new BadRequestException("Key cannot be null");
		}
		if (key.isEmpty()) {
			throw new BadRequestException("Key cannot be empty");
		}
		return execute(collection.whereEqualTo(key, value));
	}

	@Override
	public List<T> retrieveGt(String key, Object value) throws DBException, APIException {
		if (key == null) {
			throw new BadRequestException("Key cannot be null");
		}
		if (key.isEmpty()) {
			throw new BadRequestException("Key cannot be empty");
		}
		return execute(collection.whereGreaterThan(key, value));
	}

	@Override
	public List<T> retrieveGeq(String key, Object value) throws DBException, APIException {
		if (key == null) {
			throw new BadRequestException("Key cannot be null");
		}
		if (key.isEmpty()) {
			throw new BadRequestException("Key cannot be empty");
		}
		return execute(collection.whereGreaterThanOrEqualTo(key, value));
	}

	@Override
	public List<T> retrieveIn(String key, List<Object> values) throws DBException, APIException {
		if (key == null) {
			throw new BadRequestException("Key cannot be null");
		}
		if (key.isEmpty()) {
			throw new BadRequestException("Key cannot be empty");
		}
		if (values.isEmpty()) {
			throw new BadRequestException("List of values cannot be empty");
		}
		return execute(collection.whereIn(key, values));
	}

	@Override
	public List<T> retrieveAll() throws DBException, APIException {
		return execute(collection);
	}

	@Override
	public Date update(T value) throws DBException, APIException {
		if (value == null) {
			throw new BadRequestException("Value cannot be null");
		}
		String key = value.key();
		if (key == null) {
			throw new BadRequestException("Key cannot be null");
		}
		if (key.isEmpty()) {
			throw new BadRequestException("Key cannot be empty");
		}
		DocumentReference document = collection.document(key);
		WriteResult result;
		try {
			if (!document.get().get().exists()) {
				throw new NotFoundException("Key " + key + " not found");
			}
			result = document.set(value).get();
		} catch (ExecutionException exception) {
			throw new FirestoreExecutionException(exception);
		} catch (InterruptedException exception) {
			throw new FirestoreInterruptedException(exception);
		}
		return result.getUpdateTime().toDate();
	}

	@Override
	public Date delete(String key) throws DBException, APIException {
		if (key == null) {
			throw new BadRequestException("Key cannot be null");
		}
		if (key.isEmpty()) {
			throw new BadRequestException("Key cannot be empty");
		}
		DocumentReference document = collection.document(key);
		WriteResult result;
		try {
			if (!document.get().get().exists()) {
				throw new NotFoundException("Key " + key + " not found");
			}
			result = document.delete().get();
		} catch (ExecutionException exception) {
			throw new FirestoreExecutionException(exception);
		} catch (InterruptedException exception) {
			throw new FirestoreInterruptedException(exception);
		}
		return result.getUpdateTime().toDate();
	}

	@Override
	public List<Date> delete(List<String> keys) throws DBException, APIException {
		if (keys.isEmpty()) {
			throw new BadRequestException("List of keys cannot be empty");
		}
		List<WriteResult> results;
		try {
			WriteBatch batch = firestore.batch();
			for (String key: keys) {
				if (key == null) {
					throw new BadRequestException("Key cannot be null");
				}
				if (key.isEmpty()) {
					throw new BadRequestException("Key cannot be empty");
				}
				DocumentReference document = collection.document(key);
					if (!document.get().get().exists()) {
						throw new NotFoundException("Key " + key + " not found");
					}
				batch.delete(document);
			}
			results = batch.commit().get();
		} catch (ExecutionException exception) {
			throw new FirestoreExecutionException(exception);
		} catch (InterruptedException exception) {
			throw new FirestoreInterruptedException(exception);
		}
		List<Date> dates = new ArrayList<>();
		for (WriteResult result: results) {
			dates.add(result.getUpdateTime().toDate());
		}
		return dates;
	}

	@Override
	public List<Date> deleteAll() throws DBException, APIException {
		List<WriteResult> results;
		try {
			WriteBatch batch = firestore.batch();
			for (DocumentReference document : collection.listDocuments()) {
				batch.delete(document);
			}
			results = batch.commit().get();
		} catch (ExecutionException exception) {
			throw new FirestoreExecutionException(exception);
		} catch (InterruptedException exception) {
			throw new FirestoreInterruptedException(exception);
		}
		List<Date> dates = new ArrayList<>();
		for (WriteResult result: results) {
			dates.add(result.getUpdateTime().toDate());
		}
		return dates;
	}
}

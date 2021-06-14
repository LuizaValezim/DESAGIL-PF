//
// NÃO MODIFIQUE ESTE ARQUIVO!
//
// Este arquivo não precisa ser modificado. Se pensa
// em fazer isso, sua resposta está na direção errada.
//

package br.edu.insper.desagil.backend.core.firestore;

import java.util.HashMap;
import java.util.concurrent.ExecutionException;

import com.google.cloud.firestore.CollectionReference;
import com.google.cloud.firestore.DocumentReference;
import com.google.cloud.firestore.DocumentSnapshot;
import com.google.cloud.firestore.FieldValue;
import com.google.cloud.firestore.Firestore;
import com.google.firebase.cloud.FirestoreClient;

import br.edu.insper.desagil.backend.core.DataMap;
import br.edu.insper.desagil.backend.core.exception.DBException;
import br.edu.insper.desagil.backend.core.firestore.exception.FirestoreExecutionException;
import br.edu.insper.desagil.backend.core.firestore.exception.FirestoreInterruptedException;

public class FirestoreDataMap implements DataMap<String> {
	private DocumentReference document;

	public FirestoreDataMap(String name) throws DBException {
		Firestore firestore = FirestoreClient.getFirestore();

		CollectionReference collection;
		try {
			collection = firestore.collection("datamaps");
		} catch (IllegalArgumentException exception) {
			throw new DBException("Firestore connection failed", exception);
		}

		this.document = collection.document(name);

		DocumentSnapshot snapshot;
		try {
			snapshot = this.document.get().get();
		} catch (ExecutionException exception) {
			throw new FirestoreExecutionException(exception);
		} catch (InterruptedException exception) {
			throw new FirestoreInterruptedException(exception);
		}
		if (!snapshot.exists()) {
			this.document.create(new HashMap<>());
		}
	}

	@Override
	public boolean has(String key) throws DBException {
		DocumentSnapshot snapshot;
		try {
			snapshot = document.get().get();
		} catch (ExecutionException exception) {
			throw new FirestoreExecutionException(exception);
		} catch (InterruptedException exception) {
			throw new FirestoreInterruptedException(exception);
		}
		return snapshot.contains(key);
	}

	@Override
	public Object get(String key) throws DBException {
		DocumentSnapshot snapshot;
		try {
			snapshot = document.get().get();
		} catch (ExecutionException exception) {
			throw new FirestoreExecutionException(exception);
		} catch (InterruptedException exception) {
			throw new FirestoreInterruptedException(exception);
		}
		return snapshot.get(key);
	}

	@Override
	public void set(String key, Object value) throws DBException {
		document.update(key, value);
	}

	@Override
	public void del(String key) throws DBException {
		document.update(key, FieldValue.delete());
	}
}

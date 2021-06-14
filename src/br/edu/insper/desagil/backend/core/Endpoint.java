//
// NÃO MODIFIQUE ESTE ARQUIVO!
//
// Este arquivo não precisa ser modificado. Se pensa
// em fazer isso, sua resposta está na direção errada.
//

package br.edu.insper.desagil.backend.core;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonSyntaxException;

import br.edu.insper.desagil.backend.core.exception.APIException;
import br.edu.insper.desagil.backend.core.exception.BadRequestException;
import br.edu.insper.desagil.backend.core.exception.MethodNotImplementedException;

public abstract class Endpoint<T> extends Context {
	private final Class<T> klass;
	private final Gson gson;

	@SuppressWarnings("unchecked")
	protected Endpoint(String uri) {
		super(uri);

		ParameterizedType type = (ParameterizedType) getClass().getGenericSuperclass();
		Type[] types = type.getActualTypeArguments();
		this.klass = (Class<T>) types[0];

		this.gson = new GsonBuilder().setPrettyPrinting().create();
	}

	protected String extract(Map<String, String> args, String name) throws APIException {
		String value = args.get(name);
		if (value == null) {
			throw new BadRequestException("Arg " + name + " not found");
		}
		return value;
	}

	protected List<String> split(String arg, String regex) {
		return Arrays.asList(arg.split(regex));
	}

	@Override
	public final String doGet(Map<String, String> args, boolean isList) throws APIException {
		String value;
		if (isList) {
			value = gson.toJson(getList(args));
		} else {
			value = gson.toJson(get(args));
		}
		return value;
	}

	@Override
	public final String doPost(Map<String, String> args, String body) throws APIException {
		T value;
		try {
			value = gson.fromJson(body, klass);
		} catch (JsonSyntaxException exception) {
			throw new BadRequestException("Invalid POST body: " + exception.getMessage());
		}
		if (value == null) {
			throw new BadRequestException("POST must have a body");
		}
		return gson.toJson(post(args, value));
	}

	@Override
	public final String doPut(Map<String, String> args, String body) throws APIException {
		T value;
		try {
			value = gson.fromJson(body, klass);
		} catch (JsonSyntaxException exception) {
			throw new BadRequestException("Invalid PUT body: " + exception.getMessage());
		}
		if (value == null) {
			throw new BadRequestException("PUT must have a body");
		}
		return gson.toJson(put(args, value));
	}

	@Override
	public final String doDelete(Map<String, String> args, boolean isList) throws APIException {
		String value;
		if (isList) {
			value = gson.toJson(deleteList(args));
		} else {
			value = gson.toJson(delete(args));
		}
		return value;
	}

	protected T get(Map<String, String> args) throws APIException {
		throw new MethodNotImplementedException("get");
	}

	protected List<T> getList(Map<String, String> args) throws APIException {
		throw new MethodNotImplementedException("get");
	}

	protected Map<String, String> post(Map<String, String> args, T body) throws APIException {
		throw new MethodNotImplementedException("post");
	}

	protected Map<String, String> put(Map<String, String> args, T body) throws APIException {
		throw new MethodNotImplementedException("put");
	}

	protected Map<String, String> delete(Map<String, String> args) throws APIException {
		throw new MethodNotImplementedException("delete");
	}

	protected Map<String, String> deleteList(Map<String, String> args) throws APIException {
		throw new MethodNotImplementedException("delete");
	}
}

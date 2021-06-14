//
// NÃO MODIFIQUE ESTE ARQUIVO!
//
// Este arquivo não precisa ser modificado. Se pensa
// em fazer isso, sua resposta está na direção errada.
//

package br.edu.insper.desagil.backend.core;

import java.util.Map;

import br.edu.insper.desagil.backend.core.exception.APIException;

public abstract class Context {
	private final String uri;

	protected Context(String uri) {
		this.uri = uri;
	}

	public final String getURI() {
		return uri;
	}

	public abstract String doGet(Map<String, String> args, boolean isList) throws APIException;
	public abstract String doPost(Map<String, String> args, String body) throws APIException;
	public abstract String doPut(Map<String, String> args, String body) throws APIException;
	public abstract String doDelete(Map<String, String> args, boolean isList) throws APIException;
}

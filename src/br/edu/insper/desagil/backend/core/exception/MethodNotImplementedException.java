//
// NÃO MODIFIQUE ESTE ARQUIVO!
//
// Este arquivo não precisa ser modificado. Se pensa
// em fazer isso, sua resposta está na direção errada.
//

package br.edu.insper.desagil.backend.core.exception;

import jakarta.servlet.http.HttpServletResponse;

public class MethodNotImplementedException extends APIException {
	private static final long serialVersionUID = 1019739793307649602L;

	public MethodNotImplementedException(String method) {
		super(HttpServletResponse.SC_METHOD_NOT_ALLOWED, method.toUpperCase() + " not implemented");
	}
}

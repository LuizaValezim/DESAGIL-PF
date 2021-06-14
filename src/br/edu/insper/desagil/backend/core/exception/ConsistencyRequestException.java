//
// NÃO MODIFIQUE ESTE ARQUIVO!
//
// Este arquivo não precisa ser modificado. Se pensa
// em fazer isso, sua resposta está na direção errada.
//

package br.edu.insper.desagil.backend.core.exception;

public class ConsistencyRequestException extends BadRequestException {
	private static final long serialVersionUID = 4554525928546667749L;

	public ConsistencyRequestException(String message) {
		super("Consistency error: " + message);
	}
}

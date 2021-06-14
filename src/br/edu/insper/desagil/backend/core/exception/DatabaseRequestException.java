//
// NÃO MODIFIQUE ESTE ARQUIVO!
//
// Este arquivo não precisa ser modificado. Se pensa
// em fazer isso, sua resposta está na direção errada.
//

package br.edu.insper.desagil.backend.core.exception;

public class DatabaseRequestException extends BadRequestException {
	private static final long serialVersionUID = -584311131215532274L;

	public DatabaseRequestException(DBException exception) {
		super("Database error: " + exception.getMessage());
	}
}

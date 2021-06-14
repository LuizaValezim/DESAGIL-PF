//
// NÃO MODIFIQUE ESTE ARQUIVO!
//
// Este arquivo não precisa ser modificado. Se pensa
// em fazer isso, sua resposta está na direção errada.
//

package br.edu.insper.desagil.backend.core.firestore.exception;

import br.edu.insper.desagil.backend.core.exception.DBException;

public class FirestoreInterruptedException extends DBException {
	private static final long serialVersionUID = 5081675235612820837L;

	public FirestoreInterruptedException(InterruptedException exception) {
		super("Firestore execution interrupted", exception);
	}
}

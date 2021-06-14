//
// NÃO MODIFIQUE ESTE ARQUIVO!
//
// Este arquivo não precisa ser modificado. Se pensa
// em fazer isso, sua resposta está na direção errada.
//

package br.edu.insper.desagil.backend.core.firestore;

public abstract class FirestoreAutokeyEntity extends FirestoreEntity {
	private String key;

	public final String getKey() {
		return key;
	}

	public final void setKey(String key) {
		this.key = key;
	}

	@Override
	public final String key() {
		return getKey();
	}
}

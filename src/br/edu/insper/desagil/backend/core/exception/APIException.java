package br.edu.insper.desagil.backend.core.exception;

public class APIException extends Exception {
	private static final long serialVersionUID = 4825474457582715212L;

	private final int sc;

	public APIException(int sc, String message) {
		super(message);
		this.sc = sc;
	}

	public final int getStatus() {
		return sc;
	}
}

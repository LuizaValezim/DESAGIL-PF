package br.edu.insper.desagil.backend.core.exception;

import jakarta.servlet.http.HttpServletResponse;

public class BadRequestException extends APIException {
	private static final long serialVersionUID = -6620526673080990903L;

	public BadRequestException(String message) {
		super(HttpServletResponse.SC_BAD_REQUEST, message);
	}
}

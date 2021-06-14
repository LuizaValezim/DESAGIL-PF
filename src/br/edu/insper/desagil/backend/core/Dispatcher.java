//
// NÃO MODIFIQUE ESTE ARQUIVO!
//
// Este arquivo não precisa ser modificado. Se pensa
// em fazer isso, sua resposta está na direção errada.
//

package br.edu.insper.desagil.backend.core;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;

import org.eclipse.jetty.server.Request;
import org.eclipse.jetty.server.handler.AbstractHandler;
import org.reflections.Reflections;

import br.edu.insper.desagil.backend.core.exception.APIException;
import br.edu.insper.desagil.backend.core.exception.BadRequestException;
import br.edu.insper.desagil.backend.core.exception.NotFoundException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class Dispatcher extends AbstractHandler {
	private final Map<String, Context> contexts;

	public Dispatcher() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException, InstantiationException {
		super();
		this.contexts = new HashMap<>();
		Reflections reflections = new Reflections("br.edu.insper.desagil.backend");
		for (Class<? extends Context> klass : reflections.getSubTypesOf(Endpoint.class)) {
			Constructor<?> constructor = klass.getConstructor();
			Context context = (Context) constructor.newInstance();
			this.contexts.put(context.getURI(), context);
		}
	}

	@Override
	public void handle(String target, Request baseRequest, HttpServletRequest request, HttpServletResponse response) throws IOException {
		String responseBody;
		try {
			String uri = request.getRequestURI();
			int length = uri.length();
			boolean isList = length > 5 && uri.endsWith("/list");
			if (isList) {
				uri = uri.substring(0, length - 5);
			}
			Context context = contexts.get(uri);
			if (context == null) {
				throw new NotFoundException("Endpoint " + uri + " not found");
			}

			Map<String, String> args = new HashMap<>();
			Map<String, String[]> map = request.getParameterMap();
			for (String name : map.keySet()) {
				if (name.isEmpty()) {
					throw new BadRequestException("Empty args not allowed");
				}
				String[] words = map.get(name);
				if (words.length < 1) {
					throw new BadRequestException("Arg " + name + " has no value");
				}
				if (words.length > 1) {
					throw new BadRequestException("Arg " + name + " has multiple values");
				}
				args.put(name, words[0]);
			}

			String line;
			BufferedReader reader = request.getReader();
			StringBuilder builder = new StringBuilder();
			while ((line = reader.readLine()) != null) {
				builder.append(line);
				builder.append('\n');
			}
			String requestBody = builder.toString();

			String method = request.getMethod();
			switch (method) {
			case "GET":
				responseBody = context.doGet(args, isList);
				break;
			case "POST":
				responseBody = context.doPost(args, requestBody);
				break;
			case "PUT":
				responseBody = context.doPut(args, requestBody);
				break;
			case "DELETE":
				responseBody = context.doDelete(args, isList);
				break;
			case "OPTIONS":
				responseBody = "";
				break;
			default:
				throw new APIException(HttpServletResponse.SC_METHOD_NOT_ALLOWED, method.toUpperCase() + " not supported");
			}
			response.setStatus(HttpServletResponse.SC_OK);
			response.setContentType("application/json");
		} catch (APIException exception) {
			responseBody = exception.getMessage();
			response.setStatus(exception.getStatus());
			response.setContentType("text/plain");
		} catch (Exception exception) {
			exception.printStackTrace();
			responseBody = "Internal server error";
			response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
			response.setContentType("text/plain");
		}
		response.addHeader("Access-Control-Allow-Origin", "*");
		response.addHeader("Access-Control-Allow-Methods", "*");
		response.addHeader("Access-Control-Allow-Headers", "*");
		PrintWriter writer = response.getWriter();
		writer.println(responseBody);
		baseRequest.setHandled(true);
	}
}

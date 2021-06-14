//
// NÃO MODIFIQUE ESTE ARQUIVO!
//
// Este arquivo não precisa ser modificado. Se pensa
// em fazer isso, sua resposta está na direção errada.
//

package br.edu.insper.desagil.backend;

import java.io.FileInputStream;
import java.io.IOException;
import java.net.InetAddress;

import org.eclipse.jetty.server.Server;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;

import br.edu.insper.desagil.backend.core.Dispatcher;

public class Backend {
	private static final int PORT = 8080;

	public static void init(String fileName) throws IOException {
		FileInputStream stream = new FileInputStream(fileName);
		FirebaseOptions options = FirebaseOptions.builder()
			.setCredentials(GoogleCredentials.fromStream(stream))
			.build();
		FirebaseApp.initializeApp(options);
	}

	public static void main(String[] args) throws Exception {
		Dispatcher dispatcher = new Dispatcher();

		Server server = new Server(PORT);
		server.setHandler(dispatcher);
		server.start();

		String address = InetAddress.getLocalHost().getHostAddress();
		System.out.println("Waiting on http://" + address + ':' + PORT);
	}
}

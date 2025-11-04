/**
 * ALL RIGHT RESERVED By kavinda_d
 *
 * @AUTHOR : kavinda_d
 * @PROJECT : fms backend
 */
package com.example.proj2;
import com.example.proj2.service.ClientService;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.socket.messaging.WebSocketStompClient;
@SpringBootApplication
public class Proj2Application implements CommandLineRunner {
	private final ClientService clientService;
	public Proj2Application(ClientService clientService) {
		this.clientService = clientService;
	}
	public static void main(String[] args) {
		SpringApplication.run(Proj2Application.class, args);
	}
	@Override
	public void run(String... args) {
		clientService.connectToServer();
	}
}

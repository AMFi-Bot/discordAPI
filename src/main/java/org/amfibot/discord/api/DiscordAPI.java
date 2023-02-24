package org.amfibot.discord.api;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@SpringBootApplication
@RestController
public class DiscordAPI {

	public static void main(String[] args) {
		SpringApplication.run(DiscordAPI.class, args);
	}

	@GetMapping("/")
	public String root() {
		return "Hello, discord api!";
	}

}

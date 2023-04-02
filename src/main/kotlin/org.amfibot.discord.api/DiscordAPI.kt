package org.amfibot.discord.api

import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController

@SpringBootApplication
@RestController
class DiscordAPI {
    @GetMapping("/")
    fun root(): String {
        return "Hello, discord api!"
    }

}

fun main(args: Array<String>) {
    runApplication<DiscordAPI>(*args);
}
package org.amfibot.discord.api

import org.apache.catalina.webresources.TomcatURLStreamHandlerFactory
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.security.core.Authentication
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController

@SpringBootApplication
@RestController
class DiscordAPI {
    @GetMapping("/")
    fun root(): String {
        return "Hello, discord api!"
    }

    @GetMapping("/user")
    fun getUser(authentication: Authentication): Authentication {
        return authentication
    }

}

fun main(args: Array<String>) {
    // Disables TomcatURLStreamHandlerFactory
    // to bypass the factory is already defined exception
    // when auto reloading the application with spring DevTools remote
    TomcatURLStreamHandlerFactory.disable()

    runApplication<DiscordAPI>(*args)
}
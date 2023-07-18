package org.amfibot.discord.api.user

import discord4j.discordjson.json.UserData
import org.amfibot.discord.api.helpers.jackson.discord.discordJSONMapper
import org.springframework.security.authentication.BadCredentialsException
import java.net.URI
import java.net.http.HttpClient
import java.net.http.HttpRequest
import java.net.http.HttpResponse.BodyHandlers

/**
 * Fetches the discord user from the discord server
 */
fun fetchDiscordUser(token: String): UserData {
    val httpClient = HttpClient.newHttpClient()

    val uri = URI("https://discord.com/api/v10/users/@me")
    val request = HttpRequest.newBuilder(uri)
        .header("Authorization", "Bearer $token")
        .build()

    val response = httpClient.send(request, BodyHandlers.ofString(Charsets.UTF_8))

    val status = response.statusCode()
    val body = response.body()

    when (status) {
        200 -> return discordJSONMapper.readValue(body, UserData::class.java)
        401 -> throw DiscordTokenInvalidException()
        else -> {
            throw Exception(
                "The discord server returned an error with status code: $status " +
                        "and body\n$body"
            )
        }
    }
}

/**
 * Throws when the get discord user request returns an Unauthorized status code.
 */
class DiscordTokenInvalidException :
    BadCredentialsException("The discord token is invalid. May be it has been expired or was revoked")
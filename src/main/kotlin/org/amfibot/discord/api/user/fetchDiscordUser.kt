package org.amfibot.discord.api.user

import discord4j.discordjson.json.UserData
import discord4j.discordjson.json.UserGuildData
import org.amfibot.discord.api.helpers.handleRateLimitFromResponseBody
import org.amfibot.discord.api.helpers.jackson.discord.discordJSONMapper
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.security.authentication.BadCredentialsException
import java.net.URI
import java.net.http.HttpClient
import java.net.http.HttpRequest
import java.net.http.HttpResponse.BodyHandlers

val logger: Logger = LoggerFactory.getLogger("DiscordFetcher")

/**
 * Fetches the discord user from the discord server
 */
fun fetchDiscordUser(token: String): UserData {
    logger.trace("fetch discord user")

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
        429 -> {
            handleRateLimitFromResponseBody(body)

            return fetchDiscordUser(token)
        }

        else -> {
            throw Exception(
                "The discord server returned an error with status code: $status " +
                        "and body\n$body"
            )
        }
    }
}

/**
 * Fetches the discord user's guilds.
 */
fun fetchDiscordUsersGuilds(token: String): Collection<UserGuildData> {
    logger.trace("fetch discord user's guilds")

    val httpClient = HttpClient.newHttpClient()

    val uri = URI("https://discord.com/api/v10/users/@me/guilds")
    val request = HttpRequest.newBuilder(uri)
        .header("Authorization", "Bearer $token")
        .build()

    val response = httpClient.send(request, BodyHandlers.ofString(Charsets.UTF_8))

    val status = response.statusCode()
    val body = response.body()

    when (status) {
        200 -> return discordJSONMapper.readerForListOf(UserGuildData::class.java)
            .readValue(body)

        401, 403 -> throw DiscordTokenInvalidException()

        429 -> {
            handleRateLimitFromResponseBody(body)

            return fetchDiscordUsersGuilds(token)
        }

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
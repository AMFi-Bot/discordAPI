package org.amfibot.discord.api.guild

import com.fasterxml.jackson.annotation.*

import com.fasterxml.jackson.databind.annotation.JsonDeserialize

import discord4j.discordjson.json.GuildUpdateData

import org.amfibot.discord.api.helpers.jackson.AsIsJsonDeserializer
import org.amfibot.discord.api.helpers.jackson.discord.discordJSONMapper
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.http.*
import org.springframework.util.LinkedMultiValueMap
import org.springframework.util.MultiValueMap
import org.springframework.web.bind.annotation.*
import org.springframework.web.client.RestTemplate

/**
 * Operates with the guilds registered in bot
 *
 * Base path: /api/discord/guild
 */
@RestController
@RequestMapping("/api/discord/guilds")
class GuildsController(
    @Autowired private val repository: GuildRepository,
    @Value("\${DISCORD_CLIENT_ID}") private val discordClientId: String,
    @Value("\${DISCORD_CLIENT_SECRET}") private val discordClientSecret: String,
) {

    /**
     * Returns only those guilds of the given list where the bot is on it.
     *
     * ! Uses POST because the GET request does not accept body
     */
    @PostMapping("/registered")
    fun isRegistered(@RequestBody guildsIds: Collection<String>): Collection<String> {
        return repository.findAllById(guildsIds).map { guild -> guild.id }
    }

    /** Checks the guild for the bot to be registered in it. */
    @GetMapping("/registered/{guildId}")
    fun isRegistered(@PathVariable guildId: String): Boolean {
        return repository.findById(guildId).isPresent
    }

    /** Creates a discord guild object */
    @GetMapping("/register")
    fun createGuild(
        @RequestParam("code") code: String,
        @RequestParam("guild_id") guildId: String,
        @RequestParam("permissions") permissions: Int,
        @RequestParam("redirect_uri") redirectPathURI: String?,
        //request: HttpRequest
    ): GuildUpdateData {

        val redirectURI: String = redirectPathURI ?: "http://localhost:3000/discord_bot_callback"//request.uri.toString()
        val restTemplate = RestTemplate()

        val headers = HttpHeaders()
        headers.contentType = MediaType.APPLICATION_FORM_URLENCODED

        val map: MultiValueMap<String, String> = LinkedMultiValueMap()
        map.add("client_id", discordClientId)
        map.add("client_secret", discordClientSecret)
        map.add("grant_type", "authorization_code")
        map.add("code", code)
        map.add("redirect_uri", redirectURI)

        val request: HttpEntity<MultiValueMap<String, String>> = HttpEntity(map, headers)

        val response = restTemplate.postForEntity(
            "https://discord.com/api/v10/oauth2/token",
            request,
            GuildRegistrationAccessTokenJson::class.java
        )

        val body = response.body
            ?: throw Exception("The response from a discord server is null.")

        val mapper = discordJSONMapper

        val guild = mapper.readValue(body.guild, GuildUpdateData::class.java)

        return guild
    }

    /**
     * Use this class to parse the discord access token object containing the Guild
     */
    class GuildRegistrationAccessTokenJson(@JsonDeserialize(using = AsIsJsonDeserializer::class) val guild: String)
}
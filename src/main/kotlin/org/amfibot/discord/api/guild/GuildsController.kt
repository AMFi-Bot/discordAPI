package org.amfibot.discord.api.guild

import com.fasterxml.jackson.annotation.*


import discord4j.discordjson.json.GuildUpdateData
import jakarta.servlet.http.HttpServletRequest
import org.amfibot.discord.api.helpers.guild.fetchGuildOAuth2Code
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.http.*
import org.springframework.web.bind.annotation.*

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

    /**
     * Registers a discord guild in the guilds list of the bot
     *
     * The guild should be provided as an authorization code that exchanges to the access token with the guild.
     *
     */
    @GetMapping("/register")
    fun createGuild(
        @RequestParam("code") code: String,
        @RequestParam("guild_id") guildId: String,
        @RequestParam("permissions") permissions: Int,
        @RequestParam("redirect_uri") redirectPathURI: String?,
        request: HttpServletRequest
    ): GuildUpdateData {
        // If the redirectURI is not provided, may be discord redirected directly to this endpoint
        val redirectURI: String = redirectPathURI ?: request.requestURL.toString()

        return fetchGuildOAuth2Code(code, redirectURI, discordClientId, discordClientSecret)
    }
}
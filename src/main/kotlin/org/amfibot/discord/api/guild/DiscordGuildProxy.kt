package org.amfibot.discord.api.guild

import org.amfibot.discord.api.exceptions.http.client.NotFoundException
import org.amfibot.discord.api.helpers.guild.fetchRawGuild
import org.amfibot.discord.api.helpers.guild.fetchRawGuildChannels
import org.springframework.beans.factory.annotation.Value
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

/**
 * Requests a guild from discord servers and returns to the user.
 */
@RestController
@RequestMapping("/api/discord/guilds/{guildId}")
class DiscordGuildProxy(
    private val repository: GuildRepository,
    @Value("\${DISCORD_BOT_TOKEN}") private val discordBotToken: String) {
    @GetMapping("/discord_base")
    fun getBaseGuild(@PathVariable("guildId") guildId: String): ResponseEntity<String> {
        if (!repository.existsById(guildId)) throw NotFoundException()

        val headers = HttpHeaders()
        headers.contentType = MediaType.APPLICATION_JSON

        return ResponseEntity(fetchRawGuild(guildId, discordBotToken), headers, HttpStatus.OK)
    }
    @GetMapping("/channels")
    fun getGuildChannels(@PathVariable("guildId") guildId: String): ResponseEntity<String> {
        if (!repository.existsById(guildId)) throw NotFoundException()

        val headers = HttpHeaders()
        headers.contentType = MediaType.APPLICATION_JSON

        return ResponseEntity(fetchRawGuildChannels(guildId, discordBotToken), headers, HttpStatus.OK)
    }

}
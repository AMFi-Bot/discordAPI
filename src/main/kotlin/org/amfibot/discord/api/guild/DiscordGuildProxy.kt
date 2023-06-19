package org.amfibot.discord.api.guild

import discord4j.discordjson.json.ChannelData
import discord4j.discordjson.json.GuildUpdateData
import org.amfibot.discord.api.exceptions.http.client.NotFoundException
import org.amfibot.discord.api.helpers.guild.fetchGuild
import org.amfibot.discord.api.helpers.guild.fetchGuildChannels
import org.springframework.beans.factory.annotation.Value
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
    fun getBaseGuild(@PathVariable("guildId") guildId: String): GuildUpdateData{
        if (!repository.existsById(guildId)) throw NotFoundException()

        return fetchGuild(guildId, discordBotToken)
    }
    @GetMapping("/channels")
    fun getGuildChannels(@PathVariable("guildId") guildId: String): Collection<ChannelData>{
        if (!repository.existsById(guildId)) throw NotFoundException()

        return fetchGuildChannels(guildId, discordBotToken)
    }

}
package org.amfibot.discord.api.guild

import org.amfibot.discord.api.exceptions.crud.ResourceAlreadyExistsException
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

/**
 * Operates with the guilds registered in bot
 *
 * Base path: /api/discord/guild
 */
@RestController
@RequestMapping("/api/discord/guilds")
class GuildsController(@Autowired private val repository: GuildRepository) {

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
    @PostMapping
    fun createGuild(@RequestBody guild: Guild): ResponseEntity<Any?> {
        val guildId = guild.id

        if (repository.existsById(guildId)) throw ResourceAlreadyExistsException()

        repository.insert(guild)

        return ResponseEntity(HttpStatus.CREATED)
    }
}

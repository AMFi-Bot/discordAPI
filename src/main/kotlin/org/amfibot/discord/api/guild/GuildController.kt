package org.amfibot.discord.api.guild

import org.amfibot.discord.api.exceptions.crud.ResourceAlreadyExistsException
import org.amfibot.discord.api.exceptions.crud.ResourceNotFoundException
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import java.util.*

/**
 * Rest API methods collection for managing an entire discord guild object.
 *
 * Base path: /api/discord/guild
 *
 */
@RestController
@RequestMapping("/api/discord/guilds")
class GuildController(@Autowired private val repository: GuildRepository) {

    /**
     * Returns the discord guild object by guild id
     */
    @GetMapping("/{guildId}")
    fun getGuild(@PathVariable guildId: String): Guild =
        repository.findById(guildId).orElseThrow { ResourceNotFoundException() }

    /**
     * Creates a discord guild
     */
    @PostMapping
    fun createGuild(@RequestBody guild: Guild) : ResponseEntity<Any?>{
        val guildId = guild.id

        if (repository.existsById(guildId))
            throw ResourceAlreadyExistsException()

        repository.insert(guild)

        return ResponseEntity(HttpStatus.CREATED)
    }


}
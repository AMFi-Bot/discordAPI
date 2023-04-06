package org.amfibot.discord.api.guild

import org.amfibot.discord.api.exceptions.crud.ResourceAlreadyExistsException
import org.amfibot.discord.api.exceptions.crud.ResourceNotFoundException
import org.amfibot.discord.api.exceptions.http.client.BadRequestException
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
     * Returns a discord guild object by guild id
     */
    @GetMapping("/{guildId}")
    fun getGuild(@PathVariable guildId: String): Guild =
        repository.findById(guildId).orElseThrow { ResourceNotFoundException() }

    /**
     * Creates a discord guild object
     */
    @PostMapping
    fun createGuild(@RequestBody guild: Guild) : ResponseEntity<Any?>{
        val guildId = guild.id

        if (repository.existsById(guildId))
            throw ResourceAlreadyExistsException()

        repository.insert(guild)

        return ResponseEntity(HttpStatus.CREATED)
    }


    /**
     * Creates a discord guild object
     */
    @PutMapping("/{guildId}")
    fun updateGuild(@PathVariable guildId: String, @RequestBody guild: Guild) : ResponseEntity<Any?>{
        if (!repository.existsById(guildId))
            throw ResourceNotFoundException()

        if (guildId != guild.id) throw BadRequestException()

        repository.save(guild)

        return ResponseEntity(HttpStatus.NO_CONTENT)
    }

    @DeleteMapping("/{guildId}")
    fun deleteGuild(@PathVariable guildId: String): ResponseEntity<Any?> {
        if (!repository.existsById(guildId))
            throw ResourceNotFoundException()

        repository.deleteById(guildId)

        return ResponseEntity(HttpStatus.NO_CONTENT)
    }


}
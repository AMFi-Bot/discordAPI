package org.amfibot.discord.api.guild

import org.amfibot.discord.api.config.RabbitQueues
import org.amfibot.discord.api.exceptions.crud.ResourceNotFoundException
import org.amfibot.discord.api.exceptions.http.client.BadRequestException
import org.springframework.amqp.rabbit.core.RabbitTemplate
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import java.util.*

/**
 * Manages an entire discord guild object.
 *
 * Base path: /api/discord/guilds
 *
 */
@RestController
@RequestMapping("/api/discord/guilds/{guildId}")
class GuildController(
    @Autowired private val repository: GuildRepository,
    @Autowired private val rabbitTemplate: RabbitTemplate
) {

    /**
     * Returns a discord guild object by guild id
     */
    @GetMapping
    fun getGuild(@PathVariable guildId: String): Guild =
        repository.findById(guildId).orElseThrow { ResourceNotFoundException() }


    /**
     * Creates a discord guild object
     */
    @PutMapping
    fun updateGuild(@PathVariable guildId: String, @RequestBody guild: Guild): ResponseEntity<Any?> {
        if (!repository.existsById(guildId))
            throw ResourceNotFoundException()

        if (guildId != guild.id) throw BadRequestException()

        repository.save(guild)

        // Broadcast listeners on guild update event
        rabbitTemplate.convertAndSend(
            RabbitQueues.DISCORD_GUILD_UPDATED.queueName,
            guild
        )

        return ResponseEntity(HttpStatus.NO_CONTENT)
    }

    @DeleteMapping
    fun deleteGuild(@PathVariable guildId: String): ResponseEntity<Any?> {
        if (!repository.existsById(guildId))
            throw ResourceNotFoundException()

        repository.deleteById(guildId)

        return ResponseEntity(HttpStatus.NO_CONTENT)
    }


}
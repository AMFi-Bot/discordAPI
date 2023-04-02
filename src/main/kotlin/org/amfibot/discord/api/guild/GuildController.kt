package org.amfibot.discord.api.guild

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.crossstore.ChangeSetPersister.NotFoundException
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
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
    @Throws(NotFoundException::class)
    fun getGuild(@PathVariable guildId: String): Guild =
        repository.findById(guildId).orElseThrow { NotFoundException() }
}
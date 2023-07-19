package org.amfibot.discord.api.user

import org.springframework.security.access.AccessDeniedException
import org.springframework.security.core.Authentication
import org.springframework.stereotype.Component

/**
 * Protects the guild to be accessed only from administrator user
 */
@Component("guildAuthorized")
class GuildAuthorizedGuard {
    
    fun check(authentication: Authentication, guildId: String): Boolean {
        val user = authentication.principal as User
        val guilds = fetchDiscordUsersGuilds(user.token)

        val guild = guilds.find { it.id().asString() == guildId }
            ?: throw AccessDeniedException("The guild with id $guildId is not present in the user guild list")

        val permissions = guild.permissions().get()

        return permissions.and(0x8L) == 0x8L
    }
}
package org.amfibot.discord.api.user

/**
 * A UserRepository realisation
 */
class NwUserRepository : UserRepository {
    override fun getUserFromToken(token: String): User {
        val discordUser = fetchDiscordUser(token)

        return User(discordUser.id().asString(), discordUser)
    }
}
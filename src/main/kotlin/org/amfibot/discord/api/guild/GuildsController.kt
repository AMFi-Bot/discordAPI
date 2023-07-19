package org.amfibot.discord.api.guild

import com.fasterxml.jackson.annotation.*

import jakarta.servlet.http.HttpServletRequest
import org.amfibot.discord.api.config.RabbitQueues
import org.amfibot.discord.api.exceptions.http.client.BadRequestException
import org.amfibot.discord.api.helpers.guild.fetchGuildOAuth2Code
import org.amfibot.discord.api.user.User
import org.amfibot.discord.api.user.fetchDiscordUsersGuilds
import org.springframework.amqp.rabbit.core.RabbitTemplate
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.http.*
import org.springframework.security.core.Authentication
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
    @Autowired private val rabbitTemplate: RabbitTemplate
) {

    /**
     * Returns only those guilds of the given list where the bot is on it.
     * (And where the user is administrator)
     *
     * ! Uses POST because the GET request does not accept body
     */
    @PostMapping("/registered")
    fun isRegistered(@RequestBody guildsIds: Collection<String>, authentication: Authentication): Collection<String> {
        val user = authentication.principal as User

        val userGuilds = fetchDiscordUsersGuilds(user.token)

        val guilds = guildsIds.filter { guildId ->
            userGuilds.find {
                (it.id().asString() == guildId) && (it.permissions().get().and(0x8L) == 0x8L)
            } != null
        }

        return repository.findAllById(guilds).map { guild -> guild.id }
    }

    /** Checks the guild for the bot to be registered in it. (And to user being administrator of it */
    @GetMapping("/registered/{guildId}")
    fun isRegistered(@PathVariable guildId: String, authentication: Authentication): Boolean =
        isRegistered(listOf(guildId), authentication).contains(guildId)


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
    ): ResponseEntity<Guild?> {
        // If the redirectURI is not provided, may be discord redirected directly to this endpoint
        val redirectURI: String = redirectPathURI ?: request.requestURL.toString()

        val guild = fetchGuildOAuth2Code(code, redirectURI, discordClientId, discordClientSecret)

        // Do some validations
        if (guild.id().asString() != guildId) throw BadRequestException("Guild ID has been falsified")

        val registeredGuild = repository.findById(guildId)

        if (registeredGuild.isPresent)
            return ResponseEntity(registeredGuild.get(), HttpStatus.OK)

        val botGuild = Guild(guildId)

        // Register a guild
        repository.insert(botGuild)

        // Broadcast listeners on guild registered event
        rabbitTemplate.convertAndSend(
            RabbitQueues.DISCORD_GUILD_REGISTERED.queueName,
            botGuild
        )


        return ResponseEntity(HttpStatus.NO_CONTENT)
    }
}
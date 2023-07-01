package org.amfibot.discord.api.config


enum class RabbitQueues(val queueName: String) {
    REGISTERED_DISCORD_GUILD("registered_discord_guild"),
    UPDATED_DISCORD_GUILD("updated_discord_guild"),
}
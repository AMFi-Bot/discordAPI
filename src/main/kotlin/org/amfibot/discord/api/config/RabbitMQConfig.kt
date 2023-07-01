package org.amfibot.discord.api.config

import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter
import org.springframework.amqp.support.converter.MessageConverter
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class RabbitMQConfig {

    @Bean
    fun getMessageConverter(): MessageConverter = Jackson2JsonMessageConverter()
}

enum class RabbitQueues(val queueName: String) {
    DISCORD_GUILD_REGISTERED("discord_guild.registered"),
    DISCORD_GUILD_UPDATED("discord_guild.updated"),
}
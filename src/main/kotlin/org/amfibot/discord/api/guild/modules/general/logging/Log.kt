package org.amfibot.discord.api.guild.modules.general.logging

data class Log(
    val enabled: Boolean = false,
    val baseChannel: String? = null,
    val loggers: Loggers = Loggers(),
)
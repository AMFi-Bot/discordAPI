package org.amfibot.discord.api.guild.modules.general.logging

data class Log(
    val enabled: Boolean = false,
    val defaultChannel: String? = null,
    val loggers: Loggers = Loggers(),
)
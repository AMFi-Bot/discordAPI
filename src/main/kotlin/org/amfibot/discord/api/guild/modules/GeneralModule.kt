package org.amfibot.discord.api.guild.modules


data class GeneralModule(
    var logEnabled: Boolean = false,
    var logTypes: List<String> = listOf(),
    var logChannel: String? = null,
)
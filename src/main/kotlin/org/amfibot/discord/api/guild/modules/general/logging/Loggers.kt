package org.amfibot.discord.api.guild.modules.general.logging

import org.amfibot.discord.api.guild.modules.general.logging.loggers.MessageCreate

data class Loggers(
    val messageCreate: MessageCreate = MessageCreate()
)
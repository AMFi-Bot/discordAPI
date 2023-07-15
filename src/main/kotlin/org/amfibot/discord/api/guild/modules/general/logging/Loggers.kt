package org.amfibot.discord.api.guild.modules.general.logging

import com.fasterxml.jackson.annotation.JsonInclude
import org.amfibot.discord.api.guild.modules.general.logging.loggers.MessageCreate

data class Loggers(
    @JsonInclude(JsonInclude.Include.NON_NULL)
    val messageCreate: MessageCreate? = null
)
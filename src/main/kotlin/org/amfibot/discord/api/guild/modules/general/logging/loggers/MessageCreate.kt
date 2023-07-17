package org.amfibot.discord.api.guild.modules.general.logging.loggers

import com.fasterxml.jackson.annotation.JsonProperty
import org.amfibot.discord.api.guild.modules.general.logging.Logger

/**
 * Handles all sent messages
 *
 */
class MessageCreate(
    override val state: Boolean = false,
    override val loggingChannel: String? = null
) : Logger {
    @JsonProperty("id")
    override fun getLoggerId(): String = "message_create"
}
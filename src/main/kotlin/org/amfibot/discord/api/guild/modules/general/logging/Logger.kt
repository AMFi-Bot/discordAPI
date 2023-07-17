package org.amfibot.discord.api.guild.modules.general.logging

import com.fasterxml.jackson.annotation.JsonProperty

/**
 * Represents the logger instance
 */
interface Logger {


    /**
     * The logging channel id
     */
    val loggingChannel: String?

    /**
     * The state (enabled or disabled) of the logger
     */
    val state: Boolean

    /**
     * The id (name) of the logger
     */
    @JsonProperty("id")
    fun getLoggerId(): String


}
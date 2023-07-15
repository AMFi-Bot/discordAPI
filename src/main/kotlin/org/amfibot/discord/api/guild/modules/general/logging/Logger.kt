package org.amfibot.discord.api.guild.modules.general.logging

import com.fasterxml.jackson.annotation.JsonProperty

/**
 * Represents the logger instance
 */
interface Logger {


    /**
     * The logging channel id
     */
    val loggingChannel: String

    /**
     * The id (name) of the logger
     */
    @JsonProperty("id")
    fun getLoggerId(): String


}
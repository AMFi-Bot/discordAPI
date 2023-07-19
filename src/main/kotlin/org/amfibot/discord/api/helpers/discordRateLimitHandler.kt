package org.amfibot.discord.api.helpers

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import org.slf4j.Logger
import org.slf4j.LoggerFactory


val logger: Logger = LoggerFactory.getLogger("discordRateLimitHandler")

/**
 * Handles discord rate limit exceptions
 *
 * @param timeout milliseconds
 * @param maximumTimeout maximum timeout
 */
fun handleRateLimit(timeout: Long, maximumTimeout: Long = 80 * 1000) {
    logger.trace("Rate limited for $timeout ms")

    if (timeout > 10 * 1000) {

        if (timeout > maximumTimeout) {
            throw Exception("Discord rate limit timeout is too high: $timeout ms")
        } else
            logger.warn("Rate limit timeout is a bit high: $timeout ms")

    }


    Thread.sleep(timeout + 10)
}

/**
 * Reads discord response body with rate limit error and awaits for a timeout
 */
fun handleRateLimitFromResponseBody(body: String) {
    val resp = jacksonObjectMapper().readValue(body, DiscordRateLimitResponse::class.java)

    val timeout = resp.retryAfter

    handleRateLimit((timeout * 1000).toLong())
}
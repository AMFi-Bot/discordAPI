package org.amfibot.discord.api.helpers

import com.fasterxml.jackson.annotation.JsonProperty

data class DiscordRateLimitResponse(
    val message: String,
    @JsonProperty("retry_after")
    val retryAfter: Float,
    val global: Boolean,
    val code: Int? = null
)
package org.amfibot.discord.api.helpers.guild

import com.fasterxml.jackson.core.type.TypeReference
import discord4j.discordjson.json.AccessTokenData
import discord4j.discordjson.json.ChannelData
import discord4j.discordjson.json.GuildUpdateData
import org.amfibot.discord.api.exceptions.http.client.BadRequestException
import org.amfibot.discord.api.helpers.handleRateLimitFromResponseBody
import org.amfibot.discord.api.helpers.jackson.discord.discordJSONMapper
import org.springframework.http.HttpEntity
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpMethod
import org.springframework.http.MediaType
import org.springframework.util.LinkedMultiValueMap
import org.springframework.util.MultiValueMap
import org.springframework.web.client.RestClientResponseException
import org.springframework.web.client.RestTemplate


/**
 * Fetches guild that provided within an oauth2 flow
 */
fun fetchGuildOAuth2Code(
    code: String,
    redirectUri: String,
    discordClientId: String,
    discordClientSecret: String
): GuildUpdateData =
    discordJSONMapper
        .readValue(
            fetchRawGuildOAuth2Code(code, redirectUri, discordClientId, discordClientSecret),
            AccessTokenData::class.java
        )
        .guild()
        .toOptional()
        .orElseThrow { BadRequestException("The guild is not provided within an oauth2 flow.") }


/**
 * Fetches a guild from discord servers using bot token
 */
fun fetchGuild(
    guildId: String,
    botToken: String
): GuildUpdateData =
    discordJSONMapper
        .readValue(
            fetchRawGuild(guildId, botToken),
            GuildUpdateData::class.java
        )


/**
 * Fetches a guild channels from discord servers using bot token
 */
fun fetchGuildChannels(
    guildId: String,
    botToken: String
): List<ChannelData> =
    discordJSONMapper
        .readValue(
            fetchRawGuildChannels(guildId, botToken),
            object : TypeReference<List<ChannelData>>() {}
        )


fun fetchRawGuildOAuth2Code(
    code: String,
    redirectUri: String,
    discordClientId: String,
    discordClientSecret: String
): String {
    val restTemplate = RestTemplate()

    val headers = HttpHeaders()
    headers.contentType = MediaType.APPLICATION_FORM_URLENCODED

    val map: MultiValueMap<String, String> = LinkedMultiValueMap()
    map.add("client_id", discordClientId)
    map.add("client_secret", discordClientSecret)
    map.add("grant_type", "authorization_code")
    map.add("code", code)
    map.add("redirect_uri", redirectUri)

    val request: HttpEntity<MultiValueMap<String, String>> = HttpEntity(map, headers)


    val response = try {
        restTemplate.postForEntity(
            "https://discord.com/api/v10/oauth2/token",
            request,
            String::class.java
        )
    } catch (ex: RestClientResponseException) {
        if (ex.statusCode.value() == 429) {
            handleRateLimitFromResponseBody(ex.responseBodyAsString)
            return fetchRawGuildOAuth2Code(
                code,
                redirectUri,
                discordClientId,
                discordClientSecret
            )
        } else throw ex
    }

    return response.body
        ?: throw Exception("The response from a discord server is null.")
}

fun fetchRawGuild(
    guildId: String,
    botToken: String
): String {
    val restTemplate = RestTemplate()

    val headers = HttpHeaders()
    headers.set("Authorization", "Bot $botToken")

    val request = HttpEntity<Any?>(headers)

    val response = try {
        restTemplate.exchange(
            "https://discord.com/api/v10/guilds/$guildId",
            HttpMethod.GET,
            request,
            String::class.java
        )
    } catch (ex: RestClientResponseException) {
        if (ex.statusCode.value() == 429) {
            handleRateLimitFromResponseBody(ex.responseBodyAsString)
            return fetchRawGuild(guildId, botToken)
        } else throw ex
    }

    return response.body
        ?: throw Exception("The response from a discord server is null.")
}

fun fetchRawGuildChannels(
    guildId: String,
    botToken: String
): String {
    val restTemplate = RestTemplate()

    val headers = HttpHeaders()
    headers.set("Authorization", "Bot $botToken")

    val request = HttpEntity<Any?>(headers)


    val response = try {
        restTemplate.exchange(
            "https://discord.com/api/v10/guilds/$guildId/channels",
            HttpMethod.GET,
            request,
            String::class.java
        )
    } catch (ex: RestClientResponseException) {
        if (ex.statusCode.value() == 429) {
            handleRateLimitFromResponseBody(ex.responseBodyAsString)
            return fetchRawGuildChannels(guildId, botToken)
        } else throw ex
    }

    return response.body
        ?: throw Exception("The response from a discord server is null.")
}
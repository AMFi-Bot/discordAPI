package org.amfibot.discord.api.helpers.guild

import com.fasterxml.jackson.core.type.TypeReference
import discord4j.discordjson.json.AccessTokenData
import discord4j.discordjson.json.ChannelData
import discord4j.discordjson.json.GuildUpdateData
import org.amfibot.discord.api.exceptions.http.client.BadRequestException
import org.amfibot.discord.api.helpers.jackson.discord.discordJSONMapper
import org.springframework.http.HttpEntity
import org.springframework.http.HttpHeaders
import org.springframework.http.MediaType
import org.springframework.util.LinkedMultiValueMap
import org.springframework.util.MultiValueMap
import org.springframework.web.client.RestTemplate

/**
 * Fetches guild that provided within an oauth2 flow
 */
fun fetchGuildOAuth2Code(
    code: String,
    redirectUri: String,
    discordClientId: String,
    discordClientSecret: String): GuildUpdateData {
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

    val response = restTemplate.postForEntity(
        "https://discord.com/api/v10/oauth2/token",
        request,
        String::class.java
    )

    val body = response.body
        ?: throw Exception("The response from a discord server is null.")

    val accessTokenData = discordJSONMapper.readValue(body, AccessTokenData::class.java)

    return accessTokenData.guild().toOptional().orElseThrow { BadRequestException("The guild is not provided within an oauth2 flow.") }
}

/**
 * Fetches a guild from discord servers using bot token
 */
fun fetchGuild(
    guildId: String,
    botToken: String): GuildUpdateData {
    val restTemplate = RestTemplate()

    val headers = HttpHeaders()
    headers.set("Authorization", "Bot $botToken")

    val request = HttpEntity<Any?>(headers)

    val response = restTemplate.postForEntity(
        "https://discord.com/api/v10/guild/$guildId",
        request,
        String::class.java
    )

    val body = response.body
        ?: throw Exception("The response from a discord server is null.")

    return discordJSONMapper.readValue(body, GuildUpdateData::class.java)
}

/**
 * Fetches a guild channels from discord servers using bot token
 */
fun fetchGuildChannels(
    guildId: String,
    botToken: String): List<ChannelData> {
    val restTemplate = RestTemplate()

    val headers = HttpHeaders()
    headers.set("Authorization", "Bot $botToken")

    val request = HttpEntity<Any?>(headers)

    val response = restTemplate.postForEntity(
        "https://discord.com/api/v10/guild/$guildId",
        request,
        String::class.java
    )

    val body = response.body
        ?: throw Exception("The response from a discord server is null.")

    return discordJSONMapper.readValue(body, object : TypeReference<List<ChannelData>>(){})
}
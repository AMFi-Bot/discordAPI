package org.amfibot.discord.api.helpers.jackson.discord

import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.datatype.jdk8.Jdk8Module
import discord4j.discordjson.possible.PossibleFilter
import discord4j.discordjson.possible.PossibleModule

/**
 * Mapper for discord-json objects
 */
val discordJSONMapper: ObjectMapper = ObjectMapper()
    // Support for Possible<> object form discord-json package
    .registerModule(PossibleModule())
    .setDefaultPropertyInclusion(
        JsonInclude.Value.construct(
            JsonInclude.Include.CUSTOM,
            JsonInclude.Include.ALWAYS, PossibleFilter::class.java, null
        )
    )
    // Support for Optional<>
    .registerModule(Jdk8Module())
    // Do not throw an error on unknown properties that doesn't listen in the Guild object
    .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
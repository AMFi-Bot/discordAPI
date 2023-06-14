package org.amfibot.discord.api.helpers.jackson

import com.fasterxml.jackson.core.JsonParser
import com.fasterxml.jackson.core.TreeNode
import com.fasterxml.jackson.databind.DeserializationContext
import com.fasterxml.jackson.databind.JsonDeserializer

/**
 * Parses a JSON object as a String one
 */
class AsIsJsonDeserializer : JsonDeserializer<String>() {
    override fun deserialize(jp: JsonParser, ctxt: DeserializationContext): String {
        return jp.codec.readTree<TreeNode>(jp).toString()
    }
}
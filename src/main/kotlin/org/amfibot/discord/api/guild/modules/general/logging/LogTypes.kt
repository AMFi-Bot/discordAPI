package org.amfibot.discord.api.guild.modules.general.logging

import com.fasterxml.jackson.annotation.JsonValue

enum class LogTypes(@JsonValue val id: String) {
    MESSAGE_CREATE("message_create")
}
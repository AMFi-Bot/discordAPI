package org.amfibot.discord.api.user

import discord4j.discordjson.json.UserData

/**
 * Discord-based user
 *
 * @param id User id
 * @param discordUser Discord user
 * @param token Discord user token
 */
open class User(val id: String, val discordUser: UserData, val token: String)
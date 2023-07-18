package org.amfibot.discord.api.user

import discord4j.discordjson.json.UserData

open class User(val id: String, val discordUser: UserData)
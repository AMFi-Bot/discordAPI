package org.amfibot.discord.api.guild

import org.amfibot.discord.api.guild.modules.GeneralModule
import org.springframework.data.annotation.Id

/**
 * Discord guild instance
 */
data class Guild(@Id val id: String, val generalModule: GeneralModule = GeneralModule())
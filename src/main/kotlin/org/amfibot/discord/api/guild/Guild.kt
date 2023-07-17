package org.amfibot.discord.api.guild

import org.amfibot.discord.api.guild.modules.general.GeneralModule
import org.springframework.data.annotation.Id

/**
 * Discord guild instance
 */
data class Guild(@Id val id: String, val general: GeneralModule = GeneralModule())
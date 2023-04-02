package org.amfibot.discord.api.exceptions.crud

import org.amfibot.discord.api.exceptions.http.client.NotFoundException

class ResourceNotFoundException(message: String? = null) : NotFoundException(message ?: "Resource is not found.")
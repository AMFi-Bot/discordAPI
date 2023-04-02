package org.amfibot.discord.api.exceptions.crud

import org.amfibot.discord.api.exceptions.http.client.BadRequestException

class ResourceAlreadyExistsException(message: String? = null) : BadRequestException(message ?: "The resource already exists")
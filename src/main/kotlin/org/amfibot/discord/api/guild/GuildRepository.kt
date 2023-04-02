package org.amfibot.discord.api.guild

import org.springframework.data.mongodb.repository.MongoRepository
import org.springframework.stereotype.Repository

@Repository
interface GuildRepository : MongoRepository<Guild, String>
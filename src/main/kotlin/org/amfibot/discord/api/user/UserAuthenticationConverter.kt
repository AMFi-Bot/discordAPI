package org.amfibot.discord.api.user

import org.springframework.core.convert.converter.Converter
import org.springframework.security.authentication.AbstractAuthenticationToken
import org.springframework.security.oauth2.jwt.Jwt
import org.springframework.security.oauth2.server.resource.authentication.JwtGrantedAuthoritiesConverter

/**
 * Converts JWT token to the User Authentication
 */
class UserAuthenticationConverter(private val userRepository: UserRepository) :
    Converter<Jwt, AbstractAuthenticationToken> {
    override fun convert(jwt: Jwt): AbstractAuthenticationToken {
        val discordToken = jwt.getClaimAsString("discordAccessToken")
        val user = userRepository.getUserFromToken(discordToken)

        val grantedAuthoritiesConverter = JwtGrantedAuthoritiesConverter()
        grantedAuthoritiesConverter.setAuthorityPrefix("SCOPE_")

        val authorities = grantedAuthoritiesConverter.convert(jwt)


        val authentication = UserAuthenticationToken(user, authorities)
        authentication.isAuthenticated = true

        return authentication
    }
}
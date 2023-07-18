package org.amfibot.discord.api.user

import org.springframework.security.oauth2.core.OAuth2TokenValidator
import org.springframework.security.oauth2.core.OAuth2TokenValidatorResult
import org.springframework.security.oauth2.jwt.Jwt


/**
 * Validates a JWT token to be discord-typeds
 */
class UserJWTValidator : OAuth2TokenValidator<Jwt> {
    override fun validate(token: Jwt): OAuth2TokenValidatorResult {
        if (token.getClaimAsString("userType") != "discord")
            return OAuth2TokenValidatorResult.failure()

        if (!token.hasClaim("discordAccessToken"))
            return OAuth2TokenValidatorResult.failure()

        return OAuth2TokenValidatorResult.success()
    }
}
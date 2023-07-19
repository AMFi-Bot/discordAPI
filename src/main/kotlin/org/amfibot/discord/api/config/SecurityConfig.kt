package org.amfibot.discord.api.config

import org.amfibot.discord.api.user.NwUserRepository
import org.amfibot.discord.api.user.UserAuthenticationConverter
import org.amfibot.discord.api.user.UserAuthenticationEntryPoint
import org.amfibot.discord.api.user.UserRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.annotation.web.invoke
import org.springframework.security.oauth2.jwt.JwtDecoder
import org.springframework.security.oauth2.jwt.JwtDecoders
import org.springframework.security.oauth2.jwt.JwtValidators
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder
import org.springframework.security.web.SecurityFilterChain
import org.springframework.web.cors.CorsConfiguration
import org.springframework.web.cors.CorsConfigurationSource
import org.springframework.web.cors.UrlBasedCorsConfigurationSource

@Configuration
@EnableWebSecurity
class SecurityConfig {
    @Bean
    fun securityFilterChain(http: HttpSecurity, @Autowired userRepository: UserRepository): SecurityFilterChain {
        http.invoke {
            oauth2ResourceServer {
                jwt {
                    jwtAuthenticationConverter = UserAuthenticationConverter(userRepository)
                }

                authenticationEntryPoint = UserAuthenticationEntryPoint()
            }
            authorizeRequests {
                authorize("/.~~spring-boot!~/**", permitAll)
                authorize("/error", permitAll)
                authorize("/", permitAll)
                authorize(
                    "/api/discord/guilds/register",
                    hasAuthority("SCOPE_discordUser")
                )
                authorize(
                    "/api/discord/guilds/registered/**",
                    hasAuthority("SCOPE_discordUser")
                )
                authorize(
                    "/api/discord/guilds/{guildId}",
                    "@guildAuthorized.check(authentication,#guildId)"
                )
                authorize(anyRequest, authenticated)
            }

            cors {
                configurationSource = corsConfigurationSource()
            }
        }

        return http.build()
    }

    @Bean
    fun corsConfigurationSource(): CorsConfigurationSource {
        val configuration = CorsConfiguration()
        configuration.allowedOriginPatterns = listOf("*")
        configuration.allowedMethods = listOf("HEAD", "GET", "POST", "PUT", "DELETE", "PATCH")
        configuration.allowCredentials = true
        configuration.allowedHeaders =
            listOf(
                "Accept",
                "Origin",
                "Content-Type",
                "Depth",
                "User-Agent",
                "If-Modified-Since,",
                "Cache-Control",
                "Authorization",
                "X-Req",
                "X-File-Size",
                "X-Requested-With",
                "X-File-Name"
            )

        val source = UrlBasedCorsConfigurationSource()
        source.registerCorsConfiguration("/**", configuration)
        return source
    }

    @Bean
    fun userRepository(): UserRepository = NwUserRepository()


    @Bean
    fun jwtDecoder(@Value("\${AS_JWT_ISSUER_URI}") issuerUri: String): JwtDecoder {
        val jwtDecoder: NimbusJwtDecoder = JwtDecoders.fromIssuerLocation(issuerUri)

        //val userValidator = UserJWTValidator()
        val withIssuer = JwtValidators.createDefaultWithIssuer(issuerUri)
        //val withUser = DelegatingOAuth2TokenValidator(withIssuer, userValidator)

        jwtDecoder.setJwtValidator(withIssuer)

        return jwtDecoder
    }

}
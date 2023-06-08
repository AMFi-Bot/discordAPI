package org.amfibot.discord.api.config

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.invoke
import org.springframework.security.web.SecurityFilterChain
import org.springframework.web.cors.CorsConfiguration
import org.springframework.web.cors.CorsConfigurationSource
import org.springframework.web.cors.UrlBasedCorsConfigurationSource

@Configuration
class SecurityConfig {
    @Bean
    fun securityFilterChain(http: HttpSecurity): SecurityFilterChain{
        http.invoke {
            oauth2ResourceServer {
                jwt {  }
            }
            authorizeRequests {
                authorize("/.~~spring-boot!~/**", permitAll)
                authorize("/error", permitAll)
                authorize("/api/discord/guilds", permitAll)//hasAuthority("SCOPE_discordGuilds"))
                authorize(anyRequest, permitAll)
            }

            cors {
                configurationSource = corsConfigurationSource()
            }
        }

        return http.build()
    }

    @Bean
    fun corsConfigurationSource(): CorsConfigurationSource{
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
}
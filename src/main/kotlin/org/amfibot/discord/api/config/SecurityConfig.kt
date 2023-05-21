package org.amfibot.discord.api.config

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.invoke
import org.springframework.security.web.SecurityFilterChain

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
                authorize("/api/discord/guilds", hasAuthority("SCOPE_discordGuilds"))
                authorize(anyRequest, permitAll)
            }
        }
        
        return http.build()
    }
}
package fr.polyflix.gateway.config

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity
import org.springframework.security.config.web.server.ServerHttpSecurity
import org.springframework.security.web.server.SecurityWebFilterChain

@Configuration
@EnableWebFluxSecurity
class SecurityConfiguration {
    @Bean
    fun springSecurityFilterChain(http: ServerHttpSecurity): SecurityWebFilterChain  {
        http.authorizeExchange {
            it
                .pathMatchers("/api/*/users/**")
                .permitAll()
                .pathMatchers("/api/v1/auth/**")
                .permitAll()
                .pathMatchers("/api/*/search/**")
                .permitAll()
                .pathMatchers("/actuator/**")
                .permitAll()
                .anyExchange()
                .authenticated()
        }.oauth2ResourceServer(ServerHttpSecurity.OAuth2ResourceServerSpec::opaqueToken)
            .csrf().disable()
        return http.build()
    }
}
package fr.polyflix.gateway.routing

import fr.polyflix.gateway.config.UriConfiguration
import fr.polyflix.gateway.filters.AuthFilter
import org.springframework.cloud.gateway.route.RouteLocator
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder
import org.springframework.context.annotation.Bean
import org.springframework.stereotype.Component

@Component
class RouterV1(private val uriConfiguration: UriConfiguration) {
    @Bean
    fun v1(builder: RouteLocatorBuilder, authFilter: AuthFilter): RouteLocator? {
        return builder.routes()
            .route {
                it
                    .path("/api/v1/**")
                    .uri(uriConfiguration.legacy)
            }
            .route {
                it
                    .path("/api/v1.0.0/**")
                    .uri(uriConfiguration.legacy)
            }.build()
    }
}
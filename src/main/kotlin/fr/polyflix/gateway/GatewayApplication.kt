package fr.polyflix.gateway

import fr.polyflix.gateway.config.UriConfiguration
import fr.polyflix.gateway.filters.AuthFilter
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.boot.runApplication
import org.springframework.cloud.gateway.route.RouteLocator
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder
import org.springframework.context.annotation.Bean

@SpringBootApplication
@EnableConfigurationProperties(UriConfiguration::class)
class GatewayApplication {
    @Bean
    fun myRoutes(builder: RouteLocatorBuilder, uriConfiguration: UriConfiguration, authFilter: AuthFilter): RouteLocator? {
        return builder.routes()
            .route {
                it
                    .path("/api/*/note/**")
                    .filters { f ->
                        f.rewritePath(
                            "api/(?<version>.*)/note/(?<segment>.*)",
                            "api/\${version}/\${segment}"
                        )
                    }
                    .uri(uriConfiguration.note)
            }
            .route {
                it
                    .path("/api/*/video/**")
                    .filters { f ->
                        f.rewritePath(
                            "api/(?<version>.*)/video/(?<segment>.*)",
                            "api/\${version}/\${segment}"
                        )
                    }
                    .uri(uriConfiguration.video)
            }
            .route {
                it
                    .path("/api/**")
                    .uri(uriConfiguration.legacy)
            }.build()
    }
}

fun main(args: Array<String>) {
    runApplication<GatewayApplication>(*args)
}

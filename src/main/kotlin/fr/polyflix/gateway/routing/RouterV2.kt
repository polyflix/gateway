package fr.polyflix.gateway.routing

import fr.polyflix.gateway.config.UriConfiguration
import fr.polyflix.gateway.filters.AuthFilter
import org.springframework.cloud.gateway.route.RouteLocator
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder
import org.springframework.context.annotation.Bean
import org.springframework.stereotype.Component

@Component
class RouterV2(private val uriConfiguration: UriConfiguration) {
    @Bean
    fun v2(builder: RouteLocatorBuilder, authFilter: AuthFilter): RouteLocator? {
        return builder.routes()
            .route {
                it
                    .path("/api/v2.0.0/videos/**")
                    .filters { f ->
                        f.stripPrefix(1)
                    }
                    .uri(uriConfiguration.video)
            }
            .route {
                it
                    .path("/api/v2.0.0/notes/**")
                    .filters { f ->
                        f.stripPrefix(1)
                    }
                    .uri(uriConfiguration.note)
            }
            .route {
                it
                    .path("/api/v2.0.0/quizzes/**")
                    .filters { f ->
                        f.stripPrefix(1)
                    }
                    .uri(uriConfiguration.quizz)
            }
            .route {
                it
                    .path("/api/v2.0.0/certifications/**")
                    .filters { f ->
                        f.stripPrefix(1)
                    }
                    .uri(uriConfiguration.certification)
            }
            .route {
                it
                    .path("/api/v2.0.0/attachements/**")
                    .filters { f ->
                        f.stripPrefix(1)
                    }
                    .uri(uriConfiguration.attachment)
            }
            .route {
                it
                    .path("/api/v2.0.0/search/**")
                    .filters { f ->
                        f.stripPrefix(1)
                    }
                    .uri(uriConfiguration.search)
            }
            .route {
                it
                    .path("/api/v2.0.0/cursus/**").and().path("/api/v2.0.0/courses/**")
                    .filters { f ->
                        f.stripPrefix(1)
                    }
                    .uri(uriConfiguration.catalog)
            }.build()
    }
}
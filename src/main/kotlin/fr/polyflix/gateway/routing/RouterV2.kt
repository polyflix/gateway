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
                    .path("/api/v2.0.0/admin/videos/**")
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
                    .path("/api/v2.0.0/attachments/**")
                    .filters { f ->
                        f.stripPrefix(1)
                    }
                    .uri(uriConfiguration.attachment)
            }
            .route {
                it
                    .path("/api/v2.0.0/search/**")
                    .filters { f ->
                        f.stripPrefix(2)
                    }
                    .uri(uriConfiguration.search)
            }
            .route {
                it
                    .path("/api/v2.0.0/subtitles/**")
                    .filters { f ->
                        f.stripPrefix(1)
                    }
                    .uri(uriConfiguration.subtitle)
            }
            .route {
                it
                    .path("/api/v2.0.0/admin/subtitles/**")
                    .filters { f ->
                        f.stripPrefix(1)
                    }
                    .uri(uriConfiguration.subtitle)
            }
            .route {
                it                    
                    .path("/api/v2.0.0/cursus/**")
                    .or()
                    .path("/api/v2.0.0/courses/**")
                    .or()
                    .path("/api/v2.0.0/modules/**")
                    .filters { f ->
                        f.stripPrefix(1)
                    }
                    .uri(uriConfiguration.catalog)
            }

            .route {
                it
                    .path("/api/v2.0.0/users/**")
                    .or()
                    .path("/api/v2.0.0/permissions/**")
                    .or()
                    .path("/api/v2.0.0/groups/**")
                    .or()
                    .path("/api/v2.0.0/roles/**")
                    .filters { f ->
                        f.stripPrefix(2)
                    }
                    .uri(uriConfiguration.user)
            }.build()
    }
}

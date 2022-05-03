package fr.polyflix.gateway.config

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.boot.context.properties.ConstructorBinding

@ConstructorBinding
@ConfigurationProperties(prefix = "polyflix.services")
data class UriConfiguration(
    val legacy: String?,
    val video: String?,
    val quiz: String?,
    val note: String?
)
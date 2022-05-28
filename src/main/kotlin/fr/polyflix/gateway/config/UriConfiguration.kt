package fr.polyflix.gateway.config

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.boot.context.properties.ConstructorBinding

@ConstructorBinding
@ConfigurationProperties(prefix = "polyflix.services")
data class UriConfiguration(
    val legacy: String?,
    val video: String?,
    val quizz: String?,
    val note: String?,
    val certification: String?,
    val attachment: String?,
    val catalog: String?,
    val search: String?,
    val user: String?
)
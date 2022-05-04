package fr.polyflix.gateway.filters

import fr.polyflix.gateway.models.User
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Value
import org.springframework.boot.web.client.RestTemplateBuilder
import org.springframework.cloud.gateway.filter.GatewayFilterChain
import org.springframework.cloud.gateway.filter.GlobalFilter
import org.springframework.http.HttpEntity
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpMethod
import org.springframework.stereotype.Component
import org.springframework.web.client.HttpClientErrorException
import org.springframework.web.server.ServerWebExchange
import reactor.core.publisher.Mono

@Component
class AuthFilter: GlobalFilter {
    @Value("\${polyflix.services.legacy}")
    private val legacyUrl = ""

    private val logger = LoggerFactory.getLogger(this::class.java)

    private fun getUserFromLegacy(authorizationToken: String): User? {
        // Set the headers for the get user request
        val headers = HttpHeaders()
        headers.set("Authorization", "Bearer $authorizationToken")

        try {
            val httpEntity = HttpEntity<User>(headers)
            val responseEntity = RestTemplateBuilder()
                .build()
                .exchange("${legacyUrl}/api/v1/users/me", HttpMethod.GET, httpEntity, User::class.java)

            logger.info("Successfully retrieved authenticated ${responseEntity.body}")

            return responseEntity.body
        } catch(e: HttpClientErrorException) {
            logger.error("Failed to get the authenticated user. Details = ${e.message}")
        }
        return null
    }

    private fun extractTokenFromAuthorizationHeader(authorizationHeader: String?) : String? {
        return authorizationHeader?.split(" ")?.get(1)
    }

    override fun filter(exchange: ServerWebExchange, chain: GatewayFilterChain): Mono<Void> {
        val authorizationHeader = extractTokenFromAuthorizationHeader(exchange.request.headers.getFirst("Authorization")) ?: return chain.filter(exchange)

        logger.info("Trying to retrieve the authenticated user")
        val user = getUserFromLegacy(authorizationHeader)

        val request = exchange.request
            .mutate()
            .header("Authorization", authorizationHeader)
            // Add headers for microservices
            .header("X-User-Id", user?.id)
            .header("X-User-Roles", user?.roles?.joinToString(separator = ","))
            .build()

        return chain.filter(exchange.mutate().request(request).build())
    }

}
package fr.polyflix.gateway.filters

import com.google.gson.Gson
import com.google.gson.JsonObject
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
import java.util.Base64

@Component
class AuthFilter: GlobalFilter {
    @Value("\${polyflix.services.user}")
    private val userService = ""

    private val logger = LoggerFactory.getLogger(javaClass)

    private fun getUser(authorizationToken: String): User? {
        // Set the headers for the get user request
        val headers = HttpHeaders()
        headers.set("Authorization", "Bearer $authorizationToken")
        try {
            val httpEntity = HttpEntity<User>(headers)
            val responseEntity = RestTemplateBuilder()
                .build()
                .exchange("${userService}/users/${getSubFromToken(authorizationToken)}", HttpMethod.GET, httpEntity, User::class.java)

            return responseEntity.body
        } catch(e: HttpClientErrorException) {
            logger.error("Failed to get the authenticated user. Details = ${e.message}")
        }
        return null
    }

    private fun extractTokenFromAuthorizationHeader(authorizationHeader: String?) : String? {
        return authorizationHeader?.split(" ")?.get(1)
    }

    /**
     * Decode the JWT to extract the sub, which corresponds to the user id.
     */
    private fun getSubFromToken(authorizationToken: String): String {
        val chunks = authorizationToken.split(".")
        val decoder = Base64.getUrlDecoder()
        val payload = Gson().fromJson(String(decoder.decode(chunks[1])), JsonObject::class.java)
        return payload.get("sub").asString
    }

    override fun filter(exchange: ServerWebExchange, chain: GatewayFilterChain): Mono<Void> {
        val authorizationHeader = extractTokenFromAuthorizationHeader(exchange.request.headers.getFirst("Authorization")) ?: return chain.filter(exchange)
        val user = getUser(authorizationHeader)

        val request = exchange.request
            .mutate()
            .header("Authorization", "Bearer $authorizationHeader")
            // Add headers for microservices
            .header("X-User-Id", user?.id)
            .header("X-User-Roles", user?.roles?.joinToString(separator = ","))
            .build()

        return chain.filter(exchange.mutate().request(request).build())
    }

}

package fr.polyflix.gateway.filters

import org.slf4j.LoggerFactory
import org.springframework.cloud.gateway.filter.GatewayFilterChain
import org.springframework.cloud.gateway.filter.GlobalFilter
import org.springframework.cloud.gateway.route.Route
import org.springframework.web.server.ServerWebExchange
import reactor.core.publisher.Mono

import org.springframework.cloud.gateway.support.ServerWebExchangeUtils.GATEWAY_ORIGINAL_REQUEST_URL_ATTR
import org.springframework.cloud.gateway.support.ServerWebExchangeUtils.GATEWAY_REQUEST_URL_ATTR
import org.springframework.cloud.gateway.support.ServerWebExchangeUtils.GATEWAY_ROUTE_ATTR
import org.springframework.stereotype.Component
import java.net.URI

@Component
class LoggingFilter: GlobalFilter {
    private val logger = LoggerFactory.getLogger(javaClass)

    override fun filter(exchange: ServerWebExchange, chain: GatewayFilterChain): Mono<Void> {
        val uris: Set<URI> = exchange.getAttributeOrDefault(GATEWAY_ORIGINAL_REQUEST_URL_ATTR, emptySet())

        // Request attributes
        val method = exchange.request.method

        // URI informations extraction
        val originalUri = if (uris.isEmpty()) "Unknown" else uris.iterator().next().toString()
        val routeUri: URI? = exchange.getAttribute(GATEWAY_REQUEST_URL_ATTR)

        logger.info("$method $originalUri routed to $routeUri")

        return chain.filter(exchange)
    }

}
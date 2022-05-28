package fr.polyflix.gateway.config

import org.slf4j.LoggerFactory
import org.springframework.cloud.gateway.support.ServerWebExchangeUtils.GATEWAY_ROUTE_ATTR
import org.springframework.boot.web.reactive.error.ErrorWebExceptionHandler
import org.springframework.cloud.gateway.route.Route
import org.springframework.context.annotation.Configuration
import org.springframework.http.HttpStatus
import org.springframework.web.server.ServerWebExchange
import reactor.core.publisher.Mono
import java.net.ConnectException

@Configuration
class ExceptionHandler: ErrorWebExceptionHandler {
    private val logger = LoggerFactory.getLogger(javaClass)

    override fun handle(exchange: ServerWebExchange, ex: Throwable): Mono<Void> {
        val response = exchange.response
        val request = exchange.request
        val route: Route? = exchange.getAttribute(GATEWAY_ROUTE_ATTR)

        // If ex is ConnectException :
        // it probably means that the requested service is unavailable at this moment
        // so we return 503 to the caller
        if (ex is ConnectException) {
            response.statusCode = HttpStatus.SERVICE_UNAVAILABLE
            logger.error("ConnectException on : ${route?.uri}. Is the service up and running ?")
            return response.setComplete()
        }

        logger.warn("${request.method} ${request.uri} caused error : ${ex.message}")

        return Mono.error(ex)
    }
}
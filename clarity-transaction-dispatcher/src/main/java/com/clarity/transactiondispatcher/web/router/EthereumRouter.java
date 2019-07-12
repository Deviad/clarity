package com.clarity.transactiondispatcher.web.router;

import com.clarity.transactiondispatcher.web.handler.EthereumHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerResponse;

import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.web.reactive.function.server.RequestPredicates.*;

@Configuration
public class EthereumRouter {

    @Bean
    public RouterFunction<ServerResponse> route(EthereumHandler ethereumHandler) {
        return RouterFunctions.route(GET("/ethtransaction/{id}").and(accept(APPLICATION_JSON)), ethereumHandler::readTransaction)
                .andRoute(POST("/ethtransaction").and(accept(APPLICATION_JSON)).and(contentType(APPLICATION_JSON)), ethereumHandler::createTransaction)
                .andRoute(GET("/ethtransaction").and(accept(APPLICATION_JSON)), ethereumHandler::readAll);
    }
}

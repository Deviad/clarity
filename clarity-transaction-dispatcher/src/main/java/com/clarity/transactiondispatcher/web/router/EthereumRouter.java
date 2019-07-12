package com.clarity.transactiondispatcher.web.router;

import com.clarity.transactiondispatcher.web.controller.EthereumController;
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
    public RouterFunction<ServerResponse> route(EthereumController ethereumController) {
        return RouterFunctions.route(GET("/ethtransaction/{id}").and(accept(APPLICATION_JSON)), ethereumController::readTransaction)
                .andRoute(POST("/ethtransaction").and(accept(APPLICATION_JSON)).and(contentType(APPLICATION_JSON)), ethereumController::createTransaction)
                .andRoute(GET("/ethtransaction").and(accept(APPLICATION_JSON)), ethereumController::readAll);
    }
}

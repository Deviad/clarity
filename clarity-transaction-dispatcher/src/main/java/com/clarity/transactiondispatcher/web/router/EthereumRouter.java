package com.clarity.transactiondispatcher.web.router;

import com.clarity.transactiondispatcher.web.controller.EthereumAccountController;
import com.clarity.transactiondispatcher.web.controller.EthereumTransactionController;
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
    public RouterFunction<ServerResponse> ethTransRoute(EthereumTransactionController ethereumTransactionController) {
        return RouterFunctions.route(GET("/ethtransaction/{id}").and(accept(APPLICATION_JSON)), ethereumTransactionController::readTransaction)
                .andRoute(POST("/ethtransaction").and(accept(APPLICATION_JSON)).and(contentType(APPLICATION_JSON)), ethereumTransactionController::createTransaction)
                .andRoute(GET("/ethtransaction").and(accept(APPLICATION_JSON)), ethereumTransactionController::readAll);
    }

    @Bean
    public RouterFunction<ServerResponse> ethAccRoute(EthereumAccountController ethereumAccountController) {
        return RouterFunctions.route(GET("/ethaccount/{id}").and(accept(APPLICATION_JSON)), ethereumAccountController::createAccount)
                .andRoute(POST("/ethAccount").and(accept(APPLICATION_JSON)).and(contentType(APPLICATION_JSON)), ethereumAccountController::createAccount)
                .andRoute(GET("/ethAccount").and(accept(APPLICATION_JSON)), ethereumAccountController::createAccount);
    }
}

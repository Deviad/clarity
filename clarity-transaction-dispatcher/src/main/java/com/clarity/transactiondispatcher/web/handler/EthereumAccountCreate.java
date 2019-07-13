package com.clarity.transactiondispatcher.web.handler;


import com.clarity.clarityshared.Query;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.jooq.lambda.Unchecked;
import org.springframework.stereotype.Component;

import java.util.concurrent.CompletableFuture;

public class EthereumAccountCreate implements Query<CompletableFuture<String>> {
    private String json;

    public EthereumAccountCreate(Object object) {
        this.init(object);
    }

    private void init(Object object) {
        ObjectMapper objectMapper = new ObjectMapper();
        this.json = Unchecked.function(objectMapper::writeValueAsString).apply(object);
    }

    String toJSON() {
        return json;
    }

    @Component
    static class Handler implements Query.Handler<EthereumAccountCreate, CompletableFuture<String>> {

        @Override
        public CompletableFuture<String> handle(EthereumAccountCreate command) {

            return CompletableFuture.completedFuture(command.toJSON());
        }
    }
}

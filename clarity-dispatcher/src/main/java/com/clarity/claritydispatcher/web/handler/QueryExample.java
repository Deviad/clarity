package com.clarity.claritydispatcher.web.handler;

import com.clarity.clarityshared.Query;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jooq.lambda.Unchecked;

import javax.inject.Singleton;
import java.util.concurrent.CompletableFuture;
@Slf4j
@AllArgsConstructor
public class QueryExample implements Query<CompletableFuture<String>> {
    private String json;

    public QueryExample(Object object) {
        this.init(object);
    }

    private void init(Object object) {
        ObjectMapper objectMapper = new ObjectMapper();
        this.json = Unchecked.function(objectMapper::writeValueAsString).apply(object);
    }

    private String toJSON() {
        return json;
    }

    @Singleton
    static class Handler implements Query.Handler<QueryExample, CompletableFuture<String>> {

        @Override
        public CompletableFuture<String> handle(QueryExample command) {

            return CompletableFuture.completedFuture(command.toJSON());
        }
    }
}

package com.clarity.transactiondispatcher.web.validation;


import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

import javax.validation.Validator;
import java.util.function.Function;

@Component
@Slf4j
public class RequestHandler {

    private final Validator validator;

    public RequestHandler(Validator validator) {
        this.validator = validator;
    }

    public <BODY> Mono<ServerResponse> requireValidBody(
            Function<Mono<BODY>, Mono<ServerResponse>> block,
            ServerRequest request, Class<BODY> bodyClass) {

        return request
                .bodyToMono(bodyClass)
                .flatMap(
                        body -> {
                            log.info("Emptyness" +validator.validate(body).isEmpty() + "");
                           return validator.validate(body).isEmpty()
                                    ? block.apply(Mono.just(body))
                                    : ServerResponse.unprocessableEntity().build();
                        }
                );
    }
}


package com.clarity.claritydispatcher.error.exceptionhandlers;

import com.clarity.claritydispatcher.error.exceptions.NoMessageReceivedException;
import com.clarity.claritydispatcher.service.ResponseFactory;
import io.micronaut.context.annotation.Requires;
import io.micronaut.http.HttpRequest;
import io.micronaut.http.HttpResponse;
import io.micronaut.http.annotation.Produces;
import io.micronaut.http.server.exceptions.ExceptionHandler;

import javax.inject.Singleton;

@Produces
@Singleton
public class NoMessageReceivedExceptionHandler implements ExceptionHandler<NoMessageReceivedException, HttpResponse>, ResponseFactory {
    @Override
    @Requires(classes = {NoMessageReceivedException.class, ExceptionHandler.class})
    public HttpResponse handle(HttpRequest request, NoMessageReceivedException exception) {
        return HttpResponse.serverError(getJsonErrScalarResp(exception.getStackTrace()));
    }
}

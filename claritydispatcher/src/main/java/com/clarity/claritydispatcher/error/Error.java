package com.clarity.claritydispatcher.error;
import com.clarity.claritydispatcher.service.ResponseFactory;
import com.clarity.claritydispatcher.util.JSONAble;
import io.micronaut.context.annotation.Requires;
import io.micronaut.http.HttpRequest;
import io.micronaut.http.HttpResponse;
import io.micronaut.http.annotation.Produces;
import io.micronaut.http.server.exceptions.ExceptionHandler;
import javax.inject.Singleton;

@Produces
@Singleton

public class Error implements ExceptionHandler<NoMessageReceived, HttpResponse>, ResponseFactory {

    @Override
    @Requires(classes = {NoMessageReceived.class, ExceptionHandler.class})
    public HttpResponse handle(HttpRequest request, NoMessageReceived exception) {
        return HttpResponse.serverError(this.getJsonErrsResp(exception.getStackTrace()));
    }


}

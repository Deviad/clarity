package clarity.dispatcher;


import clarity.dispatcher.services.Web3jService;
import io.micronaut.context.ApplicationContext;
import io.micronaut.context.annotation.Bean;
import io.micronaut.context.annotation.Factory;
import io.micronaut.http.HttpRequest;
import io.micronaut.http.client.annotation.Client;
import io.micronaut.websocket.RxWebSocketClient;
import io.micronaut.websocket.WebSocketSession;
import io.micronaut.websocket.annotation.ClientWebSocket;
import io.micronaut.websocket.annotation.OnOpen;
import io.reactivex.Flowable;
import lombok.Getter;
import lombok.Setter;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.AbstractMap;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Factory
public class WebsocketClientConfiguration {

    @Inject
    Web3jService web3jService;

    @Inject
    @Client("${infura.ws-endpoint}")
    RxWebSocketClient webSocketClient;

    @Bean
    @Singleton
    Flowable<EthereumClientWebSocket> webSocketClient(ApplicationContext applicationContext) {

        Map<String, Object> map = Stream.of(
                new AbstractMap.SimpleEntry<>("id", 1),
                new AbstractMap.SimpleEntry<>("method", "eth_subscribe"),
                new AbstractMap.SimpleEntry<>("params", new Object[]{"newHeads"}))
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));

        return webSocketClient.connect(EthereumClientWebSocket.class, "/");

    }

}

@ClientWebSocket("/")
@Getter
@Setter

abstract class EthereumClientWebSocket implements AutoCloseable {

    private WebSocketSession session;
    private HttpRequest request;

    @OnOpen
    public void onOpen(WebSocketSession session, HttpRequest request) {
        this.session = session;
        this.request = request;
    }
}

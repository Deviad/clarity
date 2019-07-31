package clarity.dispatcher;
//


import com.fasterxml.jackson.databind.ObjectMapper;
import io.micronaut.context.annotation.Factory;
import io.micronaut.http.client.annotation.Client;
import io.micronaut.websocket.RxWebSocketClient;
import io.reactivex.Flowable;

import java.util.AbstractMap;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Factory
public class EthereumWebsocketClient  {

    private RxWebSocketClient client;
    private ObjectMapper objectMapper = new ObjectMapper();

    public EthereumWebsocketClient(@Client("ws://localhost:8546") RxWebSocketClient client) {
        this.client = client;
    }

    public Flowable<EthereumLowLevelWebsocketClient> connect(Map<String, Object> reqParams) {

        return client.connect(EthereumLowLevelWebsocketClient.class, reqParams);

    }



}

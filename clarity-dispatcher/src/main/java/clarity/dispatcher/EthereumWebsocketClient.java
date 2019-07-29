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

    public EthereumWebsocketClient(@Client("/") RxWebSocketClient client) {
        this.client = client;
    }

    public Flowable<EthereumLowLevelWebsocketClient> connect(Map<String, Object> reqParams) {
        Map<String, Object> map = Stream.of(
        new AbstractMap.SimpleEntry<>("id", 1),
        new AbstractMap.SimpleEntry<>("method", "eth_subscribe"),
        new AbstractMap.SimpleEntry<>("params", new Object[]{"newHeads"}))
        .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
//        String json = Unchecked.supplier(()->objectMapper.writeValueAsString(map)).get();

        return client.connect(EthereumLowLevelWebsocketClient.class, map);

    }



}

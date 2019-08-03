package clarity.dispatcher;
//


import io.micronaut.context.annotation.Factory;
import io.micronaut.http.client.annotation.Client;
import io.micronaut.websocket.RxWebSocketClient;
import io.reactivex.Flowable;

import java.util.Map;

@Factory
public class EthereumWebsocketClient  {

    private RxWebSocketClient client;
    public EthereumWebsocketClient(@Client("/") RxWebSocketClient client) {
        this.client = client;
    }
    public Flowable<EthereumLowLevelWebsocketClient> connect(Map<String, Object> reqParams) {
        return client.connect(EthereumLowLevelWebsocketClient.class, reqParams);

    }



}

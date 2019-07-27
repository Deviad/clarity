package clarity.dispatcher;

import clarity.dispatcher.services.Web3jService;
import io.micronaut.discovery.event.ServiceStartedEvent;
import io.micronaut.runtime.event.annotation.EventListener;
import io.micronaut.websocket.WebSocketSession;
import io.reactivex.Flowable;
import lombok.extern.slf4j.Slf4j;

import javax.inject.Inject;
import javax.inject.Named;
import javax.inject.Singleton;
import java.util.AbstractMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Singleton
@Slf4j
public class EthereumClient {

    private final Web3jService web3jService;

   private final Flowable<EthereumClientWebSocket> ethereumWs;

    @Inject
    public EthereumClient(Web3jService web3jService, Flowable<EthereumClientWebSocket> ethereumWs) {

        this.web3jService = web3jService;
        this.ethereumWs = ethereumWs;
    }


//    StandardCharsets.UTF_8.decode().toString()
    @EventListener
    public void registerHook(ServiceStartedEvent serviceStartedEvent) {
        log.info("Endpoint {}", web3jService.getInfuraHttpsEndpoint());

        Map<String, Object> map = Stream.of(
                new AbstractMap.SimpleEntry<>("id", 1),
                new AbstractMap.SimpleEntry<>("method", "eth_subscribe"),
                new AbstractMap.SimpleEntry<>("params", new Object[]{"newHeads"}))
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));

        ethereumWs.delay(1, TimeUnit.SECONDS)
                .map(EthereumClientWebSocket::getSession)
                .map(WebSocketSession::getOpenSessions)
                .map(x-> x.stream().map(String::valueOf).collect(Collectors.joining()))
                .subscribe(log::info);
    }

}

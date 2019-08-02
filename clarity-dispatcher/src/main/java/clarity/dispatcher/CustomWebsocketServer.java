package clarity.dispatcher;

import io.micronaut.http.MediaType;
import io.micronaut.websocket.WebSocketBroadcaster;
import io.micronaut.websocket.WebSocketSession;
import io.micronaut.websocket.annotation.OnClose;
import io.micronaut.websocket.annotation.OnMessage;
import io.micronaut.websocket.annotation.OnOpen;
import io.micronaut.websocket.annotation.ServerWebSocket;
import lombok.extern.slf4j.Slf4j;

import java.util.AbstractMap;
import java.util.Map;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@ServerWebSocket("/ws/{topic}/{username}")
@Slf4j
public class CustomWebsocketServer {

  private WebSocketBroadcaster broadcaster;
  private EthereumWebsocketClient client;

  public CustomWebsocketServer(WebSocketBroadcaster broadcaster, EthereumWebsocketClient client) {
    this.client = client;
    this.broadcaster = broadcaster;
  }

  @OnOpen
  public void onOpen(String topic, String username, WebSocketSession session) {}

  @OnMessage
  public void onMessage(String topic, String username, String message, WebSocketSession session) {
    //        String msg = "[" + username + "] " + message;
    Map<String, Object> map =
        Stream.of(
                new AbstractMap.SimpleEntry<>("id", 1),
                new AbstractMap.SimpleEntry<>("method", "eth_subscribe"),
                new AbstractMap.SimpleEntry<>("params", new Object[] {"newHeads"}))
            .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));

    client
        .connect(map)
        .map(EthereumLowLevelWebsocketClient::getMessages)
        .doOnNext(
            x ->
                broadcaster.broadcastSync(
                    x.poll(), MediaType.TEXT_EVENT_STREAM_TYPE, (o)->true))
        .doOnError(Throwable::printStackTrace)
        .subscribe();
  }

  @OnClose
  public void onClose(String topic, String username, WebSocketSession session) {
    String msg = "[" + username + "] Disconnected!";
    broadcaster.broadcastSync(msg, (o)->true);
  }

  private Predicate<WebSocketSession> isValid(String topic, WebSocketSession session) {
    return s ->
        s != session
            && topic.equalsIgnoreCase(s.getUriVariables().get("topic", String.class, null));
  }
}

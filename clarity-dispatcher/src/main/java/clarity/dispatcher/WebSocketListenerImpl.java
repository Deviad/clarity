package clarity.dispatcher;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import okhttp3.Response;
import okhttp3.WebSocket;
import okhttp3.WebSocketListener;
import okio.ByteString;
import org.jooq.lambda.Unchecked;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.AbstractMap;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Slf4j
@Singleton
public class WebSocketListenerImpl extends WebSocketListener {

  private static final int NORMAL_CLOSURE_STATUS = 1000;
  private MyRxBus<String> bus;

  @Inject
  WebSocketListenerImpl(MyRxBus<String> bus) {
    this.bus = bus;
  }

  @Override
  public void onOpen(WebSocket webSocket, Response response) {}

  @Override
  public void onMessage(WebSocket webSocket, String text) {
    log.info(text);
    Map<String, Object> map =
        Stream.of(
                new AbstractMap.SimpleEntry<>("id", 1),
                new AbstractMap.SimpleEntry<>("method", "eth_subscribe"),
                new AbstractMap.SimpleEntry<>("params", new Object[] {"newHeads"}))
            .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
    final ObjectMapper mapper = new ObjectMapper();
    String json = Unchecked.supplier(() -> mapper.writeValueAsString(map)).get();
    bus.setObject(text);
    webSocket.send(json);
  }

  @Override
  public void onMessage(WebSocket webSocket, ByteString bytes) {}

  @Override
  public void onClosing(WebSocket webSocket, int code, String reason) {
    webSocket.close(NORMAL_CLOSURE_STATUS, null);
  }

  @Override
  public void onFailure(WebSocket webSocket, Throwable t, Response response) {}
}

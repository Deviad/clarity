package clarity.dispatcher;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.micronaut.runtime.http.scope.RequestScope;
import lombok.extern.slf4j.Slf4j;
import okhttp3.Response;
import okhttp3.WebSocket;
import okhttp3.WebSocketListener;
import okio.ByteString;

import javax.inject.Named;

@Slf4j
@RequestScope
public class WebSocketListenerImpl extends WebSocketListener {

  private static final int NORMAL_CLOSURE_STATUS = 1000;
  private MyRxBean<String> outputBus;
  private MyRxBean<String> inputBus;

  private String json = null;

  WebSocketListenerImpl(
      @Named("OUTPUT") MyRxBean<String> outputBus, @Named("INPUT") MyRxBean<String> inputBus) {
    this.outputBus = outputBus;
    this.inputBus = inputBus;
  }

  @Override
  public void onOpen(WebSocket webSocket, Response response) {}

  @Override
  public void onMessage(WebSocket webSocket, String text) {
    log.info(text);
    final ObjectMapper mapper = new ObjectMapper();
    if (json == null && inputBus.getEvents().replay(1).autoConnect().next().block() != null) {
      json = inputBus.getEvents().replay(1).autoConnect().next().block();
    }
    outputBus.setObject(text);
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

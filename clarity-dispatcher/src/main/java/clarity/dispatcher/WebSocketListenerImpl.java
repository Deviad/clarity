package clarity.dispatcher;

import lombok.extern.slf4j.Slf4j;
import okhttp3.Response;
import okhttp3.WebSocket;
import okhttp3.WebSocketListener;
import okio.ByteString;
import reactor.core.publisher.ConnectableFlux;

import javax.inject.Inject;
import javax.inject.Singleton;

@Slf4j
@Singleton
public class WebSocketListenerImpl extends WebSocketListener {

  private static final int NORMAL_CLOSURE_STATUS = 1000;
  @Inject private MyRxOutputBean<Output> outputBus;
  @Inject private MyRxInputBean<String> inputBus;

  private String json = null;

  @Override
  public void onOpen(WebSocket webSocket, Response response) {}

  @Override
  public void onMessage(WebSocket webSocket, String text) {
    log.info(text);

    final ConnectableFlux<String> replay = inputBus.getEvents().replay(1);

    if (json == null && replay.autoConnect().hasElements().block()) {
      json = replay.autoConnect().next().block();
    }

    Output output = new Output();
    output.setText(text);
    output.setWebSocket(webSocket);
    outputBus.setObject(output);
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

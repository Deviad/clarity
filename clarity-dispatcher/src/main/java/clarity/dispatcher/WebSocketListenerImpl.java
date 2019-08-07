package clarity.dispatcher;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import okhttp3.Response;
import okhttp3.WebSocket;
import okhttp3.WebSocketListener;
import okio.ByteString;

@Slf4j
public class WebSocketListenerImpl extends WebSocketListener {

  private static final int NORMAL_CLOSURE_STATUS = 1000;
  @Getter
  private final MyRxOutputBean<Output> outputBus;

  private String json;

  public WebSocketListenerImpl(MyRxOutputBean<Output> outputBus, String json) {
      this.outputBus = outputBus;
      this.json = json;
  }

  @Override
  public void onOpen(WebSocket webSocket, Response response) {
  }

  @Override
  public void onMessage(WebSocket webSocket, String text) {
    log.info(text);

    Output output = new Output();
    output.setText(text);
    outputBus.setObject(output);
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

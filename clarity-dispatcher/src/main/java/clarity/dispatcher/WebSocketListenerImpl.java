package clarity.dispatcher;

import io.reactivex.Observable;
import lombok.extern.slf4j.Slf4j;
import okhttp3.Response;
import okhttp3.WebSocket;
import okhttp3.WebSocketListener;
import okio.ByteString;

import javax.inject.Inject;
import javax.inject.Singleton;

@Slf4j
@Singleton
public class WebSocketListenerImpl extends WebSocketListener {

  private MyRxBus<String> bus;
  @Inject
  WebSocketListenerImpl(MyRxBus<String> bus) {
    this.bus = bus;
  }

  private static final int NORMAL_CLOSURE_STATUS = 1000;

  @Override
  public void onOpen(WebSocket webSocket, Response response) {
  }

  @Override
  public void onMessage(WebSocket webSocket, String text) {
    //            output("Receiving : " + text);
    log.info(text);

    bus.setObject(text);
  }

  @Override
  public void onMessage(WebSocket webSocket, ByteString bytes) {
  }

  @Override
  public void onClosing(WebSocket webSocket, int code, String reason) {
    webSocket.close(NORMAL_CLOSURE_STATUS, null);
  }

  @Override
  public void onFailure(WebSocket webSocket, Throwable t, Response response) {
  }
}

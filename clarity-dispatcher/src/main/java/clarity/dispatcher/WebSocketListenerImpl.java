package clarity.dispatcher;

import io.reactivex.Observable;
import lombok.extern.slf4j.Slf4j;
import okhttp3.Response;
import okhttp3.WebSocket;
import okhttp3.WebSocketListener;
import okio.ByteString;

import javax.inject.Inject;

@Slf4j
public class WebSocketListenerImpl extends WebSocketListener {

  @Inject MyRxBus<String> bus;

  private static final int NORMAL_CLOSURE_STATUS = 1000;

  @Override
  public void onOpen(WebSocket webSocket, Response response) {
    //            webSocket.send("Hello, it's SSaurel !");
    //            webSocket.send("What's up ?");
    //            webSocket.send(ByteString.decodeHex("deadbeef"));
    //            webSocket.close(NORMAL_CLOSURE_STATUS, "Goodbye !");
  }

  @Override
  public void onMessage(WebSocket webSocket, String text) {
    //            output("Receiving : " + text);
    log.info(text);

    bus.setObject(text);
  }

  @Override
  public void onMessage(WebSocket webSocket, ByteString bytes) {
    //            output("Receiving bytes : " + bytes.hex());
  }

  @Override
  public void onClosing(WebSocket webSocket, int code, String reason) {
    webSocket.close(NORMAL_CLOSURE_STATUS, null);
    //            output("Closing : " + code + " / " + reason);
  }

  @Override
  public void onFailure(WebSocket webSocket, Throwable t, Response response) {
    //            output("Error : " + t.getMessage());
  }
}

package clarity.dispatcher;

import lombok.Getter;
import okhttp3.OkHttpClient;
import okhttp3.Request;

public class ConnectionFacade implements AutoCloseable {

  @Getter private final MyRxOutputBean<Output> outputBus = new MyRxOutputBean<>();
  @Getter private final OkHttpClient client = new OkHttpClient();
  private final Request request = new Request.Builder().url("ws://127.0.0.1:8546").build();

  void connect(String json) {
    WebSocketListenerImpl listener = new WebSocketListenerImpl(outputBus, json);
    client.newWebSocket(request, listener).send(json);
  }

  @Override
  public void close() throws Exception {
    //        client.dispatcher().executorService().shutdown();
  }
}

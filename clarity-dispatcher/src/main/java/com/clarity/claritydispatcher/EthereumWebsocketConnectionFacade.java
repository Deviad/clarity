package com.clarity.claritydispatcher;

import lombok.Getter;
import lombok.Setter;
import okhttp3.OkHttpClient;
import okhttp3.Request;

public class EthereumWebsocketConnectionFacade implements AutoCloseable {

  @Getter private final MyRxOutputBean<Output> outputBus = new MyRxOutputBean<>();
  private final Request request = new Request.Builder().url("ws://127.0.0.1:8546").build();
  @Getter @Setter private OkHttpClient client = new OkHttpClient();
  @Getter @Setter private WebSocketListenerImpl webSocketListener;

  void connect(String json) {
    WebSocketListenerImpl listener = new WebSocketListenerImpl(outputBus, json);
    client.newWebSocket(request, listener).send(json);
  }

  @Override
  public void close() throws Exception {
    //        client.dispatcher().executorService().shutdown();
  }
}

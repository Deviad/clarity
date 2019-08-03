package clarity.dispatcher;

import io.micronaut.http.HttpRequest;
import io.micronaut.websocket.WebSocketSession;
import io.micronaut.websocket.annotation.ClientWebSocket;
import io.micronaut.websocket.annotation.OnMessage;
import io.micronaut.websocket.annotation.OnOpen;
import io.reactivex.Flowable;
import io.reactivex.disposables.Disposable;

import java.util.Queue;

@ClientWebSocket("http://localhost:8546")
public abstract class EthereumLowLevelWebsocketClient implements AutoCloseable {
  private WebSocketSession session;
  private HttpRequest request;
  private String topic;
  private String username;
  private Flowable<Queue<String>> messages;

  @OnOpen
  public void onOpen(WebSocketSession session, HttpRequest request) {
    this.topic = topic;
    this.username = username;
    this.session = session;
    this.request = request;
  }

  public String getTopic() {
    return topic;
  }

  public String getUsername() {
    return username;
  }

  public Disposable getMessages() {
    return messages.subscribe(Queue::poll);
  }

  public WebSocketSession getSession() {
    return session;
  }

  public HttpRequest getRequest() {
    return request;
  }

  @OnMessage
  public void onMessage(Flowable<String> message) {
    messages.map(x-> x.offer(message.blockingFirst())).subscribe();
  }
}

package clarity.dispatcher;

import io.micronaut.http.HttpRequest;
import io.micronaut.websocket.WebSocketSession;
import io.micronaut.websocket.annotation.ClientWebSocket;
import io.micronaut.websocket.annotation.OnMessage;
import io.micronaut.websocket.annotation.OnOpen;

import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

@ClientWebSocket("http://localhost:8546")
public abstract class EthereumLowLevelWebsocketClient implements AutoCloseable {
  private WebSocketSession session;
  private HttpRequest request;
  private String topic;
  private String username;
  private Queue<String> messages = new ConcurrentLinkedQueue<>();

  @OnOpen
  public void onOpen(String topic, String username, WebSocketSession session, HttpRequest request) {
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

  public Queue<String> getMessages() {
    return messages;
  }

  public WebSocketSession getSession() {
    return session;
  }

  public HttpRequest getRequest() {
    return request;
  }

  @OnMessage
  public void onMessage(String message) {
    messages.offer(message);
  }
}

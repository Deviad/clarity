package clarity.dispatcher;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import okhttp3.Response;
import okhttp3.WebSocket;
import okhttp3.WebSocketListener;
import okio.Buffer;
import okio.BufferedSink;
import okio.ByteString;
import org.jooq.lambda.Unchecked;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.io.IOException;
import java.util.AbstractMap;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;
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
    //            output("Receiving : " + text);
    log.info(text);

    final Buffer buffer = new Buffer();

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
//    streamPrimesToSinkAsynchronously(buffer, json);
  }

  @Override
  public void onMessage(WebSocket webSocket, ByteString bytes) {}

  @Override
  public void onClosing(WebSocket webSocket, int code, String reason) {
    webSocket.close(NORMAL_CLOSURE_STATUS, null);
  }

  @Override
  public void onFailure(WebSocket webSocket, Throwable t, Response response) {}

  private void streamPrimesToSinkAsynchronously(final BufferedSink sink, String jsonString) {
//    Thread thread =
//        new Thread("writer") {
//          @Override
//          public void run() {
//            try {
//              for (int i = 2; i <= 997; i++) {
//                Thread.sleep(10);
//                sink.writeUtf8(jsonString);
//              }
//              sink.close();
//
//            } catch (IOException | InterruptedException e) {
//              e.printStackTrace();
//            }
//          }
//        };
//    thread.start();

//    final ScheduledExecutorService executorService = Executors.newSingleThreadScheduledExecutor();
//    executorService.scheduleAtFixedRate(
//            () -> Unchecked.runnable(() -> sink.writeUtf8(jsonString)).run(),
//            1,
//            1,
//            TimeUnit.SECONDS);

//    final Timer timer = new Timer("MyTimer");
//    timer.schedule(new TimerTask() {
//      @Override
//      public void run() {
//        Unchecked.runnable(() -> sink.writeUtf8(jsonString)).run();
//      }
//    }, 0, TimeUnit.SECONDS.toSeconds(1));

  }
}

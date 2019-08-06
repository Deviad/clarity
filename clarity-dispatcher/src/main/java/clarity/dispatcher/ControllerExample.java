package clarity.dispatcher;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.micronaut.http.MediaType;
import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Get;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import org.jooq.lambda.Unchecked;
import reactor.core.publisher.Flux;
import reactor.core.scheduler.Schedulers;

import javax.inject.Inject;
import java.time.Duration;
import java.util.AbstractMap;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Controller("/eth")
@Slf4j
public class ControllerExample {
  private WebSocketListenerImpl listener;
  private MyRxOutputBean<Output> outputBus;
  private MyRxInputBean<String> inputBus;

  @Inject
  ControllerExample(
      WebSocketListenerImpl listener,
      MyRxInputBean<String> inputBus,
      MyRxOutputBean<Output> outputBus) {
    this.listener = listener;
    this.inputBus = inputBus;
    this.outputBus = outputBus;
  }

  @Get(value = "/ssetest", produces = MediaType.TEXT_EVENT_STREAM)
  @SneakyThrows
  Flux<String> test() {

    Map<String, Object> map =
        Stream.of(
                new AbstractMap.SimpleEntry<>("id", 1),
                new AbstractMap.SimpleEntry<>("method", "eth_subscribe"),
                new AbstractMap.SimpleEntry<>("params", new Object[] {"newHeads"}))
            .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
    return connectionFactory(map);
  }

  @SneakyThrows
  private Flux<String> connectionFactory(Map<String, Object> map) {
    final ObjectMapper mapper = new ObjectMapper();
    String json = Unchecked.supplier(() -> mapper.writeValueAsString(map)).get();

    return Flux.interval(Duration.ofMillis(500))
        .doOnNext(x -> createConnection(json))
        .publishOn(Schedulers.elastic())
        .subscribeOn(Schedulers.single())
        .flatMap(x -> outputBus.getEvents().replay(1).autoConnect().map(Output::getText));
  }

  private void createConnection(String json) {
    final OkHttpClient client = new OkHttpClient();
    final Request request = new Request.Builder().url("ws://127.0.0.1:8546").build();
    inputBus.setObject(json);
    client.newWebSocket(request, listener).send(json);
  }
}

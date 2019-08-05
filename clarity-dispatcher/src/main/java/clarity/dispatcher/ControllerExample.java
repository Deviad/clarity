package clarity.dispatcher;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.micronaut.http.MediaType;
import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Get;
import lombok.SneakyThrows;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import org.jooq.lambda.Unchecked;
import reactor.core.publisher.Flux;

import javax.inject.Inject;
import javax.inject.Named;
import java.util.AbstractMap;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Controller("/eth")
public class ControllerExample {

  private WebSocketListenerImpl listener;
  private MyRxOutputBean<String> outputBus;
  private MyRxInputBean<String> inputBus;

  @Inject
  ControllerExample(WebSocketListenerImpl listener, MyRxInputBean<String> inputBus, MyRxOutputBean<String> outputBus) {
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
    whatever(map);
    return Flux.from(outputBus.getEvents().replay(1).autoConnect());
  }

  @SneakyThrows
  private void whatever(Map<String, Object> map) {
    final ObjectMapper mapper = new ObjectMapper();
    String json = Unchecked.supplier(() -> mapper.writeValueAsString(map)).get();
    final OkHttpClient client = new OkHttpClient();
    final Request request = new Request.Builder().url("ws://127.0.0.1:8546").build();
    inputBus.setObject(json);
    client.newWebSocket(request, listener).send(json);
    client.dispatcher().executorService().shutdown();
  }
}

package clarity.dispatcher;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Get;
import io.reactivex.Flowable;
import lombok.SneakyThrows;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import org.jooq.lambda.Unchecked;
import reactor.core.publisher.Flux;

import javax.inject.Inject;
import java.util.AbstractMap;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Controller
public class ControllerExample {

  @Inject MyRxBus<String> bus;
  @Inject WebSocketListenerImpl listener;

  @Get(value = "/ssetest")
  @SneakyThrows
  Flux<String> test() {

    Map<String, Object> map =
        Stream.of(
                new AbstractMap.SimpleEntry<>("id", 1),
                new AbstractMap.SimpleEntry<>("method", "eth_subscribe"),
                new AbstractMap.SimpleEntry<>("params", new Object[] {"newHeads"}))
            .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
    whatever(map);
    return Flux.from(bus.getEvents().replay(0).autoConnect());
  }

  private void whatever(Map<String, Object> map) {
    ObjectMapper mapper = new ObjectMapper();
    String json = Unchecked.supplier(() -> mapper.writeValueAsString(map)).get();
    OkHttpClient client = new OkHttpClient();
    Request request = new Request.Builder().url("ws://localhost:8546").build();
    client.newWebSocket(request, listener).send(json);
    client.dispatcher().executorService().shutdown();
  }
}

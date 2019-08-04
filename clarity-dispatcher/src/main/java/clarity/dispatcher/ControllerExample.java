package clarity.dispatcher;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Get;
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

  @SneakyThrows
  private void whatever(Map<String, Object> map) {
    final ObjectMapper mapper = new ObjectMapper();
    String json = Unchecked.supplier(() -> mapper.writeValueAsString(map)).get();
    final OkHttpClient client = new OkHttpClient();
    final Request request = new Request.Builder().url("ws://127.0.0.1:8546").build();
    client.newWebSocket(request, listener).send(json);
    client.dispatcher().executorService().shutdown();
    //    try (Response response = client.newCall(request).execute()) {
    //      if (!response.isSuccessful()) throw new IOException("Unexpected code " + response);
    //
    //      System.out.println(Unchecked.supplier(()->response.body().string()).get());
    //    }

  }
}

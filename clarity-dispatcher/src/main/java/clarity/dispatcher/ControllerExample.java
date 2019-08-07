package clarity.dispatcher;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.micronaut.http.MediaType;
import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Get;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.jooq.lambda.Unchecked;
import reactor.core.publisher.Flux;
import reactor.core.scheduler.Schedulers;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.time.Duration;
import java.util.AbstractMap;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Controller("/eth")
@Slf4j
public class ControllerExample {
  private ConnectionFacade cf;

  @PostConstruct
  void init() {
    cf = new ConnectionFacade();
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
        .doOnNext(x -> cf.connect(json))
        .publishOn(Schedulers.elastic())
        .subscribeOn(Schedulers.single())
        .flatMap(x -> cf.getOutputBus().getEvents().replay(1).autoConnect().map(Output::getText))
        .distinct()
        .doFinally(
            signalType ->
                cf.getOutputBus().getEvents().doOnNext(x -> x.getWebSocket().cancel()).subscribe());
  }
}

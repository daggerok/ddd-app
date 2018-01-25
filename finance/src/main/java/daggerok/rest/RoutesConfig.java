package daggerok.rest;

import daggerok.domain.CreditCard;
import daggerok.domain.CreditCardRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

import java.util.UUID;

import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.web.reactive.function.server.RequestPredicates.POST;
import static org.springframework.web.reactive.function.server.RouterFunctions.route;
import static org.springframework.web.reactive.function.server.ServerResponse.status;

@Configuration
public class RoutesConfig {

  @Bean RouterFunction<ServerResponse> routes(final CreditCardRepository repository) {

    return

        route(
            POST("/api/v1/create"),
            request -> status(CREATED).contentType(MediaType.APPLICATION_JSON_UTF8)
                                      .body(Mono.fromCallable(() -> {
                                        repository.save(new CreditCard(UUID.randomUUID()));
                                        return "done";
                                      }).subscribeOn(Schedulers.elastic()), String.class)
        );
  }
}

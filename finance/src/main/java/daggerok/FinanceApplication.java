package daggerok;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.kafka.KafkaProperties;
import org.springframework.kafka.core.KafkaOperations;

import javax.annotation.PostConstruct;

@Slf4j
@SpringBootApplication
@RequiredArgsConstructor
public class FinanceApplication {

  final KafkaProperties kafkaProperties;
  final KafkaOperations<Object, Object> kafka;

  @PostConstruct
  public void init() {
    log.info("p: {}", kafkaProperties);
    log.info("k: {}", kafka);
  }

  public static void main(String[] args) {
    SpringApplication.run(FinanceApplication.class, args);
  }
}

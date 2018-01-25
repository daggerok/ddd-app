package daggerok;

import daggerok.domain.events.DomainEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.kafka.KafkaProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;
import org.springframework.kafka.support.serializer.JsonSerializer;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@EnableKafka
@SpringBootApplication
@RequiredArgsConstructor
public class FinanceApplication {

  final KafkaProperties kafkaProperties;
//  final KafkaOperations<String, DomainEvent> kafka;

  @Bean KafkaTemplate<String, DomainEvent> kafkaTemplate() {
    return new KafkaTemplate<>(producerFactory());
  }

  @Bean ProducerFactory<String, DomainEvent> producerFactory() {
    return new DefaultKafkaProducerFactory<>(kafkaConfig());
  }

  @Bean
  public Map<String, Object> kafkaConfig() {
    final HashMap<String, Object> config = new HashMap<>();
    config.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, "127.0.0.1:9092");
    config.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
    config.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, JsonSerializer.class);
    return config;
  }

  public static void main(String[] args) {
    SpringApplication.run(FinanceApplication.class, args);
  }
}

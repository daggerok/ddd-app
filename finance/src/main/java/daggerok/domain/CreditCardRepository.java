package daggerok.domain;

import daggerok.domain.events.DomainEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
@Repository
@RequiredArgsConstructor
public class CreditCardRepository {

  final KafkaTemplate<String, DomainEvent> kafkaTemplate;
  final Map<UUID, List<DomainEvent>> eventStreams = new ConcurrentHashMap<>();

  public void save(final CreditCard creditCard) {
    log.info("cc: {}", creditCard);
    final List<DomainEvent> currentStream = eventStreams.getOrDefault(creditCard.getId(), new ArrayList<>());
    final List<DomainEvent> newEvents = creditCard.getDirtyEvents();
    currentStream.addAll(newEvents);
    eventStreams.put(creditCard.getId(), currentStream);
    newEvents.forEach(e -> kafkaTemplate.send("domain-events", e));
    creditCard.eventsFlushed();
  }

  public CreditCard findById(final UUID id) {
    return CreditCard.recreate(id, eventStreams.getOrDefault(id, new ArrayList<>()));
  }
}

package daggerok.domain;

import daggerok.domain.events.DomainEvent;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class CreditCardRepository {

  final Map<UUID, List<DomainEvent>> eventStreams = new ConcurrentHashMap<>();

  void save(final CreditCard creditCard) {
    final List<DomainEvent> currentStream = eventStreams.getOrDefault(creditCard.getId(), new ArrayList<>());
    final List<DomainEvent> newEvents = creditCard.getDirtyEvents();
    currentStream.addAll(newEvents);
    eventStreams.put(creditCard.getId(), currentStream);
    creditCard.eventsFlushed();
  }

  CreditCard findById(final UUID id) {
    return CreditCard.recreate(id, eventStreams.getOrDefault(id, new ArrayList<>()));
  }
}

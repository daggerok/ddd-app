package daggerok.domain;

import daggerok.domain.events.CardRepaidEvent;
import daggerok.domain.events.CardWithdrawnEvent;
import daggerok.domain.events.DomainEvent;
import daggerok.domain.events.LimitAssignedEvent;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.experimental.Accessors;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

import static io.vavr.collection.List.ofAll;
import static java.time.LocalDateTime.now;

@Data
@NoArgsConstructor
@Accessors(chain = true)
@RequiredArgsConstructor
public class CreditCard {

  @NonNull UUID id;
  private BigDecimal limit;
  private BigDecimal used = BigDecimal.ZERO;

  public List<DomainEvent> getDirtyEvents() {
    return Collections.unmodifiableList(dirtyEvents);
  }

  private final List<DomainEvent> dirtyEvents = new ArrayList<>(); // dirty // not flushed yet

  public void assignLimit(final BigDecimal amount) { // command
    if (limitAlreadyAssigned()) // invariant
      throw new IllegalStateException("limit already assigned."); // NACK
    limitAssigned(new LimitAssignedEvent(id, now(), amount)); // ACK
  }

  public BigDecimal availableLimit() {
    return limit.subtract(used);
  }

  public void withdraw(final BigDecimal amount) { // cmd
    if (notEnoughMoneyToWithdraw(amount)) // invariant
      throw new IllegalStateException("cannot withdraw over limit."); // NACK
    cardWithdrawn(new CardWithdrawnEvent(id, now(), amount)); // ACK
  }

  public void repay(final BigDecimal amount) { // cmd
    cardRepaid(new CardRepaidEvent(id, now(), amount)); //ACK
  }

  public void eventsFlushed() {
    dirtyEvents.clear();
  }

  /* event sourcing start: recreation state */
  public static CreditCard recreate(final UUID id, final List<DomainEvent> events) {
   return ofAll(events).foldLeft(new CreditCard(id), CreditCard::handle);
  }

  private CreditCard handle(final DomainEvent domainEvent) {
    return io.vavr.API.Match(domainEvent).of(
        io.vavr.API.Case(io.vavr.API.$(io.vavr.Predicates.instanceOf(LimitAssignedEvent.class)), this::limitAssigned),
        io.vavr.API.Case(io.vavr.API.$(io.vavr.Predicates.instanceOf(CardWithdrawnEvent.class)), this::cardWithdrawn),
        io.vavr.API.Case(io.vavr.API.$(io.vavr.Predicates.instanceOf(CardRepaidEvent.class)), this::cardRepaid)
    );
  }
  /* event sourcing end */

  /* events */

  private CreditCard limitAssigned(final LimitAssignedEvent limitAssigned) {
    this.limit = limitAssigned.getLimit();
    dirtyEvents.add(limitAssigned);
    return this;
  }

  private CreditCard cardWithdrawn(final CardWithdrawnEvent cardWithdrawn) {
    used = used.add(cardWithdrawn.getUsed());
    dirtyEvents.add(cardWithdrawn);
    return this;
  }

  private CreditCard cardRepaid(final CardRepaidEvent cardRepaid) {
    used = used.subtract(cardRepaid.getAmount());
    dirtyEvents.add(cardRepaid);
    return this;
  }

  /* helpers */

  private boolean limitAlreadyAssigned() {
    return null != limit;
  }

  private boolean notEnoughMoneyToWithdraw(final BigDecimal amount) {
    return availableLimit().compareTo(amount) < 0;
  }
}

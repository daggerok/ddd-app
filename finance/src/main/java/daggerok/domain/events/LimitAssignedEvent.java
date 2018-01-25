package daggerok.domain.events;

import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@ToString
@NoArgsConstructor
@RequiredArgsConstructor
public class LimitAssignedEvent implements DomainEvent {

  @NonNull UUID id;
  @NonNull LocalDateTime date;
  @NonNull BigDecimal limit;
  String type = "card.assigned";
}

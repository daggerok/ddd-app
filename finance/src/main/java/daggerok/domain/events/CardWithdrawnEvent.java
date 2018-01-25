package daggerok.domain.events;

import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@ToString
@NoArgsConstructor
@RequiredArgsConstructor
public class CardWithdrawnEvent implements DomainEvent {

  @NonNull UUID id;
  @NonNull LocalDateTime date;
  @NonNull BigDecimal used;
  String type = "card.withdrawn";
}

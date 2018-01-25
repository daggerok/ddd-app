package daggerok.domain.events;

import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@ToString
@NoArgsConstructor
@RequiredArgsConstructor
public class CardRepaidEvent implements DomainEvent {

  @NonNull UUID id;
  @NonNull LocalDateTime date;
  @NonNull BigDecimal amount;
  String type = "card.repaid";
}

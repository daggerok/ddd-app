package daggerok.domain.events;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

import java.time.LocalDateTime;
import java.util.UUID;

@JsonTypeInfo(
    use = JsonTypeInfo.Id.NAME,
    include = JsonTypeInfo.As.PROPERTY,
    property = "type"
)
@JsonSubTypes({
    @JsonSubTypes.Type(name = "card.assigned", value = LimitAssignedEvent.class),
    @JsonSubTypes.Type(name = "card.withdrawn", value = CardWithdrawnEvent.class),
    @JsonSubTypes.Type(name = "card.repaid", value = CardRepaidEvent.class),
})
public interface DomainEvent {

  UUID getId();

  String getType();

  LocalDateTime getDate();
}

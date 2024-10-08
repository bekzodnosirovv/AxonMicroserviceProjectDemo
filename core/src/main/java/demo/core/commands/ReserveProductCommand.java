package demo.core.commands;

import lombok.Builder;
import lombok.Data;
import org.axonframework.modelling.command.TargetAggregateIdentifier;

import java.math.BigDecimal;

@Data
@Builder
public class ReserveProductCommand {
    @TargetAggregateIdentifier
    private final String productId;
    private final Integer quantity;
    private final String orderId;
    private final String userId;
}

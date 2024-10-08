package demo.productservice.command;

import demo.core.commands.CancelProductReservationCommand;
import demo.core.commands.ReserveProductCommand;
import demo.core.events.ProductReservationCancelledEvent;
import demo.core.events.ProductReservedEvent;
import demo.productservice.core.events.ProductCreateEvent;
import org.axonframework.commandhandling.CommandHandler;
import org.axonframework.eventsourcing.EventSourcingHandler;
import org.axonframework.modelling.command.AggregateIdentifier;
import org.axonframework.modelling.command.AggregateLifecycle;
import org.axonframework.spring.stereotype.Aggregate;
import org.springframework.beans.BeanUtils;

import java.math.BigDecimal;

@Aggregate(snapshotTriggerDefinition = "productSnapshotTriggerDefinition")
public class ProductAggregate {

    @AggregateIdentifier
    private String productId;
    private String title;
    private BigDecimal price;
    private Integer quantity;

    public ProductAggregate() {
    }

    @CommandHandler
    public ProductAggregate(CreateProductCommand command) throws Exception {

        if (command.getPrice().compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Price cannot be less or equal than zero");
        }
        if (command.getTitle() == null || command.getTitle().isBlank()) {
            throw new IllegalArgumentException("Title cannot be null or empty");
        }

        ProductCreateEvent event = new ProductCreateEvent();
        BeanUtils.copyProperties(command, event);
        AggregateLifecycle.apply(event);
    }

    @EventSourcingHandler
    public void on(ProductCreateEvent event) {
        this.productId = event.getProductId();
        this.title = event.getTitle();
        this.price = event.getPrice();
        this.quantity = event.getQuantity();
    }

    @CommandHandler
    public void handle(ReserveProductCommand reserveProductCommand) throws Exception {
        if (quantity < reserveProductCommand.getQuantity()) {
            throw new IllegalArgumentException("Insufficient number of items in stock");
        }

        ProductReservedEvent productReservedEvent = ProductReservedEvent.builder()
                .orderId(reserveProductCommand.getOrderId())
                .userId(reserveProductCommand.getUserId())
                .quantity(reserveProductCommand.getQuantity())
                .productId(reserveProductCommand.getProductId())
                .build();

        AggregateLifecycle.apply(productReservedEvent);
    }

    @EventSourcingHandler
    public void on(ProductReservedEvent event) {
        this.quantity -= event.getQuantity();
    }

    @CommandHandler
    public void handle(CancelProductReservationCommand cancelProductReservationCommand) throws Exception {
        ProductReservationCancelledEvent productReservationCancelledEvent = ProductReservationCancelledEvent.builder()
                .productId(cancelProductReservationCommand.getProductId())
                .userId(cancelProductReservationCommand.getUserId())
                .orderId(cancelProductReservationCommand.getOrderId())
                .quantity(cancelProductReservationCommand.getQuantity())
                .reason(cancelProductReservationCommand.getReason())
                .build();

        AggregateLifecycle.apply(productReservationCancelledEvent);
    }

    @EventSourcingHandler
    public void on(ProductReservationCancelledEvent event) {
        this.quantity += event.getQuantity();
    }
}

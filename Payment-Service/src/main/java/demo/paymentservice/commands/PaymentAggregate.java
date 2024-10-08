package demo.paymentservice.commands;

import demo.core.commands.ProcessPaymentCommand;
import demo.core.events.PaymentProcessedEvent;
import org.axonframework.commandhandling.CommandHandler;
import org.axonframework.eventsourcing.EventSourcingHandler;
import org.axonframework.modelling.command.AggregateIdentifier;
import org.axonframework.modelling.command.AggregateLifecycle;
import org.axonframework.spring.stereotype.Aggregate;

@Aggregate(snapshotTriggerDefinition = "paymentSnapshotTriggerDefinition")
public class PaymentAggregate {
    @AggregateIdentifier
    private String paymentId;
    private String orderId;

    public PaymentAggregate() {
    }

    @CommandHandler
    public PaymentAggregate(ProcessPaymentCommand command) {

        if (command.getPaymentId() == null) {
            throw new IllegalArgumentException("Payment id is required");
        }
        if (command.getOrderId() == null) {
            throw new IllegalArgumentException("Order id is required");
        }
        if (command.getPaymentDetails() == null) {
            throw new IllegalArgumentException("Payment details is required");
        }

        PaymentProcessedEvent event = PaymentProcessedEvent.builder()
                .paymentId(command.getPaymentId())
                .orderId(command.getOrderId())
                .build();

        AggregateLifecycle.apply(event);

    }

    @EventSourcingHandler
    public void on(PaymentProcessedEvent event) {
        this.paymentId = event.getPaymentId();
        this.orderId = event.getOrderId();
    }
}

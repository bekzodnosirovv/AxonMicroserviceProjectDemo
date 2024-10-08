package demo.paymentservice.events;

import demo.core.events.PaymentProcessedEvent;
import demo.paymentservice.core.PaymentEntity;
import demo.paymentservice.core.PaymentRepository;
import org.axonframework.eventhandling.EventHandler;
import org.springframework.stereotype.Component;

@Component
public class PaymentEventHandler {

    private final PaymentRepository paymentRepository;

    public PaymentEventHandler(PaymentRepository paymentRepository) {
        this.paymentRepository = paymentRepository;
    }

    @EventHandler
    public void on(PaymentProcessedEvent event) {

        PaymentEntity paymentEntity = new PaymentEntity(event.getPaymentId(), event.getOrderId());
        paymentRepository.save(paymentEntity);
    }
}

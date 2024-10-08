package demo.userservice;

import demo.core.models.PaymentDetails;
import demo.core.models.User;
import demo.core.querys.FetchUserPaymentDetailsQuery;
import org.axonframework.queryhandling.QueryHandler;
import org.springframework.stereotype.Component;

@Component
public class UserEventsHandler {

    @QueryHandler
    public User findUser(FetchUserPaymentDetailsQuery query) {

        PaymentDetails paymentDetails = PaymentDetails.builder()
                .cartNumber("card123")
                .name("Bekzod Nosirov")
                .cvv("12345")
                .validUntilMonth(12)
                .validUntilYear(2030)
                .build();

        User user = User.builder()
                .userId(query.getUserId())
                .firstName("Bekzod")
                .lastName("Nosirov")
                .paymentDetails(paymentDetails)
                .build();

        return user;

    }
}

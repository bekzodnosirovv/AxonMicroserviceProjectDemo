package demo.productservice.query;

import demo.core.events.ProductReservationCancelledEvent;
import demo.core.events.ProductReservedEvent;
import demo.productservice.core.data.ProductEntity;
import demo.productservice.core.data.ProductEntityRepository;
import demo.productservice.core.events.ProductCreateEvent;
import org.axonframework.config.ProcessingGroup;
import org.axonframework.eventhandling.EventHandler;
import org.axonframework.eventhandling.ResetHandler;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.ExceptionHandler;

@Component
@ProcessingGroup("product-group")
public class ProductEventHandler {

    private final ProductEntityRepository productEntityRepository;

    public ProductEventHandler(ProductEntityRepository productEntityRepository) {
        this.productEntityRepository = productEntityRepository;
    }
    @ExceptionHandler(Exception.class)
    public void handle(Exception exception) throws Exception {
        throw exception;
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public void handle(IllegalArgumentException exception) {
        // Log error message
    }
    @EventHandler
    public void on(ProductCreateEvent event) {

        ProductEntity productEntity = new ProductEntity();
        BeanUtils.copyProperties(event, productEntity);
        try {
            productEntityRepository.save(productEntity);
        } catch (IllegalArgumentException ex) {
            ex.printStackTrace();
        }
    }

    @EventHandler
    public void on(ProductReservedEvent event) throws Exception {
        ProductEntity productEntity = productEntityRepository.findByProductId(event.getProductId());
        productEntity.setQuantity(productEntity.getQuantity() - event.getQuantity());
        productEntityRepository.save(productEntity);

    }

    @EventHandler
    public void on(ProductReservationCancelledEvent event) throws Exception {
        ProductEntity productEntity = productEntityRepository.findByProductId(event.getProductId());
        productEntity.setQuantity(productEntity.getQuantity() + event.getQuantity());
        productEntityRepository.save(productEntity);
    }

    @ResetHandler
    public void reset() {
        productEntityRepository.deleteAll();
    }

}

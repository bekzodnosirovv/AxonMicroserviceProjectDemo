package demo.productservice.command;

import demo.productservice.core.data.ProductLookupEntity;
import demo.productservice.core.data.ProductLookupRepository;
import demo.productservice.core.events.ProductCreateEvent;
import org.axonframework.config.ProcessingGroup;
import org.axonframework.eventhandling.EventHandler;
import org.axonframework.eventhandling.ResetHandler;
import org.springframework.stereotype.Component;

@Component
@ProcessingGroup("product-group")
public class ProductLookupEventHandler {

    private final ProductLookupRepository productLookupRepository;

    public ProductLookupEventHandler(ProductLookupRepository productLookupRepository){
        this.productLookupRepository = productLookupRepository;
    }

    @EventHandler
    public void on(ProductCreateEvent event) {

        ProductLookupEntity productLookupEntity = new ProductLookupEntity(event.getProductId(), event.getTitle());
        productLookupRepository.save(productLookupEntity);
    }

    @ResetHandler
    public void reset() {
        productLookupRepository.deleteAll();
    }
}

package demo.productservice.query;

import demo.productservice.core.data.ProductEntity;
import demo.productservice.core.data.ProductEntityRepository;
import demo.productservice.query.rest.ProductRestModel;
import org.axonframework.queryhandling.QueryHandler;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class ProductQueryHandler {

    private final ProductEntityRepository productEntityRepository;

    public ProductQueryHandler(ProductEntityRepository productEntityRepository) {
        this.productEntityRepository = productEntityRepository;
    }

    @QueryHandler
    public List<ProductRestModel> findProducts(FindProductQuery findProductQuery) {
        List<ProductEntity> productEntities = productEntityRepository.findAll();
        List<ProductRestModel> productRestModels = new ArrayList<>();

        for (ProductEntity productEntity : productEntities) {
            ProductRestModel productRestModel = new ProductRestModel();
            BeanUtils.copyProperties(productEntity, productRestModel);
            productRestModels.add(productRestModel);
        }
        return productRestModels;
    }
}

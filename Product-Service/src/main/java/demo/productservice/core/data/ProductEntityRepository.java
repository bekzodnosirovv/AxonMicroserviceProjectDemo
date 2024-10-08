package demo.productservice.core.data;

import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductEntityRepository extends JpaRepository<ProductEntity, String> {

    ProductEntity findByProductId(String productId);
}

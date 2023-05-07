package basavets.service;

import basavets.dto.ProductResponse;
import basavets.repositories.ProductRepository;
import org.springframework.stereotype.Service;

@Service
public class ProductService {

    private final ProductRepository productRepository;

    public ProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    public ProductResponse getAllProducts(){
        ProductResponse productResponse = new ProductResponse();
        productResponse.setProductList(productRepository.findAll());
        return productResponse;
    }
}

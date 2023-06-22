package basavets.service;

import basavets.dto.*;

public interface ProductService {

    ProductResponse getAllProducts();

    ProductOrderResponse orderProduct(ProductOrderRequest productOrderRequest, int productId);

    ProductSetResponse setProduct(ProductSetRequest productSetRequest);

    ChangeProductStatusResponse changeStatus(int purchaseId);
}

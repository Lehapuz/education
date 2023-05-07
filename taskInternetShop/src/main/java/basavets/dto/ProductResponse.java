package basavets.dto;

import basavets.beans.Product;

public class ProductResponse {
    private Iterable<Product> productList;

    public Iterable<Product> getProductList() {
        return productList;
    }

    public void setProductList(Iterable<Product> productList) {
        this.productList = productList;
    }
}

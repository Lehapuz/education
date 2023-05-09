package basavets.service;

import basavets.beans.Product;
import basavets.beans.Purchase;
import basavets.beans.Status;
import basavets.beans.User;
import basavets.dto.*;
import basavets.repositories.ProductRepository;
import basavets.repositories.PurchaseRepository;
import basavets.repositories.UserRepository;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class ProductService {

    private final ProductRepository productRepository;
    private final UserRepository userRepository;
    private final PurchaseRepository purchaseRepository;

    public ProductService(ProductRepository productRepository, UserRepository userRepository, PurchaseRepository purchaseRepository) {
        this.productRepository = productRepository;
        this.userRepository = userRepository;
        this.purchaseRepository = purchaseRepository;
    }

    public ProductResponse getAllProducts() {
        ProductResponse productResponse = new ProductResponse();
        productResponse.setProductList(productRepository.findAll());
        return productResponse;
    }

    public ProductOrderResponse orderProduct(ProductOrderRequest productOrderRequest, int productId) {

        ProductOrderResponse productOrderResponse = new ProductOrderResponse();

        try {
            org.springframework.security.core.userdetails.User user = (org.springframework.security.core.userdetails.User)
                    SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            Optional<User> currentUser = userRepository.findUserByEmail(user.getUsername());
            Optional<Product> product = productRepository.findById(productId);
            Status status = Status.IN_PROGRESS;

            if (product.isEmpty() || productOrderRequest.getAmount() > product.get().getAmount()) {
                productOrderResponse.setResult(false);
            } else {
                Purchase purchase = new Purchase();
                purchase.setUser(currentUser.get());
                purchase.setProduct(product.get());
                purchase.setValue(productOrderRequest.getAmount());
                purchase.setStatus(status);
                productOrderResponse.setResult(true);
                purchaseRepository.save(purchase);
                product.get().setAmount(product.get().getAmount() - productOrderRequest.getAmount());
                productRepository.save(product.get());
            }
        } catch (Exception e) {
            productOrderResponse.setResult(false);
        }
        return productOrderResponse;
    }

    public ProductSetResponse setProduct(ProductSetRequest productSetRequest) {

        ProductSetResponse productSetResponse = new ProductSetResponse();

        try {
            org.springframework.security.core.userdetails.User user = (org.springframework.security.core.userdetails.User)
                    SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            Optional<User> currentUser = userRepository.findUserByEmail(user.getUsername());

            if (currentUser.get().getIsModerator() == 1) {
                Product product = new Product();
                product.setName(productSetRequest.getName());
                product.setAmount(productSetRequest.getAmount());
                productSetResponse.setResult(true);
                productRepository.save(product);
            } else {
                productSetResponse.setResult(false);
            }
        } catch (Exception e) {
            productSetResponse.setResult(false);
        }
        return productSetResponse;
    }

    public ChangeProductStatusResponse changeStatus(int purchaseId) {

        ChangeProductStatusResponse changeProductStatusResponse = new ChangeProductStatusResponse();

        try {
            org.springframework.security.core.userdetails.User user = (org.springframework.security.core.userdetails.User)
                    SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            Optional<User> currentUser = userRepository.findUserByEmail(user.getUsername());

            if (currentUser.get().getIsModerator() == 1) {
                Optional<Purchase> purchase = purchaseRepository.findById(purchaseId);
                purchase.get().setStatus(Status.DELIVERED);
                changeProductStatusResponse.setResult(true);
                purchaseRepository.save(purchase.get());
            } else {
                changeProductStatusResponse.setResult(false);
            }
        } catch (Exception e) {
            changeProductStatusResponse.setResult(false);
        }
        return changeProductStatusResponse;
    }
}

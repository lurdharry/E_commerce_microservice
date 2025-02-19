package com.lurdharry.ecommerce.product;


import com.lurdharry.ecommerce.exception.ProductPurchaseException;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;
    private final ProductMapper mapper;

    public Integer createProduct(@Valid ProductRequest request) {
        var product = mapper.toProduct(request);
        return productRepository.save(product).getId();
    }

    public List<ProductPurchaseResponse> purchaseProducts(@Valid List<ProductPurchaseRequest> request) {
        // Extract the product IDs from the purchase requests
        var productIds = request.stream()
                .map(ProductPurchaseRequest::productId)
                .toList();

        // Retrieve all products from the repository that match the provided IDs,
        // ordering them by ID for consistency
        var storedProducts = productRepository.findAllByIdInOrderById(productIds);

        // If the number of requested product IDs doesn't match the number of stored products,
        // it means one or more products do not exist in the repository.
        if (productIds.size() != storedProducts.size()){
            throw new ProductPurchaseException("One or more products does not exist");
        }

        // Sort the purchase requests by product ID to align with the order of stored products
        var storedRequest = request
                .stream()
                .sorted(Comparator.comparing(ProductPurchaseRequest::productId))
                .toList();

        // Initialize a list to store the responses for each purchased product
        var purchasedProducts = new ArrayList<ProductPurchaseResponse>();

        // Iterate over each stored product along with its corresponding purchase request
        for (int i = 0; i < storedProducts.size(); i++) {
            var product = storedProducts.get(i);
            var productRequest = storedRequest.get(i);

            // Check if the available quantity is sufficient for the requested purchase quantity
            if(product.getAvailableQuantity() < productRequest.quantity()){
                throw new ProductPurchaseException(
                        "Insufficient stock quantity for product with ID:: " + productRequest.productId()
                );
            }

            // Calculate the new available quantity after the purchase
            var newAvailableQuantity = product.getAvailableQuantity() - productRequest.quantity();
            product.setAvailableQuantity(newAvailableQuantity);

            // Save the updated product to the repository
            productRepository.save(product);

            // Map the product and purchase quantity to a response object and add it to the list
            purchasedProducts.add(mapper.toProductPurchaseResponse(product, productRequest.quantity()));
        }

        // Return the list of responses representing the purchased products
        return purchasedProducts;
    }


    public ProductResponse findById(Integer productId) {
        return productRepository.findById(productId)
                .map(mapper::toProductResponse)
                .orElseThrow(()-> new EntityNotFoundException("Product not found with ID:: " + productId));
    }

    public List<ProductResponse> findAll() {
        return productRepository.findAll()
                .stream()
                .map(mapper::toProductResponse)
                .collect(Collectors.toList());
    }
}

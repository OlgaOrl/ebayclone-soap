package com.ebayclone.soap.service.impl;

import com.ebayclone.soap.service.ProductService;
import com.ebayclone.soap.types.Product;
import com.ebayclone.soap.types.ServiceFault;

import javax.jws.WebService;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

@WebService(
    serviceName = "ProductService",
    portName = "ProductServicePort",
    targetNamespace = "http://ebay.clone.soap/service",
    endpointInterface = "com.ebayclone.soap.service.ProductService"
)
public class ProductServiceImpl implements ProductService {
    
    // In-memory storage for demo purposes (instance-scoped for test isolation)
    private final ConcurrentHashMap<Long, Product> products = new ConcurrentHashMap<>();
    private long nextId = 1;
    
    @Override
    public Product createProduct(Product product) throws ServiceFault {
        // Validate input
        if (product.getTitle() == null || product.getTitle().isEmpty()) {
            throw new ServiceFault("Product title cannot be empty", "VALIDATION_ERROR");
        }
        
        // Set product ID and dates
        product.setId(nextId++);
        product.setCreatedDate(new Date());
        product.setStatus("ACTIVE");
        
        // Store product
        products.put(product.getId(), product);
        
        return product;
    }
    
    @Override
    public Product getProduct(Long productId) throws ServiceFault {
        Product product = products.get(productId);
        if (product == null) {
            throw new ServiceFault("Product not found: " + productId, "NOT_FOUND");
        }
        return product;
    }
    
    @Override
    public List<Product> searchProducts(String keyword, String category, Double maxPrice) throws ServiceFault {
        return products.values().stream()
            .filter(p -> keyword == null || p.getTitle().toLowerCase().contains(keyword.toLowerCase()) 
                || p.getDescription().toLowerCase().contains(keyword.toLowerCase()))
            .filter(p -> category == null || p.getCategory().equals(category))
            .filter(p -> maxPrice == null || p.getCurrentPrice().compareTo(BigDecimal.valueOf(maxPrice)) <= 0)
            .collect(Collectors.toList());
    }
    
    @Override
    public Product updateProduct(Product product) throws ServiceFault {
        if (!products.containsKey(product.getId())) {
            throw new ServiceFault("Product not found: " + product.getId(), "NOT_FOUND");
        }
        
        products.put(product.getId(), product);
        return product;
    }
    
    @Override
    public boolean deleteProduct(Long productId) throws ServiceFault {
        if (!products.containsKey(productId)) {
            throw new ServiceFault("Product not found: " + productId, "NOT_FOUND");
        }
        
        products.remove(productId);
        return true;
    }
}
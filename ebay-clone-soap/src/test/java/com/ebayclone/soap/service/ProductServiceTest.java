package com.ebayclone.soap.service;

import com.ebayclone.soap.service.impl.ProductServiceImpl;
import com.ebayclone.soap.types.Product;
import com.ebayclone.soap.types.ServiceFault;
import org.junit.Before;
import org.junit.Test;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import static org.junit.Assert.*;

public class ProductServiceTest {
    
    private ProductService productService;
    
    @Before
    public void setup() {
        productService = new ProductServiceImpl();
    }
    
    @Test
    public void testCreateProductSuccess() throws ServiceFault {
        // Prepare test data
        Product product = new Product();
        product.setTitle("Test Product");
        product.setDescription("This is a test product");
        product.setCategory("Electronics");
        product.setStartingPrice(new BigDecimal("99.99"));
        product.setCurrentPrice(new BigDecimal("99.99"));
        product.setSellerId(1L);
        product.setEndDate(new Date(System.currentTimeMillis() + 86400000)); // Tomorrow
        
        // Call the service
        Product createdProduct = productService.createProduct(product);
        
        // Verify the result
        assertNotNull(createdProduct);
        assertNotNull(createdProduct.getId());
        assertEquals("Test Product", createdProduct.getTitle());
        assertEquals("ACTIVE", createdProduct.getStatus());
    }
    
    @Test
    public void testSearchProducts() throws ServiceFault {
        // Create some test products
        Product product1 = new Product();
        product1.setTitle("iPhone 12");
        product1.setDescription("Latest Apple smartphone");
        product1.setCategory("Electronics");
        product1.setStartingPrice(new BigDecimal("699.99"));
        product1.setCurrentPrice(new BigDecimal("699.99"));
        product1.setSellerId(1L);
        product1.setEndDate(new Date(System.currentTimeMillis() + 86400000));
        productService.createProduct(product1);
        
        Product product2 = new Product();
        product2.setTitle("Samsung Galaxy S21");
        product2.setDescription("Latest Android smartphone");
        product2.setCategory("Electronics");
        product2.setStartingPrice(new BigDecimal("799.99"));
        product2.setCurrentPrice(new BigDecimal("799.99"));
        product2.setSellerId(1L);
        product2.setEndDate(new Date(System.currentTimeMillis() + 86400000));
        productService.createProduct(product2);
        
        // Search by keyword
        List<Product> results = productService.searchProducts("iPhone", null, null);
        assertEquals(1, results.size());
        assertEquals("iPhone 12", results.get(0).getTitle());
        
        // Search by category
        results = productService.searchProducts(null, "Electronics", null);
        assertEquals(2, results.size());
        
        // Search by price
        results = productService.searchProducts(null, null, 750.0);
        assertEquals(1, results.size());
        assertEquals("iPhone 12", results.get(0).getTitle());
    }
}
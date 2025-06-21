package com.ebayclone.soap.service.impl;

import com.ebayclone.soap.service.AuctionService;
import com.ebayclone.soap.service.ProductService;
import com.ebayclone.soap.types.*;

import javax.jws.WebService;
import java.math.BigDecimal;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

@WebService(
    serviceName = "AuctionService",
    portName = "AuctionServicePort",
    targetNamespace = "http://ebay.clone.soap/service",
    endpointInterface = "com.ebayclone.soap.service.AuctionService"
)
public class AuctionServiceImpl implements AuctionService {
    
    // In-memory storage for demo purposes
    private static final ConcurrentHashMap<Long, List<Bid>> productBids = new ConcurrentHashMap<>();
    private static final ConcurrentHashMap<Long, Bid> bids = new ConcurrentHashMap<>();
    private static long nextBidId = 1;
    
    private ProductService productService;
    
    public AuctionServiceImpl() {
        // In a real implementation, this would be injected
        productService = new ProductServiceImpl();
    }
    
    @Override
    public Bid placeBid(Bid bid) throws ServiceFault {
        // Validate product exists
        Product product = productService.getProduct(bid.getProductId());
        
        // Validate bid amount
        if (bid.getBidAmount().compareTo(product.getCurrentPrice()) <= 0) {
            throw new ServiceFault("Bid amount must be higher than current price", "VALIDATION_ERROR");
        }
        
        // Set bid ID and time
        bid.setId(nextBidId++);
        bid.setBidTime(new Date());
        bid.setStatus("ACTIVE");
        
        // Store bid
        bids.put(bid.getId(), bid);
        
        // Add to product bids
        productBids.computeIfAbsent(bid.getProductId(), k -> new ArrayList<>()).add(bid);
        
        // Update product current price
        product.setCurrentPrice(bid.getBidAmount());
        productService.updateProduct(product);
        
        return bid;
    }
    
    @Override
    public List<Bid> getBidsForProduct(Long productId) throws ServiceFault {
        // Validate product exists
        productService.getProduct(productId);
        
        return productBids.getOrDefault(productId, Collections.emptyList());
    }
    
    @Override
    public Bid getHighestBid(Long productId) throws ServiceFault {
        List<Bid> productBidList = getBidsForProduct(productId);
        
        return productBidList.stream()
            .max(Comparator.comparing(Bid::getBidAmount))
            .orElseThrow(() -> new ServiceFault("No bids found for product: " + productId, "NOT_FOUND"));
    }
    
    @Override
    public Order closeAuction(Long productId) throws ServiceFault {
        // Validate product exists and is active
        Product product = productService.getProduct(productId);
        if (!"ACTIVE".equals(product.getStatus())) {
            throw new ServiceFault("Auction is not active", "INVALID_STATE");
        }
        
        // Get highest bid
        Bid highestBid = getHighestBid(productId);
        
        // Update product status
        product.setStatus("SOLD");
        productService.updateProduct(product);
        
        // Create order
        Order order = new Order();
        order.setId(System.currentTimeMillis()); // Simple ID generation
        order.setProductId(productId);
        order.setBuyerId(highestBid.getBidderId());
        order.setSellerId(product.getSellerId());
        order.setTotalAmount(highestBid.getBidAmount());
        order.setOrderDate(new Date());
        order.setStatus("PENDING");
        
        return order;
    }
}
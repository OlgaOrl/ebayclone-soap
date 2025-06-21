package com.ebayclone.soap.client;

import com.ebayclone.soap.service.AuctionService;
import com.ebayclone.soap.service.ProductService;
import com.ebayclone.soap.types.Bid;
import com.ebayclone.soap.types.Order;
import com.ebayclone.soap.types.Product;
import org.apache.cxf.jaxws.JaxWsProxyFactoryBean;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

public class AuctionServiceClient {
    
    public static void main(String[] args) {
        // Create client proxies
        ProductService productClient = createProductClient();
        AuctionService auctionClient = createAuctionClient();
        
        try {
            // Create a test product for auction
            Product product = new Product();
            product.setTitle("Vintage Watch");
            product.setDescription("Rare collectible watch in excellent condition");
            product.setCategory("Collectibles");
            product.setStartingPrice(new BigDecimal("100.00"));
            product.setCurrentPrice(new BigDecimal("100.00"));
            product.setSellerId(1L);
            product.setEndDate(new Date(System.currentTimeMillis() + 86400000)); // Tomorrow
            
            Product createdProduct = productClient.createProduct(product);
            System.out.println("Created auction product: " + createdProduct.getTitle() + " (ID: " + createdProduct.getId() + ")");
            
            // Place some bids
            Bid bid1 = new Bid();
            bid1.setProductId(createdProduct.getId());
            bid1.setBidderId(2L);
            bid1.setBidAmount(new BigDecimal("110.00"));
            
            Bid placedBid1 = auctionClient.placeBid(bid1);
            System.out.println("Placed bid: $" + placedBid1.getBidAmount() + " by user " + placedBid1.getBidderId());
            
            Bid bid2 = new Bid();
            bid2.setProductId(createdProduct.getId());
            bid2.setBidderId(3L);
            bid2.setBidAmount(new BigDecimal("120.00"));
            
            Bid placedBid2 = auctionClient.placeBid(bid2);
            System.out.println("Placed bid: $" + placedBid2.getBidAmount() + " by user " + placedBid2.getBidderId());
            
            // Get all bids for the product
            List<Bid> bids = auctionClient.getBidsForProduct(createdProduct.getId());
            System.out.println("Total bids: " + bids.size());
            
            // Get highest bid
            Bid highestBid = auctionClient.getHighestBid(createdProduct.getId());
            System.out.println("Highest bid: $" + highestBid.getBidAmount() + " by user " + highestBid.getBidderId());
            
            // Close the auction
            Order order = auctionClient.closeAuction(createdProduct.getId());
            System.out.println("Auction closed. Order created:");
            System.out.println("Order ID: " + order.getId());
            System.out.println("Buyer ID: " + order.getBuyerId());
            System.out.println("Total amount: $" + order.getTotalAmount());
            System.out.println("Status: " + order.getStatus());
            
        } catch (Exception e) {
            System.err.println("Error in auction process: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    private static ProductService createProductClient() {
        JaxWsProxyFactoryBean factory = new JaxWsProxyFactoryBean();
        factory.setServiceClass(ProductService.class);
        factory.setAddress("http://localhost:8080/soap/product");
        return (ProductService) factory.create();
    }
    
    private static AuctionService createAuctionClient() {
        JaxWsProxyFactoryBean factory = new JaxWsProxyFactoryBean();
        factory.setServiceClass(AuctionService.class);
        factory.setAddress("http://localhost:8080/soap/auction");
        return (AuctionService) factory.create();
    }
}
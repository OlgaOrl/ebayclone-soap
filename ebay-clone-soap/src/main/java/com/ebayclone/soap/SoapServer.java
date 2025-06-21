package com.ebayclone.soap;

import com.ebayclone.soap.config.HandlerConfig;
import com.ebayclone.soap.service.impl.AuctionServiceImpl;
import com.ebayclone.soap.service.impl.OrderServiceImpl;
import com.ebayclone.soap.service.impl.ProductServiceImpl;
import com.ebayclone.soap.service.impl.UserServiceImpl;
import org.apache.cxf.jaxws.JaxWsServerFactoryBean;

public class SoapServer {
    
    public static void main(String[] args) {
        // Create and start the User service
        JaxWsServerFactoryBean userFactory = new JaxWsServerFactoryBean();
        userFactory.setServiceClass(UserServiceImpl.class);
        userFactory.setAddress("http://localhost:8080/soap/user");
        userFactory.setHandlers(HandlerConfig.getHandlerChain());
        userFactory.create();
        
        // Create and start the Product service
        JaxWsServerFactoryBean productFactory = new JaxWsServerFactoryBean();
        productFactory.setServiceClass(ProductServiceImpl.class);
        productFactory.setAddress("http://localhost:8080/soap/product");
        productFactory.setHandlers(HandlerConfig.getHandlerChain());
        productFactory.create();
        
        // Create and start the Auction service
        JaxWsServerFactoryBean auctionFactory = new JaxWsServerFactoryBean();
        auctionFactory.setServiceClass(AuctionServiceImpl.class);
        auctionFactory.setAddress("http://localhost:8080/soap/auction");
        auctionFactory.setHandlers(HandlerConfig.getHandlerChain());
        auctionFactory.create();
        
        // Create and start the Order service
        JaxWsServerFactoryBean orderFactory = new JaxWsServerFactoryBean();
        orderFactory.setServiceClass(OrderServiceImpl.class);
        orderFactory.setAddress("http://localhost:8080/soap/order");
        orderFactory.setHandlers(HandlerConfig.getHandlerChain());
        orderFactory.create();
        
        System.out.println("SOAP services started. WSDL available at:");
        System.out.println("- http://localhost:8080/soap/user?wsdl");
        System.out.println("- http://localhost:8080/soap/product?wsdl");
        System.out.println("- http://localhost:8080/soap/auction?wsdl");
        System.out.println("- http://localhost:8080/soap/order?wsdl");
        
        // Keep the server running
        try {
            Thread.sleep(Long.MAX_VALUE);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}


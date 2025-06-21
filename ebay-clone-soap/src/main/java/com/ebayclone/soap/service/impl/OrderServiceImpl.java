package com.ebayclone.soap.service.impl;

import com.ebayclone.soap.service.OrderService;
import com.ebayclone.soap.types.Order;
import com.ebayclone.soap.types.ServiceFault;

import javax.jws.WebService;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

@WebService(
    serviceName = "OrderService",
    portName = "OrderServicePort",
    targetNamespace = "http://ebay.clone.soap/service",
    endpointInterface = "com.ebayclone.soap.service.OrderService"
)
public class OrderServiceImpl implements OrderService {
    
    // In-memory storage for demo purposes
    private static final ConcurrentHashMap<Long, Order> orders = new ConcurrentHashMap<>();
    
    @Override
    public Order createOrder(Order order) throws ServiceFault {
        // Validate input
        if (order.getProductId() == null || order.getBuyerId() == null || order.getSellerId() == null) {
            throw new ServiceFault("Product ID, buyer ID, and seller ID are required", "VALIDATION_ERROR");
        }
        
        // Store order
        orders.put(order.getId(), order);
        
        return order;
    }
    
    @Override
    public Order getOrder(Long orderId) throws ServiceFault {
        Order order = orders.get(orderId);
        if (order == null) {
            throw new ServiceFault("Order not found: " + orderId, "NOT_FOUND");
        }
        return order;
    }
    
    @Override
    public Order updateOrderStatus(Long orderId, String status) throws ServiceFault {
        Order order = getOrder(orderId);
        
        // Validate status transition
        validateStatusTransition(order.getStatus(), status);
        
        // Update status
        order.setStatus(status);
        orders.put(orderId, order);
        
        return order;
    }
    
    @Override
    public List<Order> getBuyerOrders(Long buyerId) throws ServiceFault {
        return orders.values().stream()
            .filter(o -> o.getBuyerId().equals(buyerId))
            .collect(Collectors.toList());
    }
    
    @Override
    public List<Order> getSellerOrders(Long sellerId) throws ServiceFault {
        return orders.values().stream()
            .filter(o -> o.getSellerId().equals(sellerId))
            .collect(Collectors.toList());
    }
    
    private void validateStatusTransition(String currentStatus, String newStatus) throws ServiceFault {
        // Implement status transition validation logic
        // For example, DELIVERED cannot go back to PENDING
        
        // Simple validation for demo
        List<String> validStatuses = List.of("PENDING", "PAID", "SHIPPED", "DELIVERED", "CANCELLED");
        if (!validStatuses.contains(newStatus)) {
            throw new ServiceFault("Invalid status: " + newStatus, "VALIDATION_ERROR");
        }
    }
}
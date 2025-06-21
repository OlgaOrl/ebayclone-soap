package com.ebayclone.soap.types;

import javax.xml.bind.annotation.*;
import java.math.BigDecimal;
import java.util.Date;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "Order", propOrder = {
    "id", "productId", "buyerId", "sellerId", "totalAmount", 
    "orderDate", "status", "shippingAddress", "paymentMethod"
})
public class Order {
    
    @XmlElement(required = true)
    private Long id;
    
    @XmlElement(required = true)
    private Long productId;
    
    @XmlElement(required = true)
    private Long buyerId;
    
    @XmlElement(required = true)
    private Long sellerId;
    
    @XmlElement(required = true)
    private BigDecimal totalAmount;
    
    @XmlElement(required = true)
    @XmlSchemaType(name = "dateTime")
    private Date orderDate;
    
    @XmlElement(required = true)
    private String status; // PENDING, PAID, SHIPPED, DELIVERED, CANCELLED
    
    @XmlElement(required = true)
    private String shippingAddress;
    
    @XmlElement(required = true)
    private String paymentMethod;
    
    // Getters and setters
    // ...
}
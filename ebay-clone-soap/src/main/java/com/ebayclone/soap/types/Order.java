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
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Long getProductId() { return productId; }
    public void setProductId(Long productId) { this.productId = productId; }

    public Long getBuyerId() { return buyerId; }
    public void setBuyerId(Long buyerId) { this.buyerId = buyerId; }

    public Long getSellerId() { return sellerId; }
    public void setSellerId(Long sellerId) { this.sellerId = sellerId; }

    public BigDecimal getTotalAmount() { return totalAmount; }
    public void setTotalAmount(BigDecimal totalAmount) { this.totalAmount = totalAmount; }

    public Date getOrderDate() { return orderDate; }
    public void setOrderDate(Date orderDate) { this.orderDate = orderDate; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public String getShippingAddress() { return shippingAddress; }
    public void setShippingAddress(String shippingAddress) { this.shippingAddress = shippingAddress; }

    public String getPaymentMethod() { return paymentMethod; }
    public void setPaymentMethod(String paymentMethod) { this.paymentMethod = paymentMethod; }
}

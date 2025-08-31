package com.ebayclone.soap.types;

import javax.xml.bind.annotation.*;
import java.math.BigDecimal;
import java.util.Date;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "Product", propOrder = {
    "id", "title", "description", "category", "startingPrice", 
    "currentPrice", "sellerId", "createdDate", "endDate", "status"
})
public class Product {
    
    @XmlElement(required = true)
    private Long id;
    
    @XmlElement(required = true)
    private String title;
    
    @XmlElement(required = true)
    private String description;
    
    @XmlElement(required = true)
    private String category;
    
    @XmlElement(required = true)
    private BigDecimal startingPrice;
    
    @XmlElement(required = true)
    private BigDecimal currentPrice;
    
    @XmlElement(required = true)
    private Long sellerId;
    
    @XmlElement(required = true)
    @XmlSchemaType(name = "dateTime")
    private Date createdDate;
    
    @XmlElement(required = true)
    @XmlSchemaType(name = "dateTime")
    private Date endDate;
    
    @XmlElement(required = true)
    private String status; // ACTIVE, SOLD, EXPIRED
    
    // Getters and setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public String getCategory() { return category; }
    public void setCategory(String category) { this.category = category; }

    public BigDecimal getStartingPrice() { return startingPrice; }
    public void setStartingPrice(BigDecimal startingPrice) { this.startingPrice = startingPrice; }

    public BigDecimal getCurrentPrice() { return currentPrice; }
    public void setCurrentPrice(BigDecimal currentPrice) { this.currentPrice = currentPrice; }

    public Long getSellerId() { return sellerId; }
    public void setSellerId(Long sellerId) { this.sellerId = sellerId; }

    public Date getCreatedDate() { return createdDate; }
    public void setCreatedDate(Date createdDate) { this.createdDate = createdDate; }

    public Date getEndDate() { return endDate; }
    public void setEndDate(Date endDate) { this.endDate = endDate; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
}

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
    // ...
}
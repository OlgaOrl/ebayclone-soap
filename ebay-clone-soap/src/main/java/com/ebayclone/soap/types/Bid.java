package com.ebayclone.soap.types;

import javax.xml.bind.annotation.*;
import java.math.BigDecimal;
import java.util.Date;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "Bid", propOrder = {
    "id", "productId", "bidderId", "bidAmount", "bidTime", "status"
})
public class Bid {
    
    @XmlElement(required = true)
    private Long id;
    
    @XmlElement(required = true)
    private Long productId;
    
    @XmlElement(required = true)
    private Long bidderId;
    
    @XmlElement(required = true)
    private BigDecimal bidAmount;
    
    @XmlElement(required = true)
    @XmlSchemaType(name = "dateTime")
    private Date bidTime;
    
    @XmlElement(required = true)
    private String status; // ACTIVE, WINNING, OUTBID, REJECTED
    
    // Getters and setters
    // ...
}
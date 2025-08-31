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
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Long getProductId() { return productId; }
    public void setProductId(Long productId) { this.productId = productId; }

    public Long getBidderId() { return bidderId; }
    public void setBidderId(Long bidderId) { this.bidderId = bidderId; }

    public BigDecimal getBidAmount() { return bidAmount; }
    public void setBidAmount(BigDecimal bidAmount) { this.bidAmount = bidAmount; }

    public Date getBidTime() { return bidTime; }
    public void setBidTime(Date bidTime) { this.bidTime = bidTime; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
}

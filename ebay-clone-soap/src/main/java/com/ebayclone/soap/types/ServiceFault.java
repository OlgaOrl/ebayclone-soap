package com.ebayclone.soap.types;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;
import javax.xml.ws.WebFault;

@WebFault(name = "ServiceFault", targetNamespace = "http://ebay.clone.soap/types")
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "ServiceFault", propOrder = {
    "message",
    "errorCode"
})
public class ServiceFault extends Exception {
    
    @XmlElement(required = true)
    private String message;
    
    @XmlElement(required = true)
    private String errorCode;
    
    public ServiceFault() {
        super();
    }
    
    public ServiceFault(String message, String errorCode) {
        super(message);
        this.message = message;
        this.errorCode = errorCode;
    }
    
    // Getters and setters
    public String getErrorCode() { return errorCode; }
    public void setErrorCode(String errorCode) { this.errorCode = errorCode; }
    
    @Override
    public String getMessage() { return message; }
    public void setMessage(String message) { this.message = message; }
}
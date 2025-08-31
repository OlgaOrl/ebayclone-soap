package com.ebayclone.soap.types;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "ServiceFault", namespace = "http://ebay.clone.soap/types", propOrder = {
        "message",
        "errorCode"
})
@XmlRootElement(name = "ServiceFault", namespace = "http://ebay.clone.soap/types")
public class FaultInfo {

    @XmlElement(required = true)
    private String message;

    @XmlElement(required = true)
    private String errorCode;

    public FaultInfo() {}

    public FaultInfo(String message, String errorCode) {
        this.message = message;
        this.errorCode = errorCode;
    }

    public String getMessage() { return message; }
    public void setMessage(String message) { this.message = message; }

    public String getErrorCode() { return errorCode; }
    public void setErrorCode(String errorCode) { this.errorCode = errorCode; }
}


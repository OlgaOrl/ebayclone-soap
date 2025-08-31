package com.ebayclone.soap.types;

import javax.xml.ws.WebFault;

@WebFault(name = "ServiceFault", targetNamespace = "http://ebay.clone.soap/types", faultBean = "com.ebayclone.soap.types.FaultInfo")
public class ServiceFault extends Exception {
    
    private final FaultInfo faultInfo;
    
    public ServiceFault(String message, String errorCode) {
        super(message);
        this.faultInfo = new FaultInfo(message, errorCode);
    }

    public ServiceFault(String message, FaultInfo faultInfo, Throwable cause) {
        super(message, cause);
        this.faultInfo = faultInfo;
    }

    public FaultInfo getFaultInfo() {
        return faultInfo;
    }
}
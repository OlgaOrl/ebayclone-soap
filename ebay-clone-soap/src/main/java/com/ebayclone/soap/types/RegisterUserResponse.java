package com.ebayclone.soap.types;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "RegisterUserResponse", propOrder = {
    "user",
    "status",
    "message"
})
public class RegisterUserResponse {
    
    @XmlElement(required = true)
    private User user;
    
    @XmlElement(required = true)
    private String status;
    
    @XmlElement(required = false)
    private String message;
    
    // Getters and setters
    public User getUser() { return user; }
    public void setUser(User user) { this.user = user; }
    
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    
    public String getMessage() { return message; }
    public void setMessage(String message) { this.message = message; }
}
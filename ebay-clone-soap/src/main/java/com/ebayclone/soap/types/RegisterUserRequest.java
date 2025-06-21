package com.ebayclone.soap.types;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "RegisterUserRequest", propOrder = {
    "username",
    "email",
    "password"
})
public class RegisterUserRequest {
    
    @XmlElement(required = true)
    private String username;
    
    @XmlElement(required = true)
    private String email;
    
    @XmlElement(required = true)
    private String password;
    
    // Getters and setters
    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }
    
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    
    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }
}
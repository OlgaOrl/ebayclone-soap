package com.ebayclone.soap.client;

import com.ebayclone.soap.service.UserService;
import com.ebayclone.soap.types.RegisterUserRequest;
import com.ebayclone.soap.types.RegisterUserResponse;
import org.apache.cxf.jaxws.JaxWsProxyFactoryBean;

public class UserServiceClient {
    
    public static void main(String[] args) {
        // Create a client proxy
        JaxWsProxyFactoryBean factory = new JaxWsProxyFactoryBean();
        factory.setServiceClass(UserService.class);
        factory.setAddress("http://localhost:8080/soap/user");
        UserService client = (UserService) factory.create();
        
        try {
            // Create a request
            RegisterUserRequest request = new RegisterUserRequest();
            request.setUsername("testuser");
            request.setEmail("test@example.com");
            request.setPassword("password123");
            
            // Call the service
            RegisterUserResponse response = client.registerUser(request);
            
            // Print the result
            System.out.println("User registered successfully:");
            System.out.println("ID: " + response.getUser().getId());
            System.out.println("Username: " + response.getUser().getUsername());
            System.out.println("Email: " + response.getUser().getEmail());
            
        } catch (Exception e) {
            System.err.println("Error calling service: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
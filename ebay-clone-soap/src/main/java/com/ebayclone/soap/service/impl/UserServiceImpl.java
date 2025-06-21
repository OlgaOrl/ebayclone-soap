package com.ebayclone.soap.service.impl;

import com.ebayclone.soap.service.UserService;
import com.ebayclone.soap.types.RegisterUserRequest;
import com.ebayclone.soap.types.RegisterUserResponse;
import com.ebayclone.soap.types.ServiceFault;
import com.ebayclone.soap.types.User;

import javax.jws.WebService;
import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Level;
import java.util.logging.Logger;

@WebService(
    serviceName = "UserService",
    portName = "UserServicePort",
    targetNamespace = "http://ebay.clone.soap/service",
    endpointInterface = "com.ebayclone.soap.service.UserService"
)
public class UserServiceImpl implements UserService {
    
    private static final Logger LOGGER = Logger.getLogger(UserServiceImpl.class.getName());
    private static final ConcurrentHashMap<String, User> users = new ConcurrentHashMap<>();
    private static long nextId = 1;
    
    @Override
    public RegisterUserResponse registerUser(RegisterUserRequest request) throws ServiceFault {
        try {
            // Validate input
            if (request.getUsername() == null || request.getUsername().isEmpty()) {
                LOGGER.warning("Registration attempt with empty username");
                throw new ServiceFault("Username cannot be empty", "VALIDATION_ERROR");
            }
            
            if (request.getEmail() == null || request.getEmail().isEmpty()) {
                LOGGER.warning("Registration attempt with empty email");
                throw new ServiceFault("Email cannot be empty", "VALIDATION_ERROR");
            }
            
            if (request.getPassword() == null || request.getPassword().length() < 6) {
                LOGGER.warning("Registration attempt with invalid password");
                throw new ServiceFault("Password must be at least 6 characters", "VALIDATION_ERROR");
            }
            
            // Check if username already exists
            if (users.containsKey(request.getUsername())) {
                LOGGER.warning("Registration attempt with existing username: " + request.getUsername());
                throw new ServiceFault("Username already exists", "DUPLICATE_USERNAME");
            }
            
            // Create user
            User user = new User();
            user.setId(nextId++);
            user.setUsername(request.getUsername());
            user.setEmail(request.getEmail());
            
            // Store user (in a real implementation, you would hash the password)
            users.put(request.getUsername(), user);
            
            LOGGER.info("User registered successfully: " + user.getUsername());
            
            // Create response
            RegisterUserResponse response = new RegisterUserResponse();
            response.setUser(user);
            response.setStatus("SUCCESS");
            response.setMessage("User registered successfully");
            
            return response;
            
        } catch (ServiceFault sf) {
            // Re-throw service faults
            throw sf;
        } catch (Exception e) {
            // Log unexpected errors and convert to ServiceFault
            LOGGER.log(Level.SEVERE, "Error registering user", e);
            throw new ServiceFault("Internal server error: " + e.getMessage(), "INTERNAL_ERROR");
        }
    }
    
    // Implement other methods
}

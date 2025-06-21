package com.ebayclone.soap.service;

import com.ebayclone.soap.service.impl.UserServiceImpl;
import com.ebayclone.soap.types.RegisterUserRequest;
import com.ebayclone.soap.types.RegisterUserResponse;
import com.ebayclone.soap.types.ServiceFault;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class UserServiceTest {
    
    private UserService userService;
    
    @Before
    public void setup() {
        userService = new UserServiceImpl();
    }
    
    @Test
    public void testRegisterUserSuccess() throws ServiceFault {
        // Prepare test data
        RegisterUserRequest request = new RegisterUserRequest();
        request.setUsername("testuser");
        request.setEmail("test@example.com");
        request.setPassword("password123");
        
        // Call the service
        RegisterUserResponse response = userService.registerUser(request);
        
        // Verify the result
        assertNotNull(response);
        assertNotNull(response.getUser());
        assertEquals("testuser", response.getUser().getUsername());
        assertEquals("test@example.com", response.getUser().getEmail());
    }
    
    @Test(expected = ServiceFault.class)
    public void testRegisterUserWithEmptyUsername() throws ServiceFault {
        // Prepare test data with invalid input
        RegisterUserRequest request = new RegisterUserRequest();
        request.setUsername("");
        request.setEmail("test@example.com");
        request.setPassword("password123");
        
        // This should throw ServiceFault
        userService.registerUser(request);
    }
}

package com.ebayclone.soap.service;

import com.ebayclone.soap.types.RegisterUserRequest;
import com.ebayclone.soap.types.RegisterUserResponse;
import com.ebayclone.soap.types.ServiceFault;


import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebResult;
import javax.jws.WebService;
import javax.jws.soap.SOAPBinding;


@WebService(targetNamespace = "http://ebay.clone.soap/service", name = "UserService")
@SOAPBinding(parameterStyle = SOAPBinding.ParameterStyle.BARE)
public interface UserService {
    @WebMethod(operationName = "registerUser")
    @WebResult(name = "response", targetNamespace = "http://ebay.clone.soap/types")
    RegisterUserResponse registerUser(
        @WebParam(name = "request", targetNamespace = "http://ebay.clone.soap/types")
        RegisterUserRequest request) throws ServiceFault;
    
    // Additional methods for login, profile management, etc.
}



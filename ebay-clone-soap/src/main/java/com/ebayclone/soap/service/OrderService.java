package com.ebayclone.soap.service;

import com.ebayclone.soap.types.*;
import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebResult;
import javax.jws.WebService;
import java.util.List;

@WebService(targetNamespace = "http://ebay.clone.soap/service", name = "OrderService")
public interface OrderService {
    
    @WebMethod(operationName = "createOrder")
    @WebResult(name = "response", targetNamespace = "http://ebay.clone.soap/types")
    Order createOrder(
            @WebParam(name = "order", targetNamespace = "http://ebay.clone.soap/types")
            Order order) throws ServiceFault;
    
    @WebMethod(operationName = "getOrder")
    @WebResult(name = "response", targetNamespace = "http://ebay.clone.soap/types")
    Order getOrder(
            @WebParam(name = "orderId", targetNamespace = "http://ebay.clone.soap/types")
            Long orderId) throws ServiceFault;
    
    @WebMethod(operationName = "updateOrderStatus")
    @WebResult(name = "response", targetNamespace = "http://ebay.clone.soap/types")
    Order updateOrderStatus(
            @WebParam(name = "orderId", targetNamespace = "http://ebay.clone.soap/types")
            Long orderId,
            @WebParam(name = "status", targetNamespace = "http://ebay.clone.soap/types")
            String status) throws ServiceFault;
    
    @WebMethod(operationName = "getBuyerOrders")
    @WebResult(name = "response", targetNamespace = "http://ebay.clone.soap/types")
    List<Order> getBuyerOrders(
            @WebParam(name = "buyerId", targetNamespace = "http://ebay.clone.soap/types")
            Long buyerId) throws ServiceFault;
    
    @WebMethod(operationName = "getSellerOrders")
    @WebResult(name = "response", targetNamespace = "http://ebay.clone.soap/types")
    List<Order> getSellerOrders(
            @WebParam(name = "sellerId", targetNamespace = "http://ebay.clone.soap/types")
            Long sellerId) throws ServiceFault;
}
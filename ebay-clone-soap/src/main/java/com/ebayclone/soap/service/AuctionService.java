package com.ebayclone.soap.service;

import com.ebayclone.soap.types.*;
import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebResult;
import javax.jws.WebService;
import java.util.List;

@WebService(targetNamespace = "http://ebay.clone.soap/service", name = "AuctionService")
public interface AuctionService {
    
    @WebMethod(operationName = "placeBid")
    @WebResult(name = "response", targetNamespace = "http://ebay.clone.soap/types")
    Bid placeBid(
            @WebParam(name = "bid", targetNamespace = "http://ebay.clone.soap/types")
            Bid bid) throws ServiceFault;
    
    @WebMethod(operationName = "getBidsForProduct")
    @WebResult(name = "response", targetNamespace = "http://ebay.clone.soap/types")
    List<Bid> getBidsForProduct(
            @WebParam(name = "productId", targetNamespace = "http://ebay.clone.soap/types")
            Long productId) throws ServiceFault;
    
    @WebMethod(operationName = "getHighestBid")
    @WebResult(name = "response", targetNamespace = "http://ebay.clone.soap/types")
    Bid getHighestBid(
            @WebParam(name = "productId", targetNamespace = "http://ebay.clone.soap/types")
            Long productId) throws ServiceFault;
    
    @WebMethod(operationName = "closeAuction")
    @WebResult(name = "response", targetNamespace = "http://ebay.clone.soap/types")
    Order closeAuction(
            @WebParam(name = "productId", targetNamespace = "http://ebay.clone.soap/types")
            Long productId) throws ServiceFault;
}
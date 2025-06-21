package com.ebayclone.soap.service;

import com.ebayclone.soap.types.*;
import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebResult;
import javax.jws.WebService;
import javax.xml.ws.RequestWrapper;
import javax.xml.ws.ResponseWrapper;
import java.util.List;

@WebService(targetNamespace = "http://ebay.clone.soap/service", name = "ProductService")
public interface ProductService {
    
    @WebMethod(operationName = "createProduct")
    @WebResult(name = "response", targetNamespace = "http://ebay.clone.soap/types")
    Product createProduct(
            @WebParam(name = "product", targetNamespace = "http://ebay.clone.soap/types")
            Product product) throws ServiceFault;
    
    @WebMethod(operationName = "getProduct")
    @WebResult(name = "response", targetNamespace = "http://ebay.clone.soap/types")
    Product getProduct(
            @WebParam(name = "productId", targetNamespace = "http://ebay.clone.soap/types")
            Long productId) throws ServiceFault;
    
    @WebMethod(operationName = "searchProducts")
    @WebResult(name = "response", targetNamespace = "http://ebay.clone.soap/types")
    List<Product> searchProducts(
            @WebParam(name = "keyword", targetNamespace = "http://ebay.clone.soap/types")
            String keyword,
            @WebParam(name = "category", targetNamespace = "http://ebay.clone.soap/types")
            String category,
            @WebParam(name = "maxPrice", targetNamespace = "http://ebay.clone.soap/types")
            Double maxPrice) throws ServiceFault;
    
    @WebMethod(operationName = "updateProduct")
    @WebResult(name = "response", targetNamespace = "http://ebay.clone.soap/types")
    Product updateProduct(
            @WebParam(name = "product", targetNamespace = "http://ebay.clone.soap/types")
            Product product) throws ServiceFault;
    
    @WebMethod(operationName = "deleteProduct")
    @WebResult(name = "response", targetNamespace = "http://ebay.clone.soap/types")
    boolean deleteProduct(
            @WebParam(name = "productId", targetNamespace = "http://ebay.clone.soap/types")
            Long productId) throws ServiceFault;
}
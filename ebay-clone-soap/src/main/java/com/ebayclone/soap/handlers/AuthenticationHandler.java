package com.ebayclone.soap.handlers;

import javax.xml.namespace.QName;
import javax.xml.soap.SOAPElement;
import javax.xml.soap.SOAPEnvelope;
import javax.xml.soap.SOAPHeader;
import javax.xml.ws.handler.MessageContext;
import javax.xml.ws.handler.soap.SOAPHandler;
import javax.xml.ws.handler.soap.SOAPMessageContext;
import java.util.Collections;
import java.util.Set;

public class AuthenticationHandler implements SOAPHandler<SOAPMessageContext> {
    
    private static final String AUTH_NS = "http://ebay.clone.soap/auth";
    
    @Override
    public boolean handleMessage(SOAPMessageContext context) {
        Boolean outbound = (Boolean) context.get(MessageContext.MESSAGE_OUTBOUND_PROPERTY);
        
        if (!outbound) { // Inbound message
            try {
                SOAPEnvelope envelope = context.getMessage().getSOAPPart().getEnvelope();
                SOAPHeader header = envelope.getHeader();
                
                if (header == null) {
                    throw new RuntimeException("Authentication header is missing");
                }
                
                // Get authentication token
                SOAPElement authElement = (SOAPElement) header.getChildElements(
                        new QName(AUTH_NS, "AuthToken")).next();
                
                if (authElement == null) {
                    throw new RuntimeException("Authentication token is missing");
                }
                
                String token = authElement.getValue();
                
                // Validate token (in a real implementation, this would check against a database or auth service)
                if (token == null || token.isEmpty() || !isValidToken(token)) {
                    throw new RuntimeException("Invalid authentication token");
                }
                
                // Store user info in context for service implementations to use
                context.put("USER_ID", getUserIdFromToken(token));
                context.setScope("USER_ID", MessageContext.Scope.APPLICATION);
                
            } catch (Exception e) {
                throw new RuntimeException("Authentication failed: " + e.getMessage(), e);
            }
        }
        
        return true;
    }
    
    @Override
    public boolean handleFault(SOAPMessageContext context) {
        return true;
    }
    
    @Override
    public void close(MessageContext context) {
        // No resources to clean up
    }
    
    @Override
    public Set<QName> getHeaders() {
        return Collections.singleton(new QName(AUTH_NS, "AuthToken"));
    }
    
    private boolean isValidToken(String token) {
        // In a real implementation, this would validate the token against a database or auth service
        return token != null && !token.isEmpty();
    }
    
    private Long getUserIdFromToken(String token) {
        // In a real implementation, this would extract the user ID from the token
        // For demo purposes, just return a dummy ID
        return 1L;
    }
}

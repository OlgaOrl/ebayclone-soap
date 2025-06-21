package com.ebayclone.soap.config;

import com.ebayclone.soap.handlers.AuthenticationHandler;
import javax.xml.ws.handler.Handler;
import java.util.ArrayList;
import java.util.List;

public class HandlerConfig {
    
    public static List<Handler> getHandlerChain() {
        List<Handler> handlerChain = new ArrayList<>();
        handlerChain.add(new AuthenticationHandler());
        // Add more handlers here if needed
        return handlerChain;
    }
}
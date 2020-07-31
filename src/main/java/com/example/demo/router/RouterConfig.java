package com.example.demo.router;

import com.example.demo.handlers.CricketerHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerResponse;

import static org.springframework.web.reactive.function.server.RequestPredicates.*;

@Configuration
class RouterConfig {
    private String CRICKETER_END_POINT = "/api/cricketer/";
    @Bean
    public RouterFunction<ServerResponse> cricketerRoutes(CricketerHandler cricketerHandler) {
        return RouterFunctions.
                route(GET(CRICKETER_END_POINT).and(accept(MediaType.APPLICATION_JSON))
                        , cricketerHandler::getAllCricketers)
                .andRoute(GET(CRICKETER_END_POINT + "id").and(accept(MediaType.APPLICATION_JSON))
                        , cricketerHandler::getCricketer)
                .andRoute(POST(CRICKETER_END_POINT).and(accept(MediaType.APPLICATION_JSON))
                        , cricketerHandler::addCricketer)
                .andRoute(PUT(CRICKETER_END_POINT + "id").and(accept(MediaType.APPLICATION_JSON))
                        , cricketerHandler::updateCricketer)
                .andRoute(DELETE(CRICKETER_END_POINT + "id").and(accept(MediaType.APPLICATION_JSON))
                        , cricketerHandler::deleteCricketer)
                ;
    }
    
    @Bean
    public RouterFunction<ServerResponse> errorRoute(CricketerHandler cricketerHandler) {
        return RouterFunctions.
                route(GET("/runtimeexception").and(accept(MediaType.APPLICATION_JSON))
                        , cricketerHandler::exceptionExample);

    }
}
package com.avishaneu.testtasks.simplechat.configuration;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.web.socket.config.annotation.AbstractWebSocketMessageBrokerConfigurer;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;

/**
 * Created by tkalnitskaya on 20.07.2017.
 */

@Configuration
@EnableScheduling
@EnableWebSocketMessageBroker
public class WebSocketConfiguration extends AbstractWebSocketMessageBrokerConfigurer {

    private static final Logger log = LoggerFactory.getLogger(WebSocketConfiguration.class);

    @Value("${websocket.endpoint}")
    private String WEBSOCKET_ENDPOINT;

    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        log.info("Configuring stomp endpoint: " + WEBSOCKET_ENDPOINT);
        registry.addEndpoint(WEBSOCKET_ENDPOINT)
                .setAllowedOrigins("*")
                .withSockJS();
    }
    @Override
    public void configureMessageBroker(MessageBrokerRegistry config) {
        log.info("Configuring message broker...");
        config.enableSimpleBroker("/queue/", "/topic");
        config.setApplicationDestinationPrefixes("/app");
        config.setUserDestinationPrefix("/user");
    }
}

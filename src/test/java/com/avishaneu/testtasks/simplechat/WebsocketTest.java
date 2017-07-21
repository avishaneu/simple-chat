package com.avishaneu.testtasks.simplechat;

import com.avishaneu.testtasks.simplechat.configuration.WebSecurityConfig;
import com.avishaneu.testtasks.simplechat.configuration.WebSocketConfiguration;
import com.avishaneu.testtasks.simplechat.configuration.WebSocketSecurityConfig;
import com.avishaneu.testtasks.simplechat.model.Message;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.*;
import org.springframework.http.client.*;
import org.springframework.http.converter.FormHttpMessageConverter;
import org.springframework.messaging.simp.stomp.StompFrameHandler;
import org.springframework.messaging.simp.stomp.StompHeaders;
import org.springframework.messaging.simp.stomp.StompSession;
import org.springframework.messaging.simp.stomp.StompSessionHandlerAdapter;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.util.StringUtils;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.socket.WebSocketHttpHeaders;
import org.springframework.web.socket.client.standard.StandardWebSocketClient;
import org.springframework.web.socket.messaging.WebSocketStompClient;
import org.springframework.web.socket.sockjs.client.SockJsClient;
import org.springframework.web.socket.sockjs.client.Transport;
import org.springframework.web.socket.sockjs.client.WebSocketTransport;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.*;
import java.util.concurrent.BlockingDeque;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingDeque;

import static java.util.concurrent.TimeUnit.SECONDS;

/**
 * Created by tkalnitskaya on 21.07.2017.
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes={WebSocketConfiguration.class, WebSecurityConfig.class, WebSocketSecurityConfig.class},
        webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@EnableAutoConfiguration
@AutoConfigureMockMvc
public class WebsocketTest {

    @Value("${server.port}")
    private int port;

    @Value("${websocket.endpoint}")
    private String endpoint;

    private String topic = "/topic/chat";

    private static final String message = "Hello! I am a test message";
    private static final String username = "test";

    private WebSocketStompClient wsClient;
    private BlockingQueue<String> messageQueue;

    @Before
    public void init() {
        messageQueue = new LinkedBlockingDeque<>();
        List<Transport> transportList = new ArrayList<>();
        transportList.add(new WebSocketTransport(new StandardWebSocketClient()));
        wsClient = new WebSocketStompClient(new SockJsClient(transportList));
    }

    @Test
    public void websocketTest() throws Exception {
        String websocketURL = "ws://localhost:" + port + endpoint;

        WebSocketHttpHeaders headers = loginAndGetHeaders(username);

        StompSession session = wsClient.connect(websocketURL, headers, new StompSessionHandlerAdapter(){})
                .get(1, SECONDS);
        session.subscribe(topic, new CustomFrameHandler());

        session.send(topic, message.getBytes());

        Assert.assertEquals(message, messageQueue.poll(1, SECONDS));
    }

    class CustomFrameHandler implements StompFrameHandler {
        @Override
        public Type getPayloadType(StompHeaders stompHeaders) {
            return byte[].class;
        }

        @Override
        public void handleFrame(StompHeaders stompHeaders, Object o) {
            messageQueue.offer(new String((byte[]) o));
        }
    }

    private WebSocketHttpHeaders loginAndGetHeaders(String user) {
        String loginURL = "http://localhost:" + port + "/join";
        WebSocketHttpHeaders wsHeaders = new WebSocketHttpHeaders();

        new RestTemplate().execute(loginURL, HttpMethod.POST,
                request -> {
                    MultiValueMap<String, String> map = new LinkedMultiValueMap<>();
                    map.add("username", user);
                    new FormHttpMessageConverter().write(map, MediaType.APPLICATION_FORM_URLENCODED, request);
                },
                response -> {
                    wsHeaders.add("Cookie", response.getHeaders().getFirst("Set-Cookie"));
                    return null;
                });
        return wsHeaders;
    }
}

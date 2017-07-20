package com.avishaneu.testtasks.simplechat;

import com.avishaneu.testtasks.simplechat.model.Message;
import org.slf4j.LoggerFactory;
import org.springframework.messaging.handler.annotation.MessageExceptionHandler;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.annotation.SendToUser;
import org.springframework.stereotype.Controller;


/**
 * Created by tkalnitskaya on 20.07.2017.
 */
@Controller
public class ChatController {
    private static final org.slf4j.Logger log = LoggerFactory.getLogger(ChatController.class);

    @MessageMapping("/sendMessage")
    @SendTo("/topic/chat")
    public Message sendMessage(Message message) {
        log.debug("Received message: " + message.getContent() + " from user: " + message.getSender());
        return message;
    }

    @MessageExceptionHandler
    @SendToUser(destinations = "/queue/error", broadcast = false)
    public String handleException(Exception exception) {
        log.error("Handling exception: ", exception);
        return exception.getMessage();
    }
}
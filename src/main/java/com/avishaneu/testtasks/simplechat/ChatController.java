package com.avishaneu.testtasks.simplechat;

import com.avishaneu.testtasks.simplechat.model.Message;
import com.avishaneu.testtasks.simplechat.model.UserMessage;
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
    public UserMessage sendMessage(UserMessage userMessage) {
        log.debug("Received message: " + userMessage.getContent() + " from user: " + userMessage.getSender());
        return userMessage;
    }

    @MessageMapping("/leave")
    @SendTo("/topic/system")
    public Message leave(String user) {
        return new Message("User " + user + " left the chat");
    }

    @MessageMapping("/join")
    @SendTo("/topic/system")
    public Message join(String user) {
        return new Message("User " + user + " joined the chat");
    }

    @MessageExceptionHandler
    @SendToUser(destinations = "/queue/error", broadcast = false)
    public String handleException(Exception exception) {
        log.error("Handling exception: ", exception);
        return exception.getMessage();
    }
}
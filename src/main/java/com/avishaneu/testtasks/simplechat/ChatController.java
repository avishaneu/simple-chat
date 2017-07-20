package com.avishaneu.testtasks.simplechat;

import com.avishaneu.testtasks.simplechat.model.Message;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;

/**
 * Created by tkalnitskaya on 20.07.2017.
 */
@Controller
public class ChatController {
    @MessageMapping("/sendMessage")
    @SendTo("/chatroom")
    public Message sendMessage(Message message){
        return message;
    }
}
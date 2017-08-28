package com.avishaneu.testtasks.simplechat.service;

import com.avishaneu.testtasks.simplechat.model.UserMessage;
import com.avishaneu.testtasks.simplechat.util.CircularQueue;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by avishaneu on 8/28/17.
 */
@Service
public class MessagePersistenceService {

    @Value("${messages.cache.size}")
    private int maxSize;

    private List<UserMessage> messages;

    public synchronized void addMessage(UserMessage message){
        if (messages == null) messages = new CircularQueue<>(maxSize);
        messages.add(message);
    }

    public synchronized List<UserMessage> getMessages(){
        if (messages == null) return Collections.emptyList();
        List<UserMessage> copy = new ArrayList<>(messages.size());
        for (UserMessage message : messages){
            copy.add(message.copy());
        }
        return copy;
    }
}

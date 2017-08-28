package com.avishaneu.testtasks.simplechat.service;

import org.springframework.stereotype.Service;

import java.util.HashSet;

/**
 * Created by avishaneu on 8/28/17.
 */
@Service
public class AuthenticationService {
    private HashSet<String> userCache = new HashSet<>();

    public synchronized String authenticateUser(String userName) {
        boolean updated = false;
        String postfix = "";
        int counter = 0;
        StringBuilder newUsername = new StringBuilder(userName);
        while (!updated) {
            newUsername.append(postfix);
            updated = userCache.add(newUsername.toString());
            postfix = String.valueOf(++counter);
        }
        return newUsername.toString();
    }

    public synchronized void userLogOut(String userName) {
        userCache.remove(userName);
    }
}

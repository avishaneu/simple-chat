package com.avishaneu.testtasks.simplechat.model;

/**
 * Created by tkalnitskaya on 20.07.2017.
 */
public class UserMessage extends Message {

    private String sender;

    public UserMessage() {
    }

    public UserMessage(String content, String sender) {
        super(content);
        this.sender = sender;
    }

   public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    public UserMessage copy(){
        return new UserMessage(getContent(), sender);
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;

        UserMessage that = (UserMessage) o;

        return sender != null ? sender.equals(that.sender) : that.sender == null;
    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + (sender != null ? sender.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "UserMessage{" +
                "sender='" + sender + '\'' +
                '}';
    }
}

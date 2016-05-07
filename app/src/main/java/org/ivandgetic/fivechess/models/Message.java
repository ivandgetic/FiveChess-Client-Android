package org.ivandgetic.fivechess.models;

/**
 * Created by ivandgetic on 2016/3/18 0018.
 */
public class Message {
    String name;
    String message;

    public Message(String name, String message) {
        this.name = name;
        this.message = message;
    }

    public String getName() {
        return name;
    }

    public String getMessage() {
        return message;
    }
}

package com.example.demoproject.helper;

public class Message {
    private String content;
    private String type;

    public Message() {
    }

    public Message(String message, String type) {
        this.content = message;
        this.type = type;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    @Override
    public String toString() {
        return "Message{" +
                "message='" + content + '\'' +
                ", type='" + type + '\'' +
                '}';
    }
}

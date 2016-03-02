package com.theIronYard;

/**
 * Created by branden on 3/2/16 at 10:50.
 */
public class Message {


    int id;
    String author, text;


    public Message() {
    }

    public Message(int id, String author, String text) {
        this.id = id;
        this.author = author;
        this.text = text;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }
}
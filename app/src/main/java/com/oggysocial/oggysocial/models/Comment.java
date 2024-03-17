package com.oggysocial.oggysocial.models;


import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Date;

public class Comment implements Serializable {
    String content;
    User author;
    String image;
    Date date;

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public User getAuthor() {
        return author;
    }

    public void setAuthor(User author) {
        this.author = author;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

}

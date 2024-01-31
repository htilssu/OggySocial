package com.aesocial.aesocial.models;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
public class Post {
    String content;
    String author;
    String date;
    List<String> likes;
    List<Comment> comments;
}

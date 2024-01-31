package com.aesocial.aesocial.models;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class Comment {
    String content;
    String author;
    String image;
    String date;
}

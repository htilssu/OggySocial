package com.oggysocial.oggysocial.models;

import androidx.annotation.NonNull;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;


public class User implements Serializable {

    String id;
    String firstName;
    String lastName;
    String email;
    String bio;
    String avatar;
    String birthday;
    List<String> posts = new ArrayList<>();
    List<String> friends = new ArrayList<>();

    public User(String firstName, String lastName, String email, String birthday) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.birthday = birthday;
    }

    public User() {
        posts = new ArrayList<>();
        friends = new ArrayList<>();
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public List<String> getPosts() {
        return posts;
    }

    public void setPosts(List<String> posts) {
        this.posts = posts;
    }

    /**
     * Thêm một bài viết vào danh sách bài viết của người dùng
     *
     * @param postId id của bài viết
     */
    public void addPost(String postId) {
        posts.add(postId);
    }

    public List<String> getFriends() {
        return friends;
    }

    public void setFriends(List<String> friends) {
        this.friends = friends;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getBio() {
        return bio;
    }

    public void setBio(String bio) {
        this.bio = bio;
    }

    public String getBirthday() {
        return birthday;
    }

    public void setBirthday(String birthday) {
        this.birthday = birthday;
    }


    public void removePost(String postId) {
        posts.remove(postId);
    }

    public String getFullName() {
        return lastName + " " + firstName;
    }

    @NonNull
    @Override
    public String toString() {
        return id;
    }
}

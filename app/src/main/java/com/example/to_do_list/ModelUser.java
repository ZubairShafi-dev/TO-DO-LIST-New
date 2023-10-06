package com.example.to_do_list;

public class ModelUser {
    public ModelUser() {
    }

    public String getConfirmPassword() {
        return confirmPassword;
    }

    public void setConfirmPassword(String confirmPassword) {
        this.confirmPassword = confirmPassword;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public ModelUser(String confirmPassword, String email, String id, String password, String userName) {
        this.confirmPassword = confirmPassword;
        this.email = email;
        this.id = id;
        this.password = password;
        this.userName = userName;
    }

    String confirmPassword;
     String email;
    String id;
     String password;
     String userName;

    // Default (no-argument) constructor

}

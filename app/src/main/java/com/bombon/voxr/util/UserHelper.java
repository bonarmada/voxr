package com.bombon.voxr.util;

/**
 * Created by Vaughn on 8/17/17.
 */

public class UserHelper {
    private Integer id;
    private String username;
    private String password;
    private boolean isLoggedIn;

    public Integer getId() {return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public boolean isLoggedIn() {
        return isLoggedIn;
    }

    public void setLoggedIn(boolean loggedIn) {
        isLoggedIn = loggedIn;
    }
}

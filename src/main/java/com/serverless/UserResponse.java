package com.serverless;


import com.serverless.data.User;

/**
 * @author Ragesh Shunmugam
 */
public class UserResponse {
    private final String message;
    private final User user;

    public UserResponse(String message, User user) {
        this.message = message;
        this.user = user;
    }

    public String getMessage() {
        return this.message;
    }

    public User getUser() {
        return this.user;
    }
}

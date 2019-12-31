package com.serverless;

/**
 * @author Ragesh Shunmugam
 */
public class SessionResponse {
    private final String message;
    private final String userName;

    public SessionResponse(String message, String userName) {
        this.message = message;
        this.userName = userName;
    }

    public String getMessage() {
        return this.message;
    }

    public String getUserName() {
        return this.userName;
    }
}

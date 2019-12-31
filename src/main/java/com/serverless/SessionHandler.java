package com.serverless;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.serverless.data.Session;
import com.serverless.data.UserAdapter;
import com.serverless.util.SessionHelper;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Collections;
import java.util.Map;

public class SessionHandler implements RequestHandler<Map<String, Object>, ApiGatewayResponse> {

    private static final Logger LOG = LogManager.getLogger(UserHandler.class);

    private String userName;

    private Session session;

    public ApiGatewayResponse handleRequest(Map<String, Object> input, Context context) {
        LOG.info("UserHandler received: " + input);
        try{
            Map<String, String> pathParameters = (Map<String, String>) input.get("pathParameters");
            userName =  pathParameters.get("username");
            SessionHelper sessionHelper = new SessionHelper();
            //update sessionid as null for logout
            UserAdapter.getInstance().doLogout(userName);
        } catch(Exception e){
            LOG.error(e,e);
            SessionResponse responseBody = new SessionResponse("Failure putting transaction", userName);
            return ApiGatewayResponse.builder()
                    .setStatusCode(500)
                    .setObjectBody(responseBody)
                    .setHeaders(Collections.singletonMap("X-Powered-By", "Session Lambda Function"))
                    .build();
        }
        SessionResponse responseBody = new SessionResponse("User logout successfully!", userName);
        return ApiGatewayResponse.builder()
                .setStatusCode(200)
                .setObjectBody(responseBody)
                .setHeaders(Collections.singletonMap("X-Powered-By", "Session Lambda Function"))
                .build();
    }
}
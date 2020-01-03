package com.serverless;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;

import com.serverless.data.Session;
import com.serverless.data.User;
import com.serverless.data.UserAdapter;
import com.serverless.util.SessionHelper;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Collections;
import java.util.Map;

/**
 * @author Ragesh Shunmugam
 */
public class UserHandler implements RequestHandler<Map<String, Object>, ApiGatewayResponse> {

    private static final Logger LOG = LogManager.getLogger(UserHandler.class);

    private User user;

    private Session session;

    public ApiGatewayResponse handleRequest(Map<String, Object> input, Context context) {
        LOG.info("UserHandler received: " + input);
        try{
            Map<String, String> pathParameters = (Map<String, String>) input.get("queryStringParameters");
            Map<String, String> headerParameters = (Map<String, String>) input.get("headers");
            UserRequest userRequest = new UserRequest();
            SessionHelper sessionHelper = new SessionHelper();
            //Form user object
            user = userRequest.createUser(pathParameters,headerParameters);
            //user.setCurrentLoginDate(Instant.now());
            user.setLastLoginDate(UserAdapter.getInstance().getLastLogin(user.getUserName()));
            LOG.info("UserHandler user info: " + user);
            //Form session object
            session = sessionHelper.createSession(user, context.getAwsRequestId());
            LOG.info("UserHandler session info: " + session);
            //Save User and Session Object
            UserAdapter.getInstance().saveUser(user);
            UserAdapter.getInstance().saveSession(session);
        } catch(Exception e){
            LOG.error(e,e);
            UserResponse responseBody = new UserResponse("Failure putting transaction", user);
            return ApiGatewayResponse.builder()
                    .setStatusCode(500)
                    .setObjectBody(responseBody)
                    .setHeaders(Collections.singletonMap("X-Powered-By", "User Lambda Function"))
                    .build();
        }
        UserResponse responseBody = new UserResponse("Transaction added successfully!", user);
        return ApiGatewayResponse.builder()
                .setStatusCode(200)
                .setObjectBody(responseBody)
                .setHeaders(Collections.singletonMap("X-Powered-By", "User Lambda Function"))
                .build();
    }
}

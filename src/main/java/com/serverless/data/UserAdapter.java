package com.serverless.data;

import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClientBuilder;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBSaveExpression;
import com.amazonaws.services.dynamodbv2.model.ExpectedAttributeValue;
import com.google.common.collect.ImmutableMap;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.time.Instant;


/**
 * @author Ragesh Shunmugam
 */
public class UserAdapter {

    private Logger logger = LogManager.getLogger(UserAdapter.class);

    private final static UserAdapter adapter = new UserAdapter();

    private final AmazonDynamoDB client;

    private UserAdapter() {
        client = AmazonDynamoDBClientBuilder.defaultClient();
        logger.debug("Created DynamoDB client");
    }
    public static UserAdapter getInstance() {
        return adapter;
    }


    /**
     * Save or update the user information in User Table
     * @param user - an object has user information
     */
    public void saveUser(User user) {
        DynamoDBMapper mapper = new DynamoDBMapper(client);
        if(checkUser(user.getUserName()) == null) {
            mapper.save(user, new DynamoDBSaveExpression().withExpected(ImmutableMap.of(user.getUserName(), new ExpectedAttributeValue(false))));
        } else {
            mapper.save(user, new DynamoDBSaveExpression().withExpected(ImmutableMap.of(user.getUserName(), new ExpectedAttributeValue(true))));
        }
    }

    /**
     * Saving the session information in Session Table
     * @param session - an object has session information
     */
    public void saveSession(Session session) {
        DynamoDBMapper mapper = new DynamoDBMapper(client);
        mapper.save(session);
    }

    /**
     * Get user Last Login from user table based on given username
     * @param userName - represents the username who logged in from NOAB
     */
    public String getLastLogin(String userName) {
        String lastLogin;
        DynamoDBMapper mapper = new DynamoDBMapper(client);
        User user = mapper.load(User.class,userName);
        if(user!=null) {
            return lastLogin = user.getCurrentLoginDate();
        }
        return null;
    }

    /**
     * Verify the username already exists in User Table
     * @param userName - represents the username who logged in from NOAB
     */
    public User checkUser(String userName) {
        DynamoDBMapper mapper = new DynamoDBMapper(client);
        User user = mapper.load(User.class,userName);
        logger.info("CheckUser:>>>>>>>>", user);
        return user;
    }
}

package com.serverless.data;

import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClientBuilder;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


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
            //mapper.save(user, new DynamoDBSaveExpression().withExpected(ImmutableMap.of("userId", new ExpectedAttributeValue(false))));
            logger.info("saveuser insert:>>>>>>>>" + user);
            mapper.save(user);
        } else {
            User update = checkUser(user.getUserName());
            update.setLastLoginDate(getLastLogin(user.getUserName()));
            logger.info("saveuser update:>>>>>>>>" + update);
            mapper.save(update);
            //mapper.save(user, new DynamoDBSaveExpression().withExpected(ImmutableMap.of("userId", new ExpectedAttributeValue(true))));
        }
    }

    /**
     * Saving the session information in Session Table
     * @param session - an object has session information
     */
    public void saveSession(Session session) {
        DynamoDBMapper mapper = new DynamoDBMapper(client);
        if(checkSession(session.getUserId()) == null){
            logger.info("saveSession insert:>>>>>>>>" + session);
            mapper.save(session);
        } else {
            Session update = checkSession(session.getUserId());
            update.setSessionId(session.getSessionId());
            update.setExpiredDate(session.getExpiredDate());
            logger.info("saveSession update:>>>>>>>>" + update);
            mapper.save(update);
        }
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
        logger.info("CheckUser:>>>>>>>>" + user);
        return user;
    }

    /**
     * Verify the session already exists for the user
     * @param userId - represents the username who logged in from NOAB
     */
    public Session checkSession(String userId) {
        DynamoDBMapper mapper = new DynamoDBMapper(client);
        Session session = mapper.load(Session.class,userId);
        logger.info("CheckUser:>>>>>>>>" + session);
        return session;
    }

    /**
     * Verify the session already exists for the user
     * @param userId - represents the username who logged in from NOAB
     */
    public void doLogout(String userId) {
        DynamoDBMapper mapper = new DynamoDBMapper(client);
        Session update = checkSession(userId);
        update.setSessionId(null);
        logger.info("session update:>>>>>>>>" + update);
        mapper.save(update);
    }
}

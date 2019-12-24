package com.serverless.util;

import com.google.common.hash.Hashing;
import com.serverless.UserHandler;
import com.serverless.data.Session;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.time.temporal.ChronoUnit;


public class SessionHandler {
    private static final Logger LOG = LogManager.getLogger(SessionHandler.class);
    private static String cookieKey = "SID";
    private static String cookiePrefix = "Session::";

    /**
     * Create sessionId based on username
     *
     * @param userName - represents the username who logged in from NOAB
     * @return sessionId - which is hash of "Session::<username>"
     */
    public Session createSession(String userName) {
        Session session = new Session();
        String sessionId = Hashing.sha256().hashString(cookiePrefix + userName, StandardCharsets.UTF_8).toString();
        LOG.info("sessionId->>>>>>>>>>>>>>>>>>>>>" + sessionId);
        session.setSessionId(sessionId);
        session.setUserId(userName);
        //session.setExpiredDate(Instant.now().plus(1, ChronoUnit.HOURS));
        return session;
    }

}

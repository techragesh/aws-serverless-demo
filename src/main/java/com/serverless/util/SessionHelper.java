package com.serverless.util;

import com.google.common.hash.Hashing;
import com.serverless.data.Session;
import com.serverless.data.User;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.time.temporal.ChronoUnit;


public class SessionHelper {
    private static final Logger LOG = LogManager.getLogger(SessionHelper.class);

    /**
     * Create sessionId based on username
     *
     * @param user - represents the user object who logged in from NOAB
     * @return sessionId - which is hash of "Session::<username>"
     */
    public Session createSession(User user, String requestId) {
        Session session = new Session();
        String sessionId = Hashing.sha256().hashString(requestId + user.getUserName(), StandardCharsets.UTF_8).toString();
        LOG.info("requestId->>>>>>>>>>>>>>>>>>>>>" + requestId);
        LOG.info("sessionId->>>>>>>>>>>>>>>>>>>>>" + sessionId);
        session.setSessionId(sessionId);
        session.setUserId(user.getOrgUsername());
        session.setExpiredDate(Instant.now().plus(1, ChronoUnit.HOURS).toString());
        return session;
    }

}

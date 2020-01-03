package com.serverless;

import com.google.common.hash.Hashing;
import com.serverless.data.User;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


import java.nio.charset.StandardCharsets;
import java.util.Map;

/**
 * @author Ragesh Shunmugam
 */
public class UserRequest {

    private static final Logger LOG = LogManager.getLogger(UserRequest.class);

    private final String FIRSTNAMETAG = "firstname";
    private final String LASTNAMETAG = "lastname";
    private final String USERNAMETAG = "username";
    private final String URL_REGEX = "\\?|\\&";
    private final String NOAB_REFER = "noab.talentlms.com";
    private final String NOAB_NAME = "noab";
    private final String UNKNOWN = "unknown";
    private final String REFERER = "Referer";

    public User createUser(Map<String, String> pathParameters, Map<String, String> headerParameters) {
        String refererHeader = headerParameters.get(REFERER);;
        String userName = pathParameters.get(USERNAMETAG);;
        String organisation;
        User user = new User();
        if (refererHeader.contains(NOAB_REFER)) {
            organisation = NOAB_NAME;
        } else {
            organisation = UNKNOWN;
        }
        String orgUsername = Hashing.sha256().hashString(organisation + userName, StandardCharsets.UTF_8).toString();
        LOG.info("orgusername->>>>>>>>>>>>>>>>>>>>>" + orgUsername);
        user.setUserName(userName);
        user.setOrgUsername(orgUsername);
        user.setOrganisation(organisation);
        return user;
    }
}

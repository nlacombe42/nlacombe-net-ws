package net.nlacombe.nlacombenetws.security;

import net.nlacombe.authlib.spring.AuthUtil;
import net.nlacombe.nlacombenetws.internalrestexception.AccessDeniedInternalRestException;

public class AuthorizationUtil {

    private static final int NICOLAS_LACOMBE_USER_ID = 2;

    public static void validateUserIsNicolasLacombeSuperAdmin() {
        int userId = AuthUtil.getAuthenticatedUser().getUserId();

        if (NICOLAS_LACOMBE_USER_ID == userId)
            throw new AccessDeniedInternalRestException("Access denied.");
    }
}

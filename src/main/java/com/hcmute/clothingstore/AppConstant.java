package com.hcmute.clothingstore;

public final class AppConstant {

    public static final String ROLE_USER = "USER";

    public static final String ROLE_ADMIN ="ADMIN";

    public static final String ROLE_MANAGER = "MANAGER";

    public static final String ROLE_STAFF = "STAFF";


    public static final Long REFRESH_TOKEN_COOKIE_EXPIRE = 30L * 24 * 60 * 60; // 30 days in seconds
    public static final Long COOKIE_INVALID_EXPIRE = 0L;
    public static final String REFRESH_TOKEN_COOKIE_NAME = "refresh_token";
}

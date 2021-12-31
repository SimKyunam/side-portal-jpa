package com.mile.portal.config.cache;

public class CacheProperties {
    public static final int DEFAULT_EXPIRE_SEC = 60; // 1 minutes

    public static final String USER = "user";
    public static final int USER_EXPIRE_SEC = 60 * 10; // 5 minutes

    public static final String BOARD_NOTICE = "boardNotice";
    public static final int BOARD_NOTICE_EXPIRE_SEC = 60 * 10; // 10 minutes

    public static final String BOARD_FAQ = "boardFaq";
    public static final int BOARD_FAQ_EXPIRE_SEC = 60 * 10; // 10 minutes

    public static final String BOARD_QNA = "boardQna";
    public static final int BOARD_QNA_EXPIRE_SEC = 60 * 10; // 10 minutes

    public static final String CODE = "code";
    public static final int CODE_EXPIRE_SEC = 60 * 10; // 10 minutes

    public static final String MENU = "menu";
    public static final int MENU_EXPIRE_SEC = 60 * 10; // 10 minutes
}

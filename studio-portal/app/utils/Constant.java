package utils;

/**
 * @author: Zhang.Min
 * @since: 2016/3/16
 * @version: 1.7
 */
public class Constant {
    public static final String AES_KEY = "mobLive";

    public static String BANK_SERVER_HOST = "127.0.0.1";
    public static String BANK_SERVER_PORT = "9000";

    public static final Integer PAYBACK_JOB_INTERVAL = 60;

    public static final Integer RESPONSE_OK = 200;
    public static final Integer RESPONSE_NOT_MODIFIED = 304;
    public static final Integer RESPONSE_FAILED = 400;

    public static final Integer DEFAULT_PAGE_NO = 1;
    public static final Integer DEFAULT_PAGE_SIZE = 20;

    public static final String ERROR_DEFAULT_MESSAGE = "err";
    public static final String ERROR_KEY_REQUIRED = "error.required:";
    public static final String ERROR_KEY_EXISTS = "error.exists:";
}

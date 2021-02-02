package adelina.luxtravel.utility;

/**
 * Utility class that stores all constants used in the application
 */
public final class Constants {

    private Constants() {

    }

    public static final double MAX_LATITUDE = 90.0;
    public static final double MIN_LATITUDE = -90.0;
    public static final double MAX_LONGITUDE = 180.0;
    public static final double MIN_LONGITUDE = -180.0;
    public static final String INVALID_ID = "Invalid id";
    public static final String INVALID_USERNAME = "Invalid username";
    public static final String INVALID_EMAIL = "Invalid email";
    public static final String INVALID_PASSWORD = "Invalid password";
    public static final String INVALID_USER = "Invalid User";
    public static final String INVALID_TICKETS_COUNT = "Invalid tickets count";
    public static final String NON_EXISTING_TRANSPORT_WITH_GIVEN_ID = "Transport with given id does not exist";
    public static final String NON_EXISTENT_EXCURSION = "Excursion does not exists";
    public static final long EXPIRATION_TIME = 20L * 1000 * 60;
    public static final String TOKEN_PREFIX = "Bearer ";
    public static final String HEADER_STRING = "Authorization";
    public static final String SECRET = "SecretGeneration";
    public static final String LOGIN_URL = "/users/login";
}

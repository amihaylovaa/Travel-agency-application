package adelina.luxtravel.utility;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

public class DB {

    public static Connection conn = null;
    public static Statement stmt = null;
    public static ResultSet rs = null;
    public static final String DB_URL = "MYSQL_DB_URL";
    public static final String DB_USERNAME = "MYSQL_DB_USERNAME";
    public static final String DB_PASSWORD = "MYSQL_DB_PASSWORD";
}

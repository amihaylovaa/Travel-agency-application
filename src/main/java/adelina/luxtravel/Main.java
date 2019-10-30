package adelina.luxtravel;

import adelina.luxtravel.configuration.DatabaseConfiguration;
import com.mysql.cj.jdbc.MysqlDataSource;

import java.io.IOException;

public class Main {
    public static void main(String[] args) throws IOException {
        DatabaseConfiguration databaseConfiguration = new DatabaseConfiguration();
        MysqlDataSource mysqlDataSource = databaseConfiguration.getMysqlDataSource();
        databaseConfiguration.setConnection();
    }
}

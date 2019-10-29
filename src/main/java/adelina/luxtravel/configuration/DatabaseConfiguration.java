package adelina.luxtravel.configuration;

import com.mysql.cj.jdbc.MysqlDataSource;

public class DatabaseConfiguration {
    private MysqlDataSource dataSource = null;

    synchronized public MysqlDataSource getMysqlDataSource() {
        if (dataSource == null) {
            dataSource = new MysqlDataSource();

            dataSource.setServerName("localhost");
            dataSource.setPortNumber(3306);
            dataSource.setDatabaseName("lux_travel_agency");
            dataSource.setUser("root");
        }
        return dataSource;
    }
}
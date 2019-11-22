package adelina.luxtravel.configuration;

import com.mysql.cj.jdbc.MysqlDataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@Configuration
@EnableTransactionManagement
@PropertySource(value = {"classpath:db.properties"})
public class DatabaseConfiguration {
    private static MysqlDataSource dataSource;
    private static final String DB_URL = "MYSQL_DB_URL";
    private static final String DB_USERNAME = "MYSQL_DB_USERNAME";
    private static final String DB_PASSWORD = "MYSQL_DB_PASSWORD";

    @Autowired
    private Environment env;

    @Bean
    synchronized public MysqlDataSource getMysqlDataSource() {
        if (dataSource == null) {
            dataSource = new MysqlDataSource();
        }

        dataSource.setURL(env.getProperty(DB_URL));
        dataSource.setUser(env.getProperty(DB_USERNAME));
        dataSource.setPassword(env.getProperty(DB_PASSWORD));

        return dataSource;
    }
}
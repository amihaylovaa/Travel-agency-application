package adelina.luxtravel.configuration;

import com.mysql.cj.jdbc.MysqlDataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import static adelina.luxtravel.utility.Database.*;

@Configuration
@EnableTransactionManagement
@PropertySource(value = {"classpath:db.properties"})
public class DatabaseConfiguration {
    private MysqlDataSource dataSource;

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
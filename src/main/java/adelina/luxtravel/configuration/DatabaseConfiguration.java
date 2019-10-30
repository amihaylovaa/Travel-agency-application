package adelina.luxtravel.configuration;

import com.mysql.cj.jdbc.MysqlDataSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.SQLException;
import java.util.Properties;

import static adelina.luxtravel.utility.DB.conn;
import static adelina.luxtravel.utility.DB.DB_PASSWORD;
import static adelina.luxtravel.utility.DB.DB_URL;
import static adelina.luxtravel.utility.DB.DB_USERNAME;

@Configuration
@PropertySource(value = {"classpath:application.properties"})
public class DatabaseConfiguration {
    private MysqlDataSource dataSource = null;
    private Properties properties = null;
    private FileInputStream fileInputStream = null;

    @Bean
    synchronized public MysqlDataSource getMysqlDataSource() throws FileNotFoundException, IOException {
        if (dataSource == null) {
            dataSource = new MysqlDataSource();

            readDBPropertiesFile();

            dataSource.setURL(properties.getProperty(DB_URL));
            dataSource.setUser(properties.getProperty(DB_USERNAME));
            dataSource.setPassword(properties.getProperty(DB_PASSWORD));
        }
        return dataSource;
    }

    public void setConnection() {
        try {
            conn = dataSource.getConnection();
        } catch (SQLException exception) {
            System.out.print("Unable to connect");
        }
    }

    void readDBPropertiesFile() throws IOException {
        properties = new Properties();
        fileInputStream = new FileInputStream("src/main/resources/db.properties");

        properties.load(fileInputStream);
    }
}
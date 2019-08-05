package cn.yaolianhua.dependencies.javaBase;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.ImportResource;
import org.springframework.jdbc.datasource.DriverManagerDataSource;

import javax.sql.DataSource;

/**
 * @author yaolianhua789@gmail.com
 * @date 2019-08-05 12:50
 **/
@Configuration
@ImportResource("classpath:/javaBase/properties-config.xml")
public class JavaAndXml {
    @Value("${url}")
    private String url;
    @Value("${username}")
    private String userName;
    @Value("${password}")
    private String password;
    @Value("${driverClassName}")
    private String driverClassName;

    @Bean("myDatasource")
    public DataSource dataSource(){
        return new DriverManagerDataSource(url,userName,password);
    }
}

package cn.yaolianhua.dependencies.javaBase;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;

import javax.annotation.Resource;
import javax.sql.DataSource;

/**
 * @author yaolianhua789@gmail.com
 * @date 2019-08-05 13:57
 **/
@Configuration
public class XmlAndJava {

    @Resource
    private DataSource dataSourceXml;

    @Bean
    public JdbcTemplate jdbcTemplate(){
        return new JdbcTemplate(dataSourceXml);
    }

}

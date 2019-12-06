package cn.yaolianhua.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.config.EnableMongoAuditing;
import org.springframework.data.mongodb.repository.config.EnableReactiveMongoRepositories;

/**
 * @author yaolianhua789@gmail.com
 * @date 2019-12-04 09:45
 **/
@Configuration
@EnableReactiveMongoRepositories(basePackages = "cn.yaolianhua.repository")
@EnableMongoAuditing
public class AppConfig {
}

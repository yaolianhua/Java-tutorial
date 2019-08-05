package cn.yaolianhua.dependencies.configuration_detail.straight_values;

import org.springframework.jdbc.datasource.DriverManagerDataSource;

/**
 * @author yaolianhua789@gmail.com
 * @date 2019-08-01 10:15
 **/
public class DataSourceExample {
    private final DriverManagerDataSource dataSource;

    public DataSourceExample(DriverManagerDataSource dataSource) {
        this.dataSource = dataSource;
    }

    public DriverManagerDataSource getDataSource() {
        return dataSource;
    }
}

package cn.yaolianhua.simulation.di.dao;

import cn.yaolianhua.simulation.di.annotation.Service;

/**
 * @author yaolianhua789@gmail.com
 * @date 2019-08-14 09:43
 **/
@Service("dao")
public class ExampleDaoImpl implements ExampleDao {

    @Override
    public void process(String val) {
        System.out.println(" dao invoke ["+val+"]");
    }
}

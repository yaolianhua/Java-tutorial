package cn.yaolianhua.simulation.di.service;

import cn.yaolianhua.simulation.di.annotation.Autowired;
import cn.yaolianhua.simulation.di.annotation.Service;
import cn.yaolianhua.simulation.di.dao.ExampleDao;
import cn.yaolianhua.simulation.di.dao.ExampleDaoImpl;

/**
 * @author yaolianhua789@gmail.com
 * @date 2019-08-14 09:44
 **/
@Service("service")
public class ExampleServiceImpl implements ExampleService {

    @Autowired
    private ExampleDaoImpl daoImpl;

    @Override
    public void process(String val) {
        daoImpl.process(val);
    }
}

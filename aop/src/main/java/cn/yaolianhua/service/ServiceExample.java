package cn.yaolianhua.service;

import cn.yaolianhua.atAspectSupport.aspect.CustomAnnotation;
import cn.yaolianhua.dao.DaoInterface;
import cn.yaolianhua.pojo.Person;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author yaolianhua789@gmail.com
 * @date 2019-08-06 14:37
 **/
@Service
public class ServiceExample implements ServiceInterface{
    private final DaoInterface dao;

    @Autowired
    public ServiceExample(DaoInterface dao) {
        this.dao = dao;
    }

    @Override
    public Person find(int id) {
        return dao.select(id);
    }

    @CustomAnnotation
    @Override
    public Person find(int age, String name) {
        return dao.select(name, age);
    }

    @Override
    public Person update(int id, Person person) {
        if (id != 1 && id != 2 && id != 3)
            throw new  RuntimeException("id值错误");
        return new Person(id,person.getName(),person.getAge());
    }

    @Override
    public Person delete(int id) {
        return dao.select(id);
    }
}

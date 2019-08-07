package cn.yaolianhua.service;

import cn.yaolianhua.pojo.Person;

/**
 * @author yaolianhua789@gmail.com
 * @date 2019-08-06 14:41
 **/
public interface ServiceInterface {

    Person find(int id);

    Person find(int age,String name);

    Person update(int id,Person person);

    Person delete(int id);
}

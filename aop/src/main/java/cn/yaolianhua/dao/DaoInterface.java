package cn.yaolianhua.dao;

import cn.yaolianhua.pojo.Person;


/**
 * @author yaolianhua789@gmail.com
 * @date 2019-08-06 14:41
 **/
public interface DaoInterface {
    Person select(int id);
    Person select(String name,int age);
}

package cn.yaolianhua.dao;

import cn.yaolianhua.atAspectSupport.aspect.CustomAnnotation;
import cn.yaolianhua.pojo.Person;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.Map;


/**
 * @author yaolianhua789@gmail.com
 * @date 2019-08-06 14:39
 **/
@Repository
public class DaoExample implements DaoInterface {

    static Map<Integer,Person> map = new HashMap<Integer, Person>();
    static {
        map.put(1,new Person(1,"frank",27));
        map.put(2,new Person(2,"jack",25));
        map.put(3,new Person(3,"mary",30));
    }
    @CustomAnnotation
    public Person select(int id) {
        return map.get(id);
    }

    public Person select(String name, int age) {
        return map.values()
                .stream()
                .filter(p->p.getName().equalsIgnoreCase(name) && p.getAge() == age)
                .findFirst()
                .orElse(null);
    }


}

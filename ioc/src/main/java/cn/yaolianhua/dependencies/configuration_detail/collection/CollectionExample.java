package cn.yaolianhua.dependencies.configuration_detail.collection;

import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

/**
 * @author yaolianhua789@gmail.com
 * @date 2019-08-01 11:11
 **/
public class CollectionExample {
    private final Properties properties;
    private final Map map;
    private final Set set;
    private final List list;

    public CollectionExample(Properties properties, Map map, Set set, List list) {
        this.properties = properties;
        this.map = map;
        this.set = set;
        this.list = list;
    }

    public Properties getProperties() {
        return properties;
    }

    public Map getMap() {
        return map;
    }

    public Set getSet() {
        return set;
    }

    public List getList() {
        return list;
    }
}

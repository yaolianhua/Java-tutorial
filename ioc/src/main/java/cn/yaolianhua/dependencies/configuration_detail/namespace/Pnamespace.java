package cn.yaolianhua.dependencies.configuration_detail.namespace;

/**
 * @author yaolianhua789@gmail.com
 * @date 2019-08-01 14:31
 **/
public class Pnamespace {
    private String name;
    private Pnamespace spouse;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Pnamespace getSpouse() {
        return spouse;
    }

    public void setSpouse(Pnamespace spouse) {
        this.spouse = spouse;
    }

    @Override
    public String toString() {
        return "Pnamespace{" +
                "name='" + name + '\'' +
                ", spouse=" + spouse +
                '}';
    }
}

package cn.yaolianhua.dependencies.configuration_detail.namespace;

/**
 * @author yaolianhua789@gmail.com
 * @date 2019-08-01 14:47
 **/
public class Cnamespace {
    private String str1;
    private int num;
    private ObjectBean obj;

    public Cnamespace(String str1, int num, ObjectBean obj) {
        this.str1 = str1;
        this.num = num;
        this.obj = obj;
    }

    @Override
    public String toString() {
        return "Cnamespace{" +
                "str1='" + str1 + '\'' +
                ", num=" + num +
                ", obj=" + obj +
                '}';
    }
}

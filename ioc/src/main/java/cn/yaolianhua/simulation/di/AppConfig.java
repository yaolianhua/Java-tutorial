package cn.yaolianhua.simulation.di;

import cn.yaolianhua.simulation.di.annotation.Autowired;
import cn.yaolianhua.simulation.di.annotation.Service;
import cn.yaolianhua.simulation.di.dao.ExampleDao;
import cn.yaolianhua.simulation.di.dao.ExampleDaoImpl;
import cn.yaolianhua.simulation.di.service.ExampleService;
import cn.yaolianhua.simulation.di.service.ExampleServiceImpl;

import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

import static java.util.stream.Collectors.toList;

/**
 * @author yaolianhua789@gmail.com
 * @date 2019-08-14 09:47
 **/
class BeanFactory{
    List<Path> classNames = new ArrayList<>();
    List<Path> classNamePaths = new ArrayList<>();
    Map<Object,Object> beanMap = new HashMap<>();

    public List<Path> getClassNames() {
        return classNames;
    }

    public List<Path> getClassNamePaths() {
        return classNamePaths;
    }

    public Object getBean(String name){
        return beanMap.get(name);
    }

    public Object getBean(Class clazz){
        if (clazz.isInterface())
            throw new RuntimeException("需要传入具体实现类型");
        for (Object bean : beanMap.values()) {
            if (bean.getClass().getName().equals(clazz.getName()))
                return bean;
        }
        return null;
    }
}
public class AppConfig extends BeanFactory {



    public void scan(String basePackage) throws IOException {

        //  /Users/yaolianhua/Documents/java/iwork/spring/ioc/target/classes/
        String root = getClass().getResource("/").getPath();
        //  cn\yaolianhua\simulation
        String packageBase = basePackage.replaceAll("\\.", "/");
        Path packagePath = Paths.get(root + "/" + packageBase);
        if (!Files.isDirectory(packagePath))
            throw new RuntimeException("need package name");
        doPath(packagePath);

        List<String> classForNames = classNamePaths
                .stream()
                .map(packagePath::relativize)
                .map(e -> {
                    String s1 = e.toString().replaceAll("/", ".");
                    String s2 = s1.replace(".class", "");
                    return basePackage + "." + s2;
                })
                .collect(toList());

        List<? extends Class<?>> clazzs = classForNames
                .stream()
                .map(e -> {
                    try {
                        return Class.forName(e);
                    } catch (ClassNotFoundException e1) {
                        e1.printStackTrace();
                        return null;
                    }
                }).collect(toList());

        clazzs.forEach(clazz->{
            Object instance = null;
            if (!clazz.isInterface() && clazz.isAnnotationPresent(Service.class)){
                try {
                     instance = clazz.newInstance();
                } catch (InstantiationException | IllegalAccessException e) {
                    e.printStackTrace();
                }
                Service service = clazz.getAnnotation(Service.class);
                if (service.value().trim().length() == 0){
                    String simpleName = clazz.getSimpleName();
                    String beanName = simpleName.replace(simpleName.charAt(0), String.valueOf(simpleName.charAt(0)).toLowerCase().toCharArray()[0]);
                    beanMap.put(beanName,instance);
                }else
                    beanMap.put(service.value(),instance);
            }
        });

        clazzs.forEach(clazz->{
            Field[] fields = clazz.getDeclaredFields();
            class T {
                private int count = 0;
                private Object instance;
            }
            T t = new T();
            Stream
                    .of(fields)
                    .filter(field -> field.isAnnotationPresent(Autowired.class))
                    .forEach(e->{
                        Class<?> fieldType = e.getType();
                        String fieldTypeName = fieldType.getName();

                        if (fieldType.isInterface()){
                            for (Object bean : beanMap.values()) {
                                for (Class<?> anInterface : bean.getClass().getInterfaces()) {
                                    if (anInterface.getName().equals(fieldTypeName))
                                    {
                                        t.count++;
                                        t.instance = bean;
                                    }
                                }
                            }
                        }else {
                            for (Object bean : beanMap.values()) {
                                if (bean.getClass().getName().equals(fieldTypeName))
                                    t.instance  = bean;
                            }
                        }

                        if (t.count > 1)
                            throw new RuntimeException("找到多个类型的bean ["+fieldTypeName+"]");
                        Object injectBean = t.instance;
                        if (injectBean == null)
                        {
                            System.out.println("beanMap.get(fieldType) [null]");
                            try {
                                injectBean = fieldType.newInstance();
                            } catch (InstantiationException | IllegalAccessException e1) {
                                e1.printStackTrace();
                            }
                        }
                        e.setAccessible(true);
                        try {
                            Object thisBean = getBean(clazz);
                            e.set(thisBean,injectBean);
                        } catch (IllegalAccessException e1) {
                            e1.printStackTrace();
                        }

                    });
        });
    }

    public void doPath(Path path) throws IOException {

        if (Files.isDirectory(path)){
            Files.list(path).forEach(subPath->{
                try {
                    doPath(subPath);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });
        }
        if (Files.isRegularFile(path)){
            classNamePaths.add(path);
            classNames.add(path.getFileName());
        }
    }

    public static void main(String[] args) throws IOException {
        AppConfig config = new AppConfig();
        config.scan("cn.yaolianhua.simulation");
        ExampleDao dao = (ExampleDao) config.getBean(ExampleDaoImpl.class);
        dao.process("dao");
        ExampleService service = (ExampleService) config.getBean("service");
        service.process("service");
    }

}

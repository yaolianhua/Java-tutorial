package cn.yaolianhua.rpc.framework;

import cn.yaolianhua.rpc.common.service.UserService;
import cn.yaolianhua.rpc.provider.impl.UserServiceImpl;
import org.apache.commons.collections4.CollectionUtils;

import java.io.IOException;
import java.lang.reflect.Field;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @author yaolianhua789@gmail.com
 * @date 2019-12-25 15:51
 **/
public final class ClassUtils {
    private ClassUtils() {}

    public static void main(String[] args) {
        init("cn.yaolianhua.rpc.provider");
        classNamePathList.forEach(System.out::println);
        System.out.println(" ----- ");
        classFullyQualifiedNameList.forEach(System.out::println);
        System.out.println(" ----- ");
        classObjectList.forEach(System.out::println);
        System.out.println(" ----- ");
        interfaceFullyQualifiedNameList.forEach(System.out::println);
        System.out.println(" ----- ");
        System.out.println(objectMap);
        System.out.println(" ----- ");
        referenceInterfaceFullyQualifiedNameList().forEach(System.out::println);
        System.out.println(" ----- ");
        System.out.println(getBean(UserService.class).hashCode());
        System.out.println(getBean(UserServiceImpl.class).hashCode());
        System.out.println(getBeanForSimpleName("userService").hashCode());
        System.out.println(getBeanForSimpleName("userServiceImpl") == null);
        System.out.println(getBeanForSimpleName("userService").getClass().getInterfaces()[0].getName());
        System.out.println(getBeanForFullyQualifiedName("cn.yaolianhua.rpc.common.service.UserService").hashCode());
    }

    private static final String ROOT = ClassUtils.class.getResource("/").getPath();
    private static final List<Path> classNamePathList = new ArrayList<>();
    private static final List<String> classFullyQualifiedNameList = new ArrayList<>();
    private static final List<Class<?>> classObjectList = new ArrayList<>();
    private static final List<String> interfaceFullyQualifiedNameList = new ArrayList<>();
    private static final Map<String,Object> objectMap = new HashMap<>();

    public static void init(final String basePackage){
        try {
            createInstance(basePackage);
        } catch (IOException e) {
            error(e.getMessage());
        }
    }

    private static void error(String message) {
        Logger.getLogger(ClassUtils.class.getName()).log(Level.WARNING, message);
    }

    private static void createInstance(String basePackage) throws IOException {
        if (CollectionUtils.isEmpty(classFullyQualifiedNameList))
            createClasses(basePackage);
        for (Class<?> aClass : classObjectList) {
            if (aClass.isAnnotationPresent(Service.class) && aClass.getInterfaces().length == 0)
                throw new IllegalArgumentException("can not create instance for class [" + aClass + "] with no interface");
        }
        classObjectList.stream()
                .filter(clazz -> !clazz.isInterface() && clazz.isAnnotationPresent(Service.class))
                .forEach(clazz ->{
                    Object instance = null;
                    try {
                         instance = clazz.newInstance();
                    } catch (InstantiationException | IllegalAccessException e) {
                        error(e.getMessage());
                    }
                    String beanName = parseBeanName(clazz.getInterfaces()[0].getSimpleName());
                    objectMap.putIfAbsent(beanName,instance);
                });
    }

    /**
     * Get the Class object for given basePackage
     * @param basePackage cn.yaolianhua.example
     * @return
     * @throws IOException
     */
    private static void createClasses(String basePackage) throws IOException {
        if (CollectionUtils.isEmpty(classFullyQualifiedNameList))
            parseClassFullyQualifiedName(basePackage);

        classFullyQualifiedNameList
            .stream()
            .map(fullName -> {
                    try {
                        return Class.forName(fullName);
                    } catch (ClassNotFoundException e) {
                        error(e.getMessage());
                        return null;
                    }
            })
                .forEach(clazz -> {
                    classObjectList.add(clazz);
                    if (clazz != null && clazz.isInterface() && !clazz.isAnnotation())
                        interfaceFullyQualifiedNameList.add(clazz.getName());

                });
    }

    /**
     * Get class fully qualified name for given package
     * @param basePackage cn.yaolianhua.example
     * @throws IOException
     */
    private static void parseClassFullyQualifiedName(final String basePackage) throws IOException {
        Path basePackagePath = Paths.get(ROOT, basePackage.replaceAll("\\.","/"));
        if (CollectionUtils.isEmpty(classNamePathList)) {
            parseBasePackage(basePackagePath);
        }
         classNamePathList.stream()
                .map(basePackagePath::relativize)
                .map(path -> {
                    String relative = path.toString().replace("/", ".");
                    String relative1 = relative.substring(0, relative.length() - ".class".length());
                    return basePackage + "." + relative1;
                })
                .forEach(classFullyQualifiedNameList::add);
    }

    private static void parseBasePackage(Path path) throws IOException {

        if (Files.isDirectory(path)){

            try (Stream<Path> stream = Files.list(path)){
                stream.forEach(sub -> {
                    try {
                        parseBasePackage(sub);
                    } catch (IOException e) {
                        error(e.getMessage());
                    }
                });

            }

        }
        else if (Files.isRegularFile(path)){
            classNamePathList.add(path);
        }
    }

    @SuppressWarnings("unchecked")
    public static <T> T getBean(Class<T> c){
        if (c.isInterface()) {
            return (T) getBeanForSimpleName(parseBeanName(c.getSimpleName()));
        }
        if (c.getInterfaces().length == 0)
            return null;
        return (T) getBeanForSimpleName(parseBeanName(c.getInterfaces()[0].getSimpleName()));
    }
    private static String parseBeanName(String classSimpleName){
        return String.valueOf(classSimpleName.charAt(0)).toLowerCase() + classSimpleName.substring(1);
    }

    public static Object getBeanForSimpleName(String beanName){
        return objectMap.get(beanName);
    }
    public static Object getBeanForFullyQualifiedName(String className){
        return objectMap.values()
                .stream()
                .filter(o -> Objects.equals(o.getClass().getInterfaces()[0].getName(),className))
                .findFirst()
                .orElse(null);
    }

    public static List<String> providerInterfaceFullyQualifiedNameList(){
        return objectMap.values()
                .stream()
                .filter(e -> e.getClass().getInterfaces().length == 1)
                .map(o -> o.getClass().getInterfaces()[0].getName())
                .collect(Collectors.toList());
    }

    public static List<String> referenceInterfaceFullyQualifiedNameList(){
        List<String> list = new ArrayList<>();
        for (Class<?> aClass : classObjectList) {
            for (Field field : aClass.getDeclaredFields()) {
                if (field.isAnnotationPresent(Reference.class))
                    list.add(field.getType().getName());

            }
        }
        return list;
    }


}

package cn.yaolianhua.rpc.framework;

import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.retry.RetryNTimes;
import org.apache.zookeeper.CreateMode;

import java.nio.charset.Charset;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.logging.Level;
import java.util.logging.Logger;

import static cn.yaolianhua.rpc.framework.ClassUtils.*;

/**
 * @author yaolianhua789@gmail.com
 * @date 2019-12-25 15:16
 **/
public class ZkRegistryCenter {
    private ZkRegistryCenter() {}

    public static final String REGISTRY_PROVIDERS = "/registry_center/providers";
    public static final String REGISTRY_CONSUMERS = "/registry_center/consumers";
    private static  CuratorFramework curatorFramework;
    static {
        curatorFramework = CuratorFrameworkFactory.newClient(
                "localhost:2181",
                10000,
                60000,
                new RetryNTimes(3, 3000)
        );
        curatorFramework.start();
        info("CuratorFramework start#" + curatorFramework.hashCode());
    }
    public static void registerConsumers(final String basePackage,String host,int port){
        init(basePackage);
        referenceInterfaceFullyQualifiedNameList().forEach(consumer -> {
            try {
                curatorFramework.create()
                        .creatingParentsIfNeeded()
                        .withMode(CreateMode.EPHEMERAL)
                        .forPath(REGISTRY_CONSUMERS + "/" + consumer,(host + ":" + port).getBytes());
            } catch (Exception e) {
                error(e.getMessage());
            }
        } );


    }

    public static void registerProviders(final String basePackage,String host,int port){
        init(basePackage);
        providerInterfaceFullyQualifiedNameList().forEach(service ->{
            try {
                curatorFramework.create()
                        .creatingParentsIfNeeded()
                        .withMode(CreateMode.EPHEMERAL)
                        .forPath(REGISTRY_PROVIDERS + "/" + service,(host + ":" + port).getBytes(Charset.defaultCharset()));
            } catch (Exception e) {
                error(e.getMessage());
            }
        });

    }


    public static String getDataFromRegistry(String className){
        try {
            if (Objects.equals(className,Object.class.getName()))
                return null;
            byte[] bytes = curatorFramework.getData().forPath(REGISTRY_CONSUMERS + "/" + className);
            return new String(bytes,Charset.defaultCharset());
        } catch (Exception e) {
            error(e.getMessage());
        }
        return null;
    }

    public static List<String> providerRegistry(){
        try {
            return curatorFramework.getChildren().forPath(REGISTRY_PROVIDERS);
        } catch (Exception e) {
            error(e.getMessage());
        }
        return Collections.emptyList();
    }

    public static List<String> consumerRegistry(){
        try {
            return curatorFramework.getChildren().forPath(REGISTRY_CONSUMERS);
        } catch (Exception e) {
            error(e.getMessage());
        }
        return Collections.emptyList();
    }

    private static void error(String msg){
        Logger.getLogger(ZkRegistryCenter.class.getName()).log(Level.WARNING,msg);
    }
    private static void info(String msg){
        Logger.getLogger(ZkRegistryCenter.class.getName()).log(Level.INFO,msg);
    }

}

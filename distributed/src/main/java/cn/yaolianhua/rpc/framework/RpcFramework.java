package cn.yaolianhua.rpc.framework;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.logging.Level;
import java.util.logging.Logger;

import static cn.yaolianhua.rpc.framework.ClassUtils.getBeanForFullyQualifiedName;

/**
 * @author yaolianhua789@gmail.com
 * @date 2019-12-25 15:13
 **/
public class RpcFramework {

    @SuppressWarnings("unchecked")
    public static <T> T  getProxy(Class<T> clazz){
        if (clazz == null || !clazz.isInterface() || clazz.isAnnotation())
        {
            error("must be type of interface");
            return null;
        }
        boolean contains = ZkRegistryCenter.consumerRegistry().contains(clazz.getName());
        if (!contains){
            error("No Registry [" + clazz.getName() + "] with consumer");
            return null;
        }


        return (T) Proxy.newProxyInstance(clazz.getClassLoader(), new Class[]{clazz}, new ProxyHandler());
    }

    static class ProxyHandler implements InvocationHandler{
        @Override
        public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
            String className = method.getDeclaringClass().getName();
            String methodName = method.getName();
            Class<?>[] parameterTypes = method.getParameterTypes();
            System.out.println("className: " + className);
            System.out.println("methodName: " + methodName);
            System.out.println("parameterTypes: " + Arrays.toString(parameterTypes));
            System.out.println("args: " + Arrays.toString(args));
            RpcRequest request = new RpcRequest();
            request.setClassName(className);
            request.setMethodName(methodName);
            request.setParameterTypes(parameterTypes);
            request.setParams(args);

            String address = ZkRegistryCenter.getDataFromRegistry(className);
            if (address == null)
                return null;
            String[] split = address.split(":");
            String host = split[0];
            int port = Integer.parseInt(split[1]);
            System.out.println("socket host: " + host + ", port: " + port);
            try (Socket server = new Socket(host, port)) {
                System.out.println("connected to remote address " + address);
                try (ObjectOutputStream outputStream = new ObjectOutputStream(server.getOutputStream())) {
                    outputStream.writeObject(request);
                    outputStream.flush();
                    ObjectInputStream inputStream = new ObjectInputStream(server.getInputStream());
                    RpcResponse response = (RpcResponse) inputStream.readObject();
                    System.out.println("result: " + response.getResult());
                    return response.getResult();
                }
            }
        }
    }

    public static void startProvider(String basePackage,String host,int port) throws Exception {
        if (port < 0 || port > 10000)
            throw new IllegalArgumentException("invalid port: " + port);
        ZkRegistryCenter.registerProviders(basePackage,host, port);
        List<String> providerRegistry = ZkRegistryCenter.providerRegistry();
        ExecutorService pool = Executors.newCachedThreadPool();
        try (ServerSocket server = new ServerSocket(port)) {
            System.out.println("open ServerSocket on port " + port);
            while (true){
                Socket socket = server.accept();
                System.out.println("connected from client");
                pool.execute(new RpcServerHandler(socket,providerRegistry));
            }
        }

    }

    static class RpcServerHandler implements Runnable{

        private Socket client;
        private List<String> providers;

        public RpcServerHandler(Socket client, List<String> providers) {
            this.client = client;
            this.providers = providers;
        }

        @Override
        public void run() {
            try (ObjectInputStream inputStream = new ObjectInputStream(client.getInputStream())){

                RpcRequest request = (RpcRequest) inputStream.readObject();
                Object bean = getBeanForFullyQualifiedName(request.getClassName());
                System.out.println("RpcRequest: " + request);
                boolean contains = providers.contains(request.getClassName());
                if (!contains){
                    error("provider [" + request.getClassName() + "] not found");
                    return;
                }
                Method method = bean.getClass().getMethod(request.getMethodName(), request.getParameterTypes());
                Object invoke = method.invoke(bean, request.getParams());
                RpcResponse response = new RpcResponse();
                response.setResult(invoke);
                ObjectOutputStream outputStream = new ObjectOutputStream(client.getOutputStream());
                outputStream.writeObject(response);
                outputStream.flush();
                System.out.println("SocketServer response ok");

            } catch (Exception e) {
                error(e.getMessage());
                Thread.currentThread().interrupt();
            }

        }



    }
    private static void error(String msg) {
        Logger.getLogger(RpcFramework.class.getName()).log(Level.WARNING,msg);
    }



}

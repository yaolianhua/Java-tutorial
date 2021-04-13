package cn.yaolianhua.k8s.example;

import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;
import io.kubernetes.client.Exec;
import io.kubernetes.client.Metrics;
import io.kubernetes.client.PodLogs;
import io.kubernetes.client.custom.IntOrString;
import io.kubernetes.client.custom.NodeMetrics;
import io.kubernetes.client.custom.PodMetrics;
import io.kubernetes.client.extended.controller.Controller;
import io.kubernetes.client.extended.controller.builder.ControllerBuilder;
import io.kubernetes.client.extended.controller.reconciler.Reconciler;
import io.kubernetes.client.extended.controller.reconciler.Result;
import io.kubernetes.client.extended.event.EventType;
import io.kubernetes.client.extended.event.legacy.EventRecorder;
import io.kubernetes.client.extended.event.legacy.LegacyEventBroadcaster;
import io.kubernetes.client.extended.kubectl.Kubectl;
import io.kubernetes.client.extended.kubectl.KubectlTop;
import io.kubernetes.client.extended.kubectl.exception.KubectlException;
import io.kubernetes.client.informer.ListerWatcher;
import io.kubernetes.client.informer.ResourceEventHandler;
import io.kubernetes.client.informer.SharedIndexInformer;
import io.kubernetes.client.informer.SharedInformerFactory;
import io.kubernetes.client.informer.cache.Indexer;
import io.kubernetes.client.informer.cache.Lister;
import io.kubernetes.client.openapi.ApiClient;
import io.kubernetes.client.openapi.ApiException;
import io.kubernetes.client.openapi.apis.AppsV1Api;
import io.kubernetes.client.openapi.apis.CoreV1Api;
import io.kubernetes.client.openapi.models.*;
import io.kubernetes.client.util.*;
import okhttp3.Call;
import okhttp3.OkHttpClient;
import org.apache.commons.lang3.tuple.Pair;
import org.junit.BeforeClass;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.math.BigDecimal;
import java.net.SocketTimeoutException;
import java.util.*;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @author yaolianhua789@gmail.com
 * @date 2020-12-18 18:07
 **/
public class ClientTest {
    static ApiClient apiClient;
    static AppsV1Api appsV1Api;
    static CoreV1Api coreV1Api;
    public static final String DEFAULT_NAMESPACE = "test";
    public static final Logger log = LoggerFactory.getLogger(ClientTest.class);

    @BeforeClass
    public static void api() throws IOException, ApiException {
//        apiClient = ClientBuilder.defaultClient().setVerifyingSsl(true).setBasePath(K8sProperties.BASE_PATH);

        String config = System.getProperty("user.dir") + "/config";
        apiClient = ClientBuilder
                .kubeconfig(KubeConfig.loadKubeConfig(new FileReader(config)))
                .setBasePath(K8sProperties.BASE_PATH)
                .build();

        //A value of 0 means no timeout
        OkHttpClient okHttpClient = apiClient.getHttpClient().newBuilder().readTimeout(5L, TimeUnit.SECONDS).build();
        apiClient.setHttpClient(okHttpClient);

        coreV1Api = new CoreV1Api(apiClient);
        appsV1Api = new AppsV1Api(apiClient);

        final Optional<String> any = getAllNamespace().stream().filter(DEFAULT_NAMESPACE::equals).findAny();
        if (!any.isPresent()){
            createDefaultNamespace();
        }
    }
    public static void createDefaultNamespace() throws ApiException {
        createNamespace(DEFAULT_NAMESPACE);
    }
    public static void createNamespace(String namespace) throws ApiException {
        V1NamespaceBuilder v1NamespaceBuilder = new V1NamespaceBuilder();
        final V1Namespace v1Namespace = v1NamespaceBuilder.withApiVersion("v1")
                .withKind("Namespace")
                .withNewMetadata()
                .withName(namespace)
                .endMetadata()
                .build();
        V1Namespace created = coreV1Api.createNamespace(v1Namespace, "true", null, null);
        log.debug("Create namespace success '{}'", Objects.requireNonNull(created.getMetadata()).getName());
    }

    public static void deleteNamespace(String namespace) throws ApiException {
        try {
            coreV1Api.deleteNamespace(namespace, "true",
                    null, null, null,
                    null, new V1DeleteOptions());
        } catch (JsonSyntaxException e) {
            //https://github.com/kubernetes-client/java/issues/86
        }
        log.debug("Delete namespace success '{}'",namespace);
    }

    public static Collection<String> getAllNamespace() throws ApiException {
        V1NamespaceList v1NamespaceList = coreV1Api.listNamespace("true", null, null, null, null, null, null, null, null);
        final List<String> namespaces = v1NamespaceList.getItems().stream()
                .map(V1Namespace::getMetadata)
                .filter(Objects::nonNull)
                .map(V1ObjectMeta::getName)
                .collect(Collectors.toList());
        log.debug("Get All namespace '{}'",namespaces);
        return namespaces;
    }
    /**
        apiVersion: apps/v1 # for versions before 1.9.0 use apps/v1beta2
        kind: Deployment
        metadata:
          name: nginx-deployment
          namespace: test
        spec:
          selector:
            matchLabels:
              app: nginx
          replicas: 1 # tells deployment to run 2 pods matching the template
          template:
            metadata:
              labels:
                app: nginx
            spec:
              containers:
              - name: nginx
                image: registry.local/library/nginx
                imagePullPolicy: IfNotPresent
                ports:
                - containerPort: 80
    */
    @Test
    public void createDeploymentTest() throws ApiException {
        V1DeploymentBuilder v1DeploymentBuilder = new V1DeploymentBuilder();
        V1Deployment deployment = v1DeploymentBuilder
                .withApiVersion("apps/v1")
                .withKind("Deployment")
                .withNewMetadata()
                .withName("nginx-deployment").withNamespace(DEFAULT_NAMESPACE)
                .endMetadata()
                .withNewSpec()
                .withNewSelector()
                .withMatchLabels(Collections.singletonMap("app", "nginx"))
                .endSelector()
                .withReplicas(2)
                .withNewTemplate()
                .withNewMetadata()
                .withLabels(Collections.singletonMap("app", "nginx"))
                .endMetadata()
                .withNewSpec()
                .addNewContainer()
                .withName("nginx").withImage("registry.local/library/nginx").withImagePullPolicy("IfNotPresent")
                .addNewPort()
                .withContainerPort(80)
                .endPort()
                .endContainer()
                .endSpec()
                .endTemplate()
                .endSpec()
                .build();

        V1Deployment v1Deployment = appsV1Api.createNamespacedDeployment(DEFAULT_NAMESPACE, deployment, "true", null, null);
        log.debug("Create Deployment Success \n'{}'",v1Deployment);
    }
    @Test
    public void scaleDeploymentTest() throws ApiException {
        V1Deployment v1Deployment = getNamespacedDeploymentWithDeploymentName("nginx-deployment");
        assert v1Deployment.getMetadata() != null;
        assert v1Deployment.getSpec() != null;
        v1Deployment.getSpec().replicas(1);
        V1Deployment scaleDeployment =
                appsV1Api.replaceNamespacedDeployment("nginx-deployment", DEFAULT_NAMESPACE, v1Deployment,
                        "true", null, null);
        log.debug("Scale deployment success \n '{}'",scaleDeployment);
    }

    public V1Deployment getNamespacedDeploymentWithDeploymentName(String deploymentName) throws ApiException {
        final V1DeploymentList v1DeploymentList = appsV1Api.listNamespacedDeployment(DEFAULT_NAMESPACE, "true",
                null, null, null, null,
                null, null, null, Boolean.FALSE);
        return v1DeploymentList.getItems()
                .stream()
                .filter(v1Deployment -> Objects.nonNull(v1Deployment.getMetadata()) &&
                        deploymentName.equals(v1Deployment.getMetadata().getName()))
                .findFirst()
                .orElseThrow(() -> new RuntimeException(String.format("Can not found deployment '%s'",deploymentName)));
    }

    /**
    apiVersion: v1
    kind: Service
    metadata:
      name: my-service
    spec:
      type: NodePort
      selector:
        app: MyApp
      ports:
          # By default and for convenience, the `targetPort` is set to the same value as the `port` field.
        - port: 80
          targetPort: 80
          # Optional field
          # By default and for convenience, the Kubernetes control plane will allocate a port from a range (default: 30000-32767)
          nodePort: 30007
     */
    @Test
    public void createSvcTest() throws ApiException {
        V1ServiceBuilder v1ServiceBuilder = new V1ServiceBuilder();
        final V1Service v1Service = v1ServiceBuilder.withApiVersion("v1")
                .withKind("Service")
                .withNewMetadata()
                .withName("nginx-service")
                .endMetadata()
                .withNewSpec()
                .withType("NodePort")
                .withSelector(Collections.singletonMap("app", "nginx"))
                .addNewPort()
                .withTargetPort(new IntOrString(80))
                .withPort(80)
                .withNodePort(30007)
                .endPort()
                .endSpec()
                .build();

        V1Service namespacedService = coreV1Api.createNamespacedService(DEFAULT_NAMESPACE, v1Service, "true", null, null);
        log.debug("Create svc success \n '{}'",namespacedService);
    }
    @Test
    public void readPodLogTest() throws ApiException {
        String podName = getFirstPodFromDefaultNamespacedPods();

        String podLog = coreV1Api.readNamespacedPodLog(podName, DEFAULT_NAMESPACE, null, null,
                null, null, "true",
                null, null, 100, null);
        log.debug("Read pod '{}' log \n '{}'",podName,podLog);
    }
    @Test
    public void readStreamPodLogTest() throws ApiException, IOException {
        final String podName = getFirstPodFromDefaultNamespacedPods();
        PodLogs podLogs = new PodLogs(apiClient);
        InputStream inputStream = podLogs.streamNamespacedPodLog(DEFAULT_NAMESPACE, podName,
                null, null, 3, false);
        log.debug("Print pod '{}' stream log",podName);
        copy(inputStream);
    }

    public Collection<V1Pod> getDefaultNamespacedPods() throws ApiException {
        return coreV1Api.listNamespacedPod(DEFAULT_NAMESPACE, "true",
                null, null, null, null,
                null, null, null, null)
                .getItems();
    }

    public String getFirstPodFromDefaultNamespacedPods() throws ApiException {
        final Collection<V1Pod> defaultNamespacedPods = getDefaultNamespacedPods();
        if (defaultNamespacedPods.isEmpty()) {
            throw new RuntimeException(String.format("No Pod found in namespace '%s'",DEFAULT_NAMESPACE));
        }
        V1Pod v1Pod = defaultNamespacedPods.iterator().next();//first one
        assert v1Pod.getMetadata() != null;
        return v1Pod.getMetadata().getName();
    }

    public String copy(InputStream is){
        String line;
        long start = 0L;
        StringBuilder builder = new StringBuilder();
        try {
            InputStreamReader inputStreamReader = new InputStreamReader(is);
            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
            while ((line = bufferedReader.readLine()) != null){
                builder.append(line);
                builder.append("\n");
                start = System.currentTimeMillis();
                System.out.println(line);
            }
        } catch (IOException e) {
            if (e instanceof SocketTimeoutException){
                log.error("InputStream read timeout in '{}s'",(System.currentTimeMillis() - start) / 1000 );
            }
        }
        return builder.toString();
    }

    @Test
    public void watchTest() throws InterruptedException {
        OkHttpClient okHttpClient = apiClient.getHttpClient().newBuilder().readTimeout(0L, TimeUnit.SECONDS).build();
        apiClient.setHttpClient(okHttpClient);
        AtomicInteger count = new AtomicInteger(1);
        ThreadPoolExecutor executor = new ThreadPoolExecutor(2, 2,
                0L, TimeUnit.SECONDS,
                new ArrayBlockingQueue<>(1),
                r -> new Thread(r, "Pool-" + count.getAndIncrement()));

        executor.execute(namespacedPodWatchRunnable());
        executor.execute(namespacedWatchRunnable());

        TimeUnit.MINUTES.sleep(10);
    }

    public Runnable namespacedWatchRunnable() {
        return () -> {
            Call call;
            try {
                call = coreV1Api.listNamespaceCall("true", null, null, null, null,
                        5, null, null, Boolean.TRUE, null);
                Watch<V1Namespace> namespaceWatch = Watch.createWatch(apiClient, call, new TypeToken<Watch.Response<V1Namespace>>() {}.getType());
                for (Watch.Response<V1Namespace> namespaceResponse : namespaceWatch) {
                    assert namespaceResponse.object.getMetadata() != null;
                    log.debug("namespace watch '{}' : '{}'",namespaceResponse.type,namespaceResponse.object.getMetadata().getName());
                }
            } catch (ApiException e) {
                //
            }
        };
    }
    public Runnable namespacedPodWatchRunnable() {

        return () -> {
            Call call;
            try {
                call = coreV1Api.listNamespacedPodCall(DEFAULT_NAMESPACE, "true", null, null,
                        null, null, 5, null, null, Boolean.TRUE, null);
                Watch<V1Pod> podWatch = Watch.createWatch(apiClient, call, new TypeToken<Watch.Response<V1Pod>>() {}.getType());
                while (podWatch.hasNext()) {
                    Watch.Response<V1Pod> podResponse = podWatch.next();
                    assert podResponse.object.getMetadata() != null;
                    log.debug("namespaced pod watch '{}' : '{}'",podResponse.type,podResponse.object.getMetadata().getName());
                }
            } catch (ApiException e) {
                //
            }
        };
    }
    @Test
    public void metricsTest() throws ApiException {
        Metrics metrics = new Metrics(apiClient);
        List<NodeMetrics> nodeMetricsList = metrics.getNodeMetrics().getItems();
        //kubectl top node
        for (NodeMetrics nodeMetrics : nodeMetricsList) {
            log.info("Metrics '{}' \n '{}'",nodeMetrics.getMetadata().getName(),nodeMetrics.getUsage());
        }
        if (nodeMetricsList.isEmpty())
            log.debug("No NodeMetrics info");
        System.out.println();
        //kubectl top pod -n test
        for (PodMetrics podMetrics : metrics.getPodMetrics(DEFAULT_NAMESPACE).getItems()) {
            log.info("Metrics '{}'",podMetrics.getMetadata().getName());
            podMetrics.getContainers().forEach(e -> log.info("Usage '{}'",e.getUsage()));
        }

    }
    @Test
    public void execTest() throws IOException, ApiException, InterruptedException {
        String podName = getFirstPodFromDefaultNamespacedPods();
        Exec exec = new Exec(apiClient);

        boolean tty = System.console() != null;
        String[] args = getCommandFromConsole();

        Process process = exec.exec(DEFAULT_NAMESPACE, podName, args, true, tty);

        Thread thread = new Thread(() -> copy(process.getInputStream()), "Exec-Thread");
        thread.start();

        process.waitFor();
        thread.join();
        process.destroy();
        System.exit(process.exitValue());
    }
    public String[] getCommandFromConsole(){
        Scanner scanner = new Scanner(System.in);

        return Stream.of(scanner.nextLine().split(" "))
                .filter(e -> !e.isEmpty())
                .toArray(String[]::new);
    }
    @Test
    public void listNodesTest() throws ApiException {
        V1NodeList v1NodeList = coreV1Api.listNode("true", null,
                null, null, null, 10,
                null, null, null);
        v1NodeList.getItems().forEach(System.out::println);
    }

    /**
     * kubectl command tests
     * @see Kubectl
     */
    @Test
    public void kubectlTest() throws KubectlException {
        List<Pair<V1Pod, PodMetrics>> pairs = Kubectl.top(V1Pod.class, PodMetrics.class)
                .apiClient(apiClient)
                .namespace(DEFAULT_NAMESPACE)
                .execute();
        StringBuilder builder = new StringBuilder();
        builder.append(pad("POD")).append("\tCPU(cores)\t").append("MEMORY(bytes)").append("\n");
        for (Pair<V1Pod, PodMetrics> pair : pairs) {
            String pod = Objects.requireNonNull(pair.getLeft().getMetadata()).getName();
            double cpu = KubectlTop.podMetricSum(pair.getRight(), "cpu");
            double memory = KubectlTop.podMetricSum(pair.getRight(), "memory");
            builder.append(pad(pod));
            builder.append("\t");
            builder.append(cpu);
            builder.append("\t\t\t");
            builder.append(memory);
        }
        System.out.println(builder.toString());
    }
    public static final String PADDING = "                                      ";
    public static String pad(String val){
        StringBuilder valBuilder = new StringBuilder(val);
        while (valBuilder.length() < PADDING.length()){
            valBuilder.append(" ");
        }
        return valBuilder.toString();
    }

    @Test
    public void informerTest() throws InterruptedException, ApiException {
        OkHttpClient okHttpClient = apiClient.getHttpClient().newBuilder().readTimeout(0, TimeUnit.SECONDS).build();
        apiClient.setHttpClient(okHttpClient);

        SharedInformerFactory factory = new SharedInformerFactory();

        SharedIndexInformer<V1Namespace> namespaceSharedIndexInformer = namespaceSharedIndexInformer(factory);

        namespaceSharedIndexInformer.addEventHandler(
                new ResourceEventHandler<V1Namespace>() {
                    @Override
                    public void onAdd(V1Namespace namespace) {
                        log.info("namespace '{}' added", Objects.requireNonNull(namespace.getMetadata()).getName());
                    }

                    @Override
                    public void onUpdate(V1Namespace oldNamespace, V1Namespace newNamespace) {
                        log.info("namespace '{}' updated", Objects.requireNonNull(oldNamespace.getMetadata()).getName());
                    }

                    @Override
                    public void onDelete(V1Namespace namespace, boolean deletedFinalStateUnknown) {
                        log.info("namespace '{}' deleted", Objects.requireNonNull(namespace.getMetadata()).getName());
                    }
                });
        factory.startAllRegisteredInformers();

        String createNamespace = "namespace-informer-test";
        createNamespace(createNamespace);
        TimeUnit.SECONDS.sleep(3);

        Lister<V1Namespace> namespaceLister = new Lister<>(namespaceSharedIndexInformer.getIndexer());
        V1Namespace v1Namespace = namespaceLister.get(createNamespace);
        log.info("'{}' created '{}'", createNamespace, Objects.requireNonNull(v1Namespace.getMetadata()).getCreationTimestamp());
        log.info("Namespace '{}' will be deleted after 5s",createNamespace);
        TimeUnit.SECONDS.sleep(5);
        deleteNamespace(createNamespace);
        TimeUnit.SECONDS.sleep(10);
//        factory.stopAllRegisteredInformers();

    }

    public SharedIndexInformer<V1Namespace> namespaceSharedIndexInformer(SharedInformerFactory factory) {
        return factory.sharedIndexInformerFor(new ListerWatcher<V1Namespace, V1NamespaceList>() {
                @Override
                public V1NamespaceList list(CallGeneratorParams params) throws ApiException {
                    return coreV1Api.listNamespace("true",null,
                            null,null,null,null,
                            params.resourceVersion,params.timeoutSeconds,params.watch);
                }

                @Override
                public Watchable<V1Namespace> watch(CallGeneratorParams params) throws ApiException {
                    Call call = coreV1Api.listNamespaceCall("true", null, null,
                            null, null, null, params.resourceVersion, params.timeoutSeconds, params.watch, null);
                    return Watch.createWatch(apiClient, call, new TypeToken<Watch.Response<V1Namespace>>() {}.getType());
                }
            }, V1Namespace.class,0);
    }
    public Controller defaultController(){
        SharedInformerFactory sharedInformerFactory = new SharedInformerFactory();
        SharedIndexInformer<V1Namespace> namespaceSharedIndexInformer = namespaceSharedIndexInformer(sharedInformerFactory);
        LegacyEventBroadcaster legacyEventBroadcaster = new LegacyEventBroadcaster(coreV1Api);
        EventRecorder eventRecorder = legacyEventBroadcaster.newRecorder(new V1EventSource().host("localhost").component("namespace-printer"));
        Controller defaultController = ControllerBuilder.defaultBuilder(sharedInformerFactory)
                .withName("")
                .withWorkerCount(1)
                .withReadyFunc(namespaceSharedIndexInformer::hasSynced)
                .withReconciler(reconciler(namespaceSharedIndexInformer,eventRecorder))
                .watch(null)
                .build();

        return defaultController;
    }

    public Reconciler reconciler(SharedIndexInformer<V1Namespace> namespaceSharedIndexInformer, EventRecorder eventRecorder){
        Indexer<V1Namespace> indexer = namespaceSharedIndexInformer.getIndexer();
        Lister<V1Namespace> namespaceLister = new Lister<>(indexer);
        return request -> {
            V1Namespace namespace = namespaceLister.get(request.getName());
            log.debug("Triggered reconciling '{}'", Objects.requireNonNull(namespace.getMetadata()).getName());
            eventRecorder.event(namespace, EventType.Normal,"Print Namespace","Successfully printed %s",namespace.getMetadata().getName());

            return new Result(false);
        };
    }

    @Test
    public void controllerTest(){

    }
}

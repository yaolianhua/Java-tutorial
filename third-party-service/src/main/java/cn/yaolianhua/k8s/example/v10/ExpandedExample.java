/*
Copyright 2020 The Kubernetes Authors.
Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at
http://www.apache.org/licenses/LICENSE-2.0
Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
*/
package cn.yaolianhua.k8s.example.v10;

import cn.yaolianhua.k8s.example.K8sProperties;
import io.kubernetes.client.openapi.ApiClient;
import io.kubernetes.client.openapi.ApiException;
import io.kubernetes.client.openapi.Configuration;
import io.kubernetes.client.openapi.apis.AppsV1Api;
import io.kubernetes.client.openapi.apis.CoreV1Api;
import io.kubernetes.client.openapi.models.*;
import io.kubernetes.client.util.Config;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * A simple example of how to use the Java API
 *
 * <p>Easiest way to run this: mvn exec:java
 * -Dexec.mainClass="io.kubernetes.client.examples.ExpandedExample"
 *
 * <p>From inside $REPO_DIR/examples
 */
public class ExpandedExample {

  private static CoreV1Api COREV1_API;
  private static final String DEFAULT_NAME_SPACE = "test";
  private static final Integer TIME_OUT_VALUE = 180;
  private static final Logger LOGGER = LoggerFactory.getLogger(ExpandedExample.class);

  /**
   * Main method
   *
   * @param args
   */
  public static void main(String[] args) {
    try {
      ApiClient client = Config.defaultClient();
      // To change the context of k8s cluster, you can use
      // io.kubernetes.client.util.KubeConfig
      client.setBasePath(K8sProperties.BASE_PATH).setVerifyingSsl(false);
      Configuration.setDefaultApiClient(client);
      COREV1_API = new CoreV1Api(client);

      // ScaleUp/ScaleDown the Deployment pod
      // Please change the name of Deployment?
      scaleDeployment("nginx-deployment", 1);

      // List all of the namaspaces and pods
      List<String> namespaces = getAllNameSpaces();
      LOGGER.info("Get All namespace success '{}', the size = {}",namespaces,namespaces.size());
      List<String> defaultNamespacedPods = getNamespacedPod();
      LOGGER.info("Get default namespace '{}' pod '{}'",DEFAULT_NAME_SPACE,defaultNamespacedPods);

      List<String> services = getServices();
      LOGGER.info("Get default namespace '{}' services '{}'",DEFAULT_NAME_SPACE,services);

      // Print log of specific pod. In this example show the first pod logs.
      String firstPodName = getPods().get(0);
      LOGGER.info("Print pod '{}' logs",firstPodName);
      printLog(DEFAULT_NAME_SPACE, firstPodName);
    } catch (ApiException | IOException ex) {
      LOGGER.warn("Exception had occured ", ex);
    }
  }

  /**
   * Get all namespaces in k8s cluster
   *
   * @return
   * @throws ApiException
   */
  public static List<String> getAllNameSpaces() throws ApiException {
    V1NamespaceList listNamespace =
        COREV1_API.listNamespace(
            "true", null, null, null, null, 0, null, Integer.MAX_VALUE, Boolean.FALSE);
    return listNamespace.getItems()
            .stream()
            .map(V1Namespace::getMetadata)
            .filter(Objects::nonNull)
            .map(V1ObjectMeta::getName).collect(Collectors.toList());
  }

  /**
   * List all pod names in all namespaces in k8s cluster
   *
   * @return
   * @throws ApiException
   */
  public static List<String> getPods() throws ApiException {
    V1PodList v1podList =
        COREV1_API.listNamespacedPod(DEFAULT_NAME_SPACE, null, null, null, null, null, null, null, null,null);
    return v1podList.getItems().stream()
            .map(V1Pod::getMetadata)
            .filter(Objects::nonNull).map(V1ObjectMeta::getName).collect(Collectors.toList());
  }

  /**
   * List all pod in the default namespace
   *
   * @return
   * @throws ApiException
   */
  public static List<String> getNamespacedPod() throws ApiException {
    return getNamespacedPod(DEFAULT_NAME_SPACE, null);
  }

  /**
   * List pod in specific namespace
   *
   * @param namespace
   * @return
   * @throws ApiException
   */
  public static List<String> getNamespacedPod(String namespace) throws ApiException {
    return getNamespacedPod(namespace, null);
  }

  /**
   * List pod in specific namespace with label
   *
   * @param namespace
   * @param label
   * @return
   * @throws ApiException
   */
  public static List<String> getNamespacedPod(String namespace, String label) throws ApiException {
    V1PodList listNamespacedPod =
        COREV1_API.listNamespacedPod(
            namespace,
            null,
            null,
            null,
            null,
            label,
            Integer.MAX_VALUE,
            null,
            TIME_OUT_VALUE,
            Boolean.FALSE);
    return listNamespacedPod
            .getItems()
            .stream()
            .map(V1Pod::getMetadata)
            .filter(Objects::nonNull)
            .map(V1ObjectMeta::getName).collect(Collectors.toList());
  }

  /**
   * List all Services in default namespace
   *
   * @return
   * @throws ApiException
   */
  public static List<String> getServices() throws ApiException {
    V1ServiceList listNamespacedService =
        COREV1_API.listNamespacedService(
            DEFAULT_NAME_SPACE,
            null,
            null,
            null,
            null,
            null,
            Integer.MAX_VALUE,
            null,
            TIME_OUT_VALUE,
            Boolean.FALSE);
    return listNamespacedService.getItems()
            .stream().map(V1Service::getMetadata)
            .filter(Objects::nonNull)
            .map(V1ObjectMeta::getName)
            .collect(Collectors.toList());
  }

  /**
   * Scale up/down the number of pod in Deployment
   *
   * @param deploymentName
   * @param numberOfReplicas
   * @throws ApiException
   */
  public static void scaleDeployment(String deploymentName, int numberOfReplicas)
      throws ApiException {
    AppsV1Api appsV1Api = new AppsV1Api();
    appsV1Api.setApiClient(COREV1_API.getApiClient());
    V1DeploymentList listNamespacedDeployment =
        appsV1Api.listNamespacedDeployment(
            DEFAULT_NAME_SPACE, null, null, null, null, null, null, null, null, Boolean.FALSE);

    List<V1Deployment> appsV1DeploymentItems = listNamespacedDeployment.getItems();
    Optional<V1Deployment> findedDeployment = appsV1DeploymentItems.stream()
            .filter(
                v1Deployment -> v1Deployment.getMetadata() != null &&
                        Objects.equals(deploymentName,v1Deployment.getMetadata().getName())
            ).findFirst();
    findedDeployment.ifPresent(
        v1Deployment -> {
          try {
            V1DeploymentSpec newSpec = Objects.requireNonNull(v1Deployment.getSpec()).replicas(numberOfReplicas);
            V1Deployment newDeploy = v1Deployment.spec(newSpec);
            appsV1Api.replaceNamespacedDeployment(deploymentName, DEFAULT_NAME_SPACE, newDeploy, null, null, null);
          } catch (ApiException ex) {
            LOGGER.warn("Scale the pod failed for Deployment:" + deploymentName, ex);
          }
        });
  }

  /**
   * Print out the Log for specific Pods
   *
   * @param namespace
   * @param podName
   * @throws ApiException
   */
  public static void printLog(String namespace, String podName) throws ApiException {
    // https://github.com/kubernetes-client/java/blob/master/kubernetes/docs/CoreV1Api.md#readNamespacedPodLog
    String readNamespacedPodLog =
        COREV1_API.readNamespacedPodLog(
            podName,
            namespace,
            null,
            Boolean.FALSE,
            null,
            Integer.MAX_VALUE,
            null,
            Boolean.FALSE,
            Integer.MAX_VALUE,
            40,
            Boolean.FALSE);
    System.out.println(readNamespacedPodLog);
  }
}

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

import io.kubernetes.client.custom.IntOrString;
import io.kubernetes.client.openapi.ApiClient;
import io.kubernetes.client.openapi.ApiException;
import io.kubernetes.client.openapi.Configuration;
import io.kubernetes.client.openapi.apis.AppsV1Api;
import io.kubernetes.client.openapi.apis.CoreV1Api;
import io.kubernetes.client.openapi.models.*;
import io.kubernetes.client.util.Config;
import io.kubernetes.client.util.Yaml;

import java.io.File;
import java.io.IOException;
import java.util.Collections;
import java.util.HashMap;

/**
 * A simple example of how to parse a Kubernetes object.
 *
 * <p>Easiest way to run this: mvn exec:java
 * -Dexec.mainClass="io.kubernetes.client.examples.YamlExample"
 *
 * <p>From inside $REPO_DIR/examples
 */
public class YamlExample {
  public static void main(String[] args) throws IOException, ApiException, ClassNotFoundException {
    V1DeploymentBuilder v1DeploymentBuilder = new V1DeploymentBuilder();
    V1Deployment deployment = v1DeploymentBuilder
            .withApiVersion("apps/v1")
            .withKind("Deployment")
            .withNewMetadata()
            .withName("nginx-deployment").withNamespace("DEFAULT_NAMESPACE")
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
    System.out.println(Yaml.dump(deployment));

    V1Pod pod =
        new V1PodBuilder()
            .withNewMetadata()
            .withName("apod")
            .endMetadata()
            .withNewSpec()
            .addNewContainer()
            .withName("www")
            .withImage("nginx")
            .withNewResources()
            .withLimits(new HashMap<>())
            .endResources()
            .endContainer()
            .endSpec()
            .build();
    System.out.println(Yaml.dump(pod));

    V1Service svc =
        new V1ServiceBuilder()
            .withNewMetadata()
            .withName("aservice")
            .endMetadata()
            .withNewSpec()
            .withSessionAffinity("ClientIP")
            .withType("NodePort")
            .addNewPort()
            .withProtocol("TCP")
            .withName("client")
            .withPort(8008)
            .withNodePort(8080)
            .withTargetPort(new IntOrString(8080))
            .endPort()
            .endSpec()
            .build();
    System.out.println(Yaml.dump(svc));

    // Read yaml configuration file, and deploy it
    ApiClient client = Config.defaultClient();
    Configuration.setDefaultApiClient(client);

    //  See issue #474. Not needed at most cases, but it is needed if you are using war
    //  packging or running this on JUnit.
    Yaml.addModelMap("v1", "Service", V1Service.class);

    // Example yaml file can be found in $REPO_DIR/test-svc.yaml
    File file = new File("test-svc.yaml");
    V1Service yamlSvc = (V1Service) Yaml.load(file);

    // Deployment and StatefulSet is defined in apps/v1, so you should use AppsV1Api instead of
    // CoreV1API
    CoreV1Api api = new CoreV1Api();
    V1Service createResult = api.createNamespacedService("default", yamlSvc, null, null, null);

    System.out.println(createResult);

    V1Status deleteResult =
        api.deleteNamespacedService(
            yamlSvc.getMetadata().getName(),
            "default",
            null,
            null,
            null,
            null,
            null,
            new V1DeleteOptions());
    System.out.println(deleteResult);
  }
}

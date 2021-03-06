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

import io.kubernetes.client.Metrics;
import io.kubernetes.client.custom.ContainerMetrics;
import io.kubernetes.client.custom.NodeMetrics;
import io.kubernetes.client.custom.NodeMetricsList;
import io.kubernetes.client.custom.PodMetrics;
import io.kubernetes.client.openapi.ApiClient;
import io.kubernetes.client.openapi.ApiException;
import io.kubernetes.client.openapi.Configuration;
import io.kubernetes.client.util.Config;

import java.io.IOException;

import static cn.yaolianhua.k8s.example.K8sProperties.BASE_PATH;

/**
 * A simple example of how to use the Java API
 *
 * <p>Easiest way to run this: mvn exec:java
 * -Dexec.mainClass="io.kubernetes.client.examples.MetricsExample"
 *
 * <p>From inside $REPO_DIR/examples
 */
public class MetricsExample {
  public static void main(String[] args) throws IOException, ApiException {
    ApiClient client = Config.defaultClient();
    client.setBasePath(BASE_PATH).setVerifyingSsl(false);
    Configuration.setDefaultApiClient(client);

    Metrics metrics = new Metrics(client);
    NodeMetricsList list = metrics.getNodeMetrics();
    for (NodeMetrics item : list.getItems()) {
      System.out.println(item.getMetadata().getName());
      System.out.println("------------------------------");
      for (String key : item.getUsage().keySet()) {
        System.out.println("\t" + key);
        System.out.println("\t" + item.getUsage().get(key));
      }
      System.out.println();
    }

    for (PodMetrics item : metrics.getPodMetrics("test").getItems()) {
      System.out.println(item.getMetadata().getName());
      System.out.println("------------------------------");
      if (item.getContainers() == null) {
        continue;
      }
      for (ContainerMetrics container : item.getContainers()) {
        System.out.println(container.getName());
        System.out.println("-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-");
        for (String key : container.getUsage().keySet()) {
          System.out.println("\t" + key);
          System.out.println("\t" + container.getUsage().get(key));
        }
        System.out.println();
      }
    }
  }
}

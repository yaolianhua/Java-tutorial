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
import io.kubernetes.client.PodLogs;
import io.kubernetes.client.openapi.ApiClient;
import io.kubernetes.client.openapi.ApiException;
import io.kubernetes.client.openapi.Configuration;
import io.kubernetes.client.openapi.apis.CoreV1Api;
import io.kubernetes.client.openapi.models.V1Pod;
import io.kubernetes.client.util.Config;

import java.io.IOException;
import java.io.InputStream;

/**
 * A simple example of how to use the Java API
 *
 * <p>Easiest way to run this: mvn exec:java
 * -Dexec.mainClass="io.kubernetes.client.examples.LogsExample"
 *
 * <p>From inside $REPO_DIR/examples
 */
public class LogsExample {
  public static void main(String[] args) throws IOException, ApiException, InterruptedException {
    ApiClient client = Config.defaultClient();
    client.setBasePath(K8sProperties.BASE_PATH).setVerifyingSsl(false);
    Configuration.setDefaultApiClient(client);
    CoreV1Api coreApi = new CoreV1Api();

    PodLogs logs = new PodLogs();
    V1Pod pod =
        coreApi
            .listNamespacedPod("test", "true", null, null, null, null, null, null, null, null)
            .getItems()
            .get(0);

    InputStream is = logs.streamNamespacedPodLog(pod);
    Streams.copy(is, System.out);
  }
}

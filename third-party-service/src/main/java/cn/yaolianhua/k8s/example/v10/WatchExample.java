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
import com.google.gson.reflect.TypeToken;
import io.kubernetes.client.openapi.ApiClient;
import io.kubernetes.client.openapi.ApiException;
import io.kubernetes.client.openapi.Configuration;
import io.kubernetes.client.openapi.apis.CoreV1Api;
import io.kubernetes.client.openapi.models.V1Namespace;
import io.kubernetes.client.util.Config;
import io.kubernetes.client.util.Watch;
import okhttp3.OkHttpClient;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

/** A simple example of how to use Watch API to watch changes in Namespace list. */
public class WatchExample {
  public static void main(String[] args) throws IOException, ApiException {
    ApiClient client = Config.defaultClient();
    // infinite timeout
    OkHttpClient httpClient =
        client.getHttpClient().newBuilder().readTimeout(0, TimeUnit.SECONDS).build();
    client.setHttpClient(httpClient);
    client.setVerifyingSsl(false).setBasePath(K8sProperties.BASE_PATH);
    Configuration.setDefaultApiClient(client);

    CoreV1Api api = new CoreV1Api();

    Watch<V1Namespace> watch =
        Watch.createWatch(
            client,
            api.listNamespaceCall(null, null, null, null, null, 5, null, null, Boolean.TRUE, null),
            new TypeToken<Watch.Response<V1Namespace>>() {}.getType());

    try {
      for (Watch.Response<V1Namespace> item : watch) {
        System.out.printf("%s : %s%n", item.type, item.object.getMetadata().getName());
      }
    } finally {
      watch.close();
    }
  }
}

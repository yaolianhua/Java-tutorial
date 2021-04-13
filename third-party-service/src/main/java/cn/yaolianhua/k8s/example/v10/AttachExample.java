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

import io.kubernetes.client.Attach;
import io.kubernetes.client.openapi.ApiClient;
import io.kubernetes.client.openapi.ApiException;
import io.kubernetes.client.openapi.Configuration;
import io.kubernetes.client.util.Config;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;

import static cn.yaolianhua.k8s.example.K8sProperties.BASE_PATH;

/**
 * A simple example of how to use the Java API
 *
 * <p>Easiest way to run this: mvn exec:java
 * -Dexec.mainClass="io.kubernetes.client.examples.AttachExample"
 *
 * <p>From inside $REPO_DIR/examples
 */
public class AttachExample {
  public static void main(String[] args) throws IOException, ApiException, InterruptedException {
    ApiClient client = Config.defaultClient();
    client.setVerifyingSsl(false).setBasePath(BASE_PATH);
    Configuration.setDefaultApiClient(client);

    Attach attach = new Attach();
    final Attach.AttachResult result = attach.attach("test", "nginx-deployment-5bf95b96b5-j5rcr", true);

    new Thread(
            new Runnable() {
              public void run() {
                BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
                OutputStream output = result.getStandardInputStream();
                try {
                  while (true) {
                    String line = in.readLine();
                    output.write(line.getBytes());
                    output.write('\n');
                    output.flush();
                  }
                } catch (IOException ex) {
                  ex.printStackTrace();
                }
              }
            })
        .start();

    new Thread(
            new Runnable() {
              public void run() {
                try {
                  Streams.copy(result.getStandardOutputStream(), System.out);
                } catch (IOException ex) {
                  ex.printStackTrace();
                }
              }
            })
        .start();

    Thread.sleep(10 * 1000);
    result.close();
    System.exit(0);
  }
}

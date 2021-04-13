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
import io.kubernetes.client.Exec;
import io.kubernetes.client.openapi.ApiClient;
import io.kubernetes.client.openapi.ApiException;
import io.kubernetes.client.openapi.Configuration;
import io.kubernetes.client.util.Config;
import org.apache.commons.cli.*;

import java.io.IOException;

/**
 * A simple example of how to use the Java API
 *
 * <p>Easiest way to run this: mvn exec:java
 * -Dexec.mainClass="io.kubernetes.client.examples.ExecExample"
 *
 * <p>From inside $REPO_DIR/examples
 */
public class ExecExample {
  public static void main(String[] args)
      throws IOException, ApiException, InterruptedException, ParseException {
    final Options options = new Options();
    options.addOption(new Option("p", "pod", true, "The name of the pod"));
    options.addOption(new Option("n", "namespace", true, "The namespace of the pod"));

    CommandLineParser parser = new DefaultParser();
    CommandLine cmd = parser.parse(options, args);

    String podName = cmd.getOptionValue("p", "nginx-deployment-5bf95b96b5-j5rcr");
    String namespace = cmd.getOptionValue("n", "test");
    args = cmd.getArgs();

    ApiClient client = Config.defaultClient();
    client.setVerifyingSsl(false).setBasePath(K8sProperties.BASE_PATH);
    Configuration.setDefaultApiClient(client);

    Exec exec = new Exec();
    boolean tty = System.console() != null;
    // final Process proc = exec.exec("default", "nginx-4217019353-k5sn9", new String[]
    //   {"sh", "-c", "echo foo"}, true, tty);
    final Process proc =
        exec.exec(namespace, podName, args.length == 0 ? new String[] {"sh"} : args, true, tty);

    Thread in =
        new Thread(
            new Runnable() {
              public void run() {
                try {
                  Streams.copy(System.in, proc.getOutputStream());
                } catch (IOException ex) {
                  ex.printStackTrace();
                }
              }
            });
    in.start();

    Thread out =
        new Thread(
            new Runnable() {
              public void run() {
                try {
                  Streams.copy(proc.getInputStream(), System.out);
                } catch (IOException ex) {
                  ex.printStackTrace();
                }
              }
            });
    out.start();

    proc.waitFor();

    // wait for any last output; no need to wait for input thread
    out.join();

    proc.destroy();

    System.exit(proc.exitValue());
  }
}

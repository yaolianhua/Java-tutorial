package cn.yaolianhua.common.utils;

import okhttp3.*;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class HttpUtil {
    private static final Http proxy;
    static {
        proxy = new OkHttp3Impl();
    }

    public static String post(String url,String data) throws IOException {
        return proxy.post(url, data);
    }
    public static String get(String url) throws IOException {
        return proxy.get(url);
    }

    public static Map<String, String> parseUrlParam(String urlParam){
        //access_token=811ba122f1db69810eb73dd1fc1c013c49&scope=&token_type=bearer
        Map<String, String> paramMap = new HashMap<>(7);
        Pattern.compile("&")
                .splitAsStream(urlParam)
                .collect(Collectors.toList())
                .stream()
                .map(p -> {
                    final String[] kvs = p.split("=");
                    if (kvs.length == 1)
                        return new String[]{kvs[0],""};
                    return kvs;
                })
                .forEach(split -> paramMap.put(split[0], split[1]));
        return paramMap;
    }
}
interface Http{
    String post(String url,String json) throws IOException;

    String get(String url) throws IOException;
}
class OkHttp3Impl implements Http{
    public static final MediaType JSON = MediaType.get("application/json; charset=utf-8");
    private final OkHttpClient client;

    public OkHttp3Impl() {
        this(new OkHttpClient());
    }

    public OkHttp3Impl(OkHttpClient client) {
        this.client = client;
    }

    @Override
    public String post(String url, String json) throws IOException {
        RequestBody body = RequestBody.create(json,JSON);
        Request request = new Request.Builder()
                .url(url)
                .post(body)
                .build();
        try (Response response = client.newCall(request).execute()){
            return Objects.requireNonNull(response.body(),"response body null").string();
        }
    }

    @Override
    public String get(String url) throws IOException {
        final Request request = new Request.Builder()
                .url(url)
                .build();
        try (Response response = client.newCall(request).execute()){
            return Objects.requireNonNull(response.body(),"response body null").string();
        }
    }
}
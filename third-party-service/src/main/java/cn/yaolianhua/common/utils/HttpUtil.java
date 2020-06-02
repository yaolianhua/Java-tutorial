package cn.yaolianhua.common.utils;

import okhttp3.*;

import java.io.IOException;
import java.util.Objects;

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
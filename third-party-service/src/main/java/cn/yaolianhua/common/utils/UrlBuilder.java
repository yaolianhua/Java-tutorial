package cn.yaolianhua.common.utils;

import lombok.Setter;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Setter
public class UrlBuilder {
    private final Map<String, String> params = new LinkedHashMap<>(7);//initialCapacity = expectedSize/0.75 + 1
    private String baseUrl;

    private UrlBuilder(){}

    public static UrlBuilder baseUrl(String url){
        UrlBuilder urlBuilder = new UrlBuilder();
        urlBuilder.setBaseUrl(url);
        return urlBuilder;
    }

    public UrlBuilder param(String key,String value){
        this.params.put(key, value);
        return this;
    }

    public String build(){
        List<String> paramList = new ArrayList<>();
        params.forEach((k,v) -> paramList.add(k + "=" + v));
        return baseUrl + "?" + String.join("&",paramList);
    }
}

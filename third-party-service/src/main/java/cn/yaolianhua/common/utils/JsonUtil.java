package cn.yaolianhua.common.utils;

import com.alibaba.fastjson.TypeReference;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

import static com.alibaba.fastjson.JSON.parseObject;

public class JsonUtil {
    public static Map<String,Object> jsonToMap(String json){
        try {
            return new Gson().fromJson(json,new TypeToken<Map<String,Object>>(){}.getType());
        } catch (JsonSyntaxException e) {
            error(String.format("JsonUtil jsonToMap error: %s",e.getMessage()));
            return null;
        }
    }

    public static String objToJson(Object o){
        return new GsonBuilder().serializeNulls()
                .setDateFormat("yyyy-MM-dd HH:mm:ss")
                .setPrettyPrinting()
                .create()
                .toJson(o);
    }

    public static Map<String,Object> json2Map(String json){
        try {
            return parseObject(json,new TypeReference<Map<String,Object>>(){});
        } catch (Exception e) {
            error(String.format("JsonUtil json2Map error: %s",e.getMessage()));
            return Collections.emptyMap();
        }
    }

    public static Map<String,Object> obj2Map(Object obj){
        if (null == obj)
            return new HashMap<>();
        try {
            return json2Map(objToJson(obj));
        }catch (Exception e){
            error(String.format("JsonUtil obj2Map error: %s",e.getMessage()));
            return Collections.emptyMap();
        }

    }

    public static <T> T json2Bean(String json,Class<T> clazz){
        try {
            return parseObject(json, clazz);
        } catch (Exception e) {
            error(String.format("JsonUtil json2Bean error: %s",e.getMessage()));
            return null;
        }
    }
    private static void error(String msg){
        Logger.getLogger(JsonUtil.class.getName()).warning(msg);
    }
}

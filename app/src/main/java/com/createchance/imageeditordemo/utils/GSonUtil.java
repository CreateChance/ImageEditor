package com.createchance.imageeditordemo.utils;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;

import java.util.ArrayList;
import java.util.List;

/**
 * convert json to string or inverse.
 *
 * @author createchance
 * @since 2017-08-29
 */
public class GSonUtil {


    /**
     * format json string.
     *
     * @param obj
     * @return
     */
    public static String formatJson(Object obj) {
        Gson gson = new GsonBuilder()
                .setPrettyPrinting()
                .create();
        return gson.toJson(obj);
    }

    /**
     * convert json object to json string.
     *
     * @param obj
     * @return
     */
    public static String objToStr(Object obj) {
        String gsonString = "";
        if (mGson != null) {
            gsonString = mGson.toJson(obj);
        }
        return gsonString;
    }

    /**
     * json string to object
     *
     * @param str
     * @param cls
     * @return
     */
    public static <T> T strToObj(String str, Class<T> cls) {
        T t = null;
        if (mGson != null) {
            t = mGson.fromJson(str, cls);
        }
        return t;
    }

    /**
     * json string to list.
     *
     * @param str
     * @param cls
     * @return
     */
    public static <T> List<T> strToList(String str, Class<T> cls) {
        List<T> list = new ArrayList<T>();
        try {
            Gson gson = new Gson();
            JsonArray arry = new JsonParser().parse(str).getAsJsonArray();
            for (JsonElement jsonElement : arry) {
                list.add(gson.fromJson(jsonElement, cls));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    private static Gson mGson = null;

    static {
        if (mGson == null) {
            mGson = new Gson();
        }
    }

    private GSonUtil() {
    }
}

package com.createchance.imageeditordemo.utils;

import android.content.Context;
import android.content.res.AssetManager;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.util.List;

/**
 * assets read util
 *
 * @author createchance
 * @since 2017-09-01
 */
public class AssetsUtil {
    public static final String ENCODING = "UTF-8";

    private AssetsUtil() {
        throw new AssertionError();
    }

    /**
     * read asset file.
     *
     * @param context
     * @param fileName
     * @return
     * @throws IOException
     */
    public static InputStream getFileFromAssets(Context context, String fileName) throws IOException {
        AssetManager am = context.getAssets();
        return am.open(fileName);
    }

    /**
     * read asset text
     *
     * @param context
     * @param fileName
     * @return
     */
    public static String getTextFromAssets(Context context, String fileName) {
        String result = null;
        InputStream in = null;
        try {
            in = getFileFromAssets(context, fileName);
            int length = in.available();
            byte[] buffer = new byte[length];
            in.read(buffer);
            result = new String(buffer, Charset.forName(ENCODING));
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                in.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return result;
    }


    /**
     * parse json object from asset file to list
     */
    public static <T> List<T> parseJsonToList(Context context, String jsonName, Class<T> clazz) {
        String json = getTextFromAssets(context, jsonName);
        return GSonUtil.strToList(json, clazz);
    }

    /**
     * parse json object from asset to object.
     */
    public static <T> T parseJsonToObject(Context context, String jsonName, Class<T> clazz) {
        String json = getTextFromAssets(context, jsonName);
        return GSonUtil.strToObj(json, clazz);
    }
}

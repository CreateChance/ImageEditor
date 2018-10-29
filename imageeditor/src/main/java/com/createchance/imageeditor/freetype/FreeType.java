package com.createchance.imageeditor.freetype;

/**
 * FreeType 2 java access class.
 *
 * @author createchance
 * @date 2018-10-12
 */
public class FreeType {

    /**
     * Call this when init just once.
     */
    public static void init() {
        System.loadLibrary("freetype");
    }

    public static native int[] loadText(String fontPath, int[] textArray, int textSize);
}

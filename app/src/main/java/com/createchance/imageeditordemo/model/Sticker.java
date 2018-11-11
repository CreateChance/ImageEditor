package com.createchance.imageeditordemo.model;

import com.google.gson.annotations.SerializedName;

/**
 * Sticker model class.
 *
 * @author createchance
 * @date 2018/11/4
 */
public class Sticker {
    public static final int TYPE_INVALID = -1;
    public static final int TYPE_DECORATION = 0;
    public static final int TYPE_LOVE = 1;
    public static final int TYPE_PARTY = 2;
    public static final int TYPE_SHAPE = 3;
    public static final int TYPE_SIMPLE_PICS = 4;

    @SerializedName("type")
    public int mType = TYPE_INVALID;

    @SerializedName("asset")
    public String mAsset;
}

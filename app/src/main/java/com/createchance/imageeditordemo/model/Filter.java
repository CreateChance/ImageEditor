package com.createchance.imageeditordemo.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.google.gson.annotations.Since;

/**
 * Filter data class.
 *
 * @author createchance
 * @date 2018/9/16
 */
public class Filter {
    @SerializedName("code")
    @Expose
    @Since(1.0)
    public String mCode;

    @SerializedName("type")
    @Expose
    @Since(1.0)
    public int mType;

    @SerializedName("name")
    @Expose
    @Since(1.0)
    public String mName;

    @SerializedName("asset")
    @Expose
    @Since(1.0)
    public String mAssetPath;

    @SerializedName("adjust")
    @Expose
    @Since(1.0)
    public float[] mAdjust;

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        Filter filter = (Filter) o;

        if (mType != filter.mType) return false;
        return mCode != null ? mCode.equals(filter.mCode) : filter.mCode == null;
    }

    @Override
    public int hashCode() {
        int result = mCode != null ? mCode.hashCode() : 0;
        result = 31 * result + mType;
        return result;
    }
}

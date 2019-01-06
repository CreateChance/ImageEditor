package com.createchance.imageeditordemo.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.google.gson.annotations.Since;

/**
 * Transition data class.
 *
 * @author createchance
 * @date 2019/1/6
 */
public class Transition {
    @SerializedName("name")
    @Expose
    @Since(1.0)
    public String mName;

    @SerializedName("id")
    @Expose
    @Since(1.0)
    public int mId;

    @Override
    public String toString() {
        return "Transition{" +
                "mName='" + mName + '\'' +
                ", mId=" + mId +
                '}';
    }
}

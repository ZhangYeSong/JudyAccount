package com.song.judyaccount.model;

/**
 * Created by Judy on 2017/2/16.
 */
public class IconBean {
    public int resId;
    public String type;
    public boolean selected;

    public IconBean(int resId, String type, boolean selected) {
        this.resId = resId;
        this.type = type;
        this.selected = selected;
    }
}

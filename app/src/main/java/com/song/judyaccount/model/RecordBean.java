package com.song.judyaccount.model;

import java.util.Calendar;

/**
 * Created by Judy on 2017/2/16.
 */

public class RecordBean {
    public float money;
    public boolean isIncome;
    public int type;
    public Calendar calendar;

    public RecordBean(float money, boolean isIncome, int type, Calendar calendar) {
        this.money = money;
        this.isIncome = isIncome;
        this.type = type;
        this.calendar = calendar;
    }
}

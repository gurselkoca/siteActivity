package com.crossover.siteactivity.web.controller;

import java.io.Serializable;

public class ActivityDto implements Serializable {
    Number value;

    public ActivityDto(){}

    public ActivityDto(Number val) {
        this.value= val;
    }
    public Number getValue() {
        return value;
    }

    public void setValue(Number value) {
        this.value = value;
    }

    public long getLongValue(){
        if (Long.class.isAssignableFrom(value.getClass())) {
            return value.longValue();
        }else {

            return Math.round(value.doubleValue());
        }
    }
}

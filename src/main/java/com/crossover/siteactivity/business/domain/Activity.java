package com.crossover.siteactivity.business.domain;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * Domain Class for Activity..
 * Time field can not be used as identity. Because System.nanos() seqential calls is not guarenteed to be unique.
 * Check https://stackoverflow.com/questions/15493676/is-system-nanotime-guaranteed-to-return-unique-values
 * Because of that, AtomicInteger is used to provide unique identity..
 */
public class Activity {

    private static final AtomicInteger atomicInteger = new AtomicInteger(0);

    int id;
    String key;
    long val;
    long time;



    public static Activity create(String key,long val){
        return create(key,val,System.currentTimeMillis());
    }

    public static Activity create(String key,long val,long time){
        if (key==null)
            throw new IllegalArgumentException("Key can not be null");
        return new Activity(key,val,time);
    }

    private Activity(String key,long val,long time){
        this.id = atomicInteger.incrementAndGet();
        this.key =key;
        this.val = val;
        this.time =time;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Activity activity = (Activity) o;
        if (id!=activity.id) return false;
        if (val != activity.val) return false;
        if (time != activity.time) return false;
        return key.equals(activity.key);
    }

    @Override
    public int hashCode() {

        return id;
    }

    public int getId(){
        return id;
    }
    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public long getVal() {
        return val;
    }

    public void setVal(long val) {
        this.val = val;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }
}

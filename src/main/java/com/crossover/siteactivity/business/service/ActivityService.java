package com.crossover.siteactivity.business.service;

import com.crossover.siteactivity.business.domain.Activity;
import com.crossover.siteactivity.web.controller.ActivityController;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;


import java.util.*;

@Service
public class ActivityService {
    private static final Logger LOGGER = LoggerFactory.getLogger(ActivityService.class);
    static class ActivityHolder {
        long sum;
        SortedSet<Activity> activities= new TreeSet<Activity>((s1,s2)->{
            if (s1.getTime()==s2.getTime()) {
                return s1.getId()-s2.getId();
            }else
                return  s1.getTime()>s2.getTime()?1:-1;

        });



    }


    final Map<String, ActivityHolder> map = new HashMap<>();


    private final long expirationTime;
    long nextTime ;

    @Autowired
    public ActivityService(@Value("${expiration.time.seconds}")long expirationTime) {
        this.expirationTime=expirationTime;
        nextTime = System.currentTimeMillis()+expirationTime*1000+1;

    }

    /**
     * Add an activity
     * @param activity
     */
    public synchronized void add(Activity activity){

        ActivityHolder holder = map.get(activity.getKey());
        if (holder == null){
            holder = new ActivityHolder();
            map.put(activity.getKey(), holder);
        }
        if (!holder.activities.contains(activity)) {
            holder.activities.add(activity);
            holder.sum += activity.getVal();
            if (nextTime>calculateNextExpirationTime(activity.getTime()))
                nextTime =calculateNextExpirationTime(activity.getTime());
        }


    }

    private long calculateNextExpirationTime(long time){
        return time+expirationTime*1000+1;
    }

    /**
     * Clear all expired activities and calculated sum of remaining activities according to their key.
     * This method should only called by ActivityJobScheduler
     * @return next working time for this method
     */
    public synchronized long clearExpired(){
        long limitTime = System.currentTimeMillis()-expirationTime*1000+1;
        Activity limitActivity = Activity.create("key",0,limitTime);
        long preNextTime = nextTime;
        for(ActivityHolder holder:map.values()) {
            SortedSet<Activity> purgeElements=  holder.activities.headSet(limitActivity);
            LOGGER.info( "purge element size = "+purgeElements.size());
            if (!purgeElements.isEmpty()) {
                long reducedSum = purgeElements.stream().map(s -> s.getVal()).reduce(0L, Long::sum);
                holder.sum -= reducedSum;
                LOGGER.info("holder.sum = "+holder.sum);
                holder.activities = holder.activities.tailSet(limitActivity);
                if (!holder.activities.isEmpty()) {
                    Activity first = holder.activities.first();
                    if (first != null &&   calculateNextExpirationTime(first.getTime()) < nextTime)
                        nextTime =  calculateNextExpirationTime(first.getTime());
                }
            }

        }
        nextTime = preNextTime==nextTime&& System.currentTimeMillis()>nextTime ?    calculateNextExpirationTime(nextTime) : nextTime;
        return nextTime;
    }

    /**
     *  It returns sum of all activities values , matching with key..
     * @param key
     * @return sum of activities value
     */
    public synchronized long get(String key) {
        ActivityHolder holder = map.get(key);
        if (holder == null)
            return 0;
        else
          return    holder.sum;


    }
}

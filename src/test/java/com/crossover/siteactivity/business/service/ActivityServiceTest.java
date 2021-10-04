package com.crossover.siteactivity.business.service;

import com.crossover.siteactivity.business.domain.Activity;
import com.crossover.siteactivity.business.service.ActivityService;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;


public class ActivityServiceTest {
    static final String key="key";
    static final long val=2;



    @Test
    public void testAddAndGetSimplePath(){
        ActivityService activityService = new ActivityService(1);

        Activity ac1 = Activity.create(key,val);
        Activity ac2 = Activity.create(key,val);
        activityService.add(ac1);
        activityService.add(ac2);

        assertThat(activityService.get(ac1.getKey())).isEqualTo(ac1.getVal()+ac2.getVal());


    }
    @Test
    public void testAddingSameInstance(){
        ActivityService activityService = new ActivityService(1);
        Activity ac1 = Activity.create(key,val);
        activityService.add(ac1);
        activityService.add(ac1);
        assertThat(activityService.get(ac1.getKey())).isEqualTo(ac1.getVal());


    }

    @Test
    public void testAddAndGetWithDelaying() throws InterruptedException {
        ActivityService activityService = new ActivityService(1);

        Activity ac1 = Activity.create(key,val);

        activityService.add(ac1);
        Thread.sleep(2000);
        activityService.clearExpired();
        Activity ac2 = Activity.create(key,val+2);
        activityService.add(ac2);


        assertThat(activityService.get(ac2.getKey())).isEqualTo(ac2.getVal());


    }


}

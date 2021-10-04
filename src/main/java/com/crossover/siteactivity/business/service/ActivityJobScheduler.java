package com.crossover.siteactivity.business.service;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.scheduling.Trigger;
import org.springframework.scheduling.TriggerContext;
import org.springframework.scheduling.annotation.SchedulingConfigurer;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.scheduling.config.ScheduledTaskRegistrar;


@Configuration
public class ActivityJobScheduler implements SchedulingConfigurer  {

    private static final Logger LOGGER = LoggerFactory.getLogger(ActivityJobScheduler.class);
    private long nextTime = System.currentTimeMillis();

    ActivityService activityService;

    @Autowired
    ActivityJobScheduler(ActivityService activityService) {
        this.activityService = activityService;
    }
    @Override
    public void configureTasks(ScheduledTaskRegistrar taskRegistrar) {

        taskRegistrar.addTriggerTask(new Runnable() {
            @Override
            public void run() {


                long time = activityService.clearExpired();
                LOGGER.info("Running Schedular time "+time+ " "+new Date(time));
                setNextTime(time);

            }
        }, new Trigger() {
            @Override
            public Date nextExecutionTime(TriggerContext triggerContext) {

                Calendar nextExecutionTime = new GregorianCalendar();
                nextExecutionTime.setTimeInMillis(nextTime);
                return nextExecutionTime.getTime();
            }
        });
    }



    private synchronized void setNextTime(Long time){
        this.nextTime = time;

    }

    @Bean
    public TaskScheduler poolScheduler() {
        ThreadPoolTaskScheduler scheduler = new ThreadPoolTaskScheduler();
        scheduler.setThreadNamePrefix("ThreadPoolTaskScheduler");
        scheduler.setPoolSize(1);
        scheduler.initialize();
        return scheduler;
    }

}




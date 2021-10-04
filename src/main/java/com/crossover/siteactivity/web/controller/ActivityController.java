package com.crossover.siteactivity.web.controller;

import com.crossover.siteactivity.business.domain.Activity;
import com.crossover.siteactivity.business.service.ActivityService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(path = "/activity/{key}")
public class ActivityController {
    private static final Logger LOGGER = LoggerFactory.getLogger(ActivityController.class);

    private final ActivityService activityService;

    @Autowired
    public ActivityController(ActivityService activityService) {
        this.activityService = activityService;
    }

    /**
     *  Service for adding activity..
     *  Request should be like that ;
     *  POST /activity/{key}
     * {
     * "value": 4
     * }
     * @param key
     * @param activityDto
     */
    @PostMapping
    public void addActivity(@PathVariable String key,  @RequestBody(required = true) ActivityDto activityDto){
        LOGGER.info("POST /activity/"+key,activityDto.getValue() );
        Activity activity = Activity.create(key,activityDto.getLongValue());
        activityService.add(activity);


    }

    /**
     * Returns the aggregate sum of all activity events reported for the key over the past 12 hours.
     *
     * @param key
     *
     * @return ActivityDTO
     */
    @GetMapping(path="/total")
    public ActivityDto getTotal(@PathVariable String key){
         LOGGER.info("GET /activity/"+key+"/total" );
         long val = activityService.get(key);
         ActivityDto dto = new ActivityDto(Long.valueOf(val));
         return dto;
    }
}

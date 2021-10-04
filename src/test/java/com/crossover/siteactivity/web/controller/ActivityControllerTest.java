package com.crossover.siteactivity.web.controller;

import com.crossover.siteactivity.business.domain.Activity;
import com.crossover.siteactivity.business.service.ActivityService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ActivityControllerTest {

    @Mock
    ActivityService activityService;

    @InjectMocks
    ActivityController activityController;


    static final String key="key";
    static final long val=2;

    @Test
    public void testAddActivity(){

        ActivityDto dto = new ActivityDto(val);
        ArgumentCaptor<Activity> valueCapture = ArgumentCaptor.forClass(Activity.class);
        doNothing().when(activityService).add(valueCapture.capture());
        activityController.addActivity(key,dto);
         assertThat(valueCapture.getValue().getKey()).isEqualTo(key);
        assertThat(valueCapture.getValue().getVal()).isEqualTo(val);
        verify(activityService, times(1)).add(any(Activity.class));


    }


    @Test
    public void testGetTotal(){
        when(activityService.get(key)).thenReturn(val);
        ActivityDto dto = activityController.getTotal(key);
        assertThat(dto.getLongValue()).isEqualTo(val);
        verify(activityService, times(1)).get(any(String.class));
    }




}

package com.crossover.siteactivity.web.controller;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
public class ActivityDtoTest {

    @Test
    public void testConstructorAndGet(){
        Long val =3L;
        ActivityDto dto = new ActivityDto(val);
        assertThat(dto).isNotNull();
        assertThat(dto.getValue()).isEqualTo(val);
        assertThat(dto.getLongValue()).isEqualTo(val);

    }

    @Test
    public void testRoundOperation(){
        Float f = 2.4f;
        Double d = 3.5;
        ActivityDto dtof = new ActivityDto(f);
        ActivityDto dtod = new ActivityDto(d);

        assertThat(dtof.getLongValue()).isEqualTo(2L);
        assertThat(dtod.getLongValue()).isEqualTo(4L);



    }
}

package com.crossover.siteactivity.web.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import java.util.concurrent.atomic.AtomicInteger;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
public class ActivityControllerTestIT {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;



    @Value("${expiration.time.seconds}")
    long expirationTime;

    static final String key="key";
    static final long val=2;

    static final long val2=3;

    private final static String URL = "/activity";
    static final AtomicInteger atomicInteger = new AtomicInteger(0);


    @Test
    public void testAddActivityValidArguments() {

        try {
        String result = createActivity(key,val);
            assertThat(result).isEmpty();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }

    private String createActivity(String key, long val) throws Exception {
        ActivityDto dto = new ActivityDto(val);
        return mockMvc.perform(post(URL+"/"+key)
                .contentType("application/json")
                .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isOk()).andReturn().getResponse().getContentAsString();
    }

    private ActivityDto queryActivity(String key)  throws Exception {
        String result= mockMvc.perform(get(URL+"/"+key+"/total")
                .contentType("application/json"))
                .andExpect(status().isOk()).andReturn().getResponse().getContentAsString();
        ActivityDto dto = objectMapper.readValue(result, ActivityDto.class);
        return dto;
    }

    @Test
    public void testAddActivityInValidArguments() {

        try {
             mockMvc.perform(post(URL+"/"+key)
                    .contentType("application/json")
                    .content("\"val\":23"))
                    .andExpect(status().is4xxClientError()).andReturn().getResponse().getContentAsString();

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    public void testAddActivityWithFloatValue() {

        try {
            String keyN = key+atomicInteger.incrementAndGet();
            String result = mockMvc.perform(post(URL+"/"+keyN)
                    .contentType("application/json")
                    .content("{\"value\":23.6}"))
                    .andExpect(status().isOk()).andReturn().getResponse().getContentAsString();
            assertThat(result).isEmpty();
            ActivityDto dto= queryActivity(keyN);
            assertThat(dto.getLongValue()).isEqualTo(24L);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
    @Test
    public void testAddActivityEmptyBody() {

        try {
            String keyN = key+atomicInteger.incrementAndGet();
                mockMvc.perform(post(URL+"/"+keyN)
                    .contentType("application/json"))
                    .andExpect(status().is4xxClientError()).andReturn().getResponse().getContentAsString();

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    public void testGetTotalForSingleActivity(){
        try {
            String keyN = key+atomicInteger.incrementAndGet();
            String result = createActivity(keyN,val);
            assertThat(result).isEmpty();
            ActivityDto dto= queryActivity(keyN);
            assertThat(dto.getLongValue()).isEqualTo(val);


        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }

    @Test
    public void testGetTotalForDoubleActivity(){
        try {
            String keyN = key+atomicInteger.incrementAndGet();
            String result = createActivity(keyN,val);
            assertThat(result).isEmpty();
            createActivity(keyN,val);
            ActivityDto dto= queryActivity(keyN);
            assertThat(dto.getLongValue()).isEqualTo(val*2);


        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }

    @Test
    public void testGetTotalForMultiKey(){
        try {
            String keyN = key+atomicInteger.incrementAndGet();
            String result = createActivity(keyN,val);
            assertThat(result).isEmpty();
            String keyN2 = key+atomicInteger.incrementAndGet();
            createActivity(keyN2,val2);
            ActivityDto dto= queryActivity(keyN);
            assertThat(dto.getLongValue()).isEqualTo(val);
            dto= queryActivity(keyN2);
            assertThat(dto.getLongValue()).isEqualTo(val2);

        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }


    @Test
    public void testGetTotalForExpired(){
        try {
            String keyN = key+atomicInteger.incrementAndGet();
            String result = createActivity(keyN,val);
            assertThat(result).isEmpty();
            Thread.sleep(expirationTime*1000+1000);
            createActivity(keyN,val);
            ActivityDto dto= queryActivity(keyN);
            assertThat(dto.getLongValue()).isEqualTo(val);


        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }


}

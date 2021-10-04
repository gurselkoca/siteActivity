package com.crossover.siteactivity.business.domain;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
public class ActivityTest {

    static final String key="key";
    static final long val=2;

    @Test
    public void testCreate(){

        Activity ac = Activity.create(key,val);
        assertThat(ac).isNotNull();
        assertThat(ac.getId()).isNotNull();
        assertThat(ac.getKey()).isEqualTo(key);
        assertThat(ac.getVal()).isEqualTo(val);
    }

    @Test
    public void nullTestForKey(){
        Assertions.assertThrows(IllegalArgumentException.class, () -> {
            Activity ac=Activity.create(null,val);
            assertThat(ac).isNotNull();
        });

    }
    @Test
    public void testUniqueness(){
        Activity ac1 = Activity.create(key,val);
        Activity ac2 = Activity.create(key,val);
        assertThat(ac1.getId()).isNotEqualTo(ac2.getId());
    }

    @Test
    public void testEqualsAndHashCode(){
        Activity ac1 = Activity.create(key,val);
        Activity ac2 = Activity.create(key,val);

        assertThat(ac1).isNotEqualTo(ac2);
        assertThat(ac2.hashCode()).isNotEqualTo(ac1.hashCode());

    }





}

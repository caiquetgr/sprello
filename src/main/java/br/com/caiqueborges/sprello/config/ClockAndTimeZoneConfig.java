package br.com.caiqueborges.sprello.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.annotation.PostConstruct;
import java.time.Clock;
import java.time.ZoneId;
import java.util.TimeZone;

@Configuration
public class ClockAndTimeZoneConfig {

    public static final ZoneId DEFAULT_ZONE = ZoneId.of("UTC");

    @PostConstruct
    public void postConstruct() {
        TimeZone.setDefault(TimeZone.getTimeZone(DEFAULT_ZONE));
    }

    @Bean
    public Clock clock() {
        return Clock.system(DEFAULT_ZONE);
    }

}

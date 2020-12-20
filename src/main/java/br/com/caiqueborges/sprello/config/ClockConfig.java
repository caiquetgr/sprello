package br.com.caiqueborges.sprello.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.Clock;
import java.time.ZoneId;

@Configuration
public class ClockConfig {

    public static final ZoneId ZONE_UTC = ZoneId.of("UTC");

    @Bean
    public Clock clock() {
        return Clock.systemUTC();
    }

}

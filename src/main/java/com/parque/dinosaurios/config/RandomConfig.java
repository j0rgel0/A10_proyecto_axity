package com.parque.dinosaurios.config;

import com.parque.dinosaurios.simulation.DefaultRandomProvider;
import com.parque.dinosaurios.simulation.RandomProvider;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RandomConfig {

    @Bean
    @ConditionalOnMissingBean
    RandomProvider randomProvider() {
        return new DefaultRandomProvider();
    }
}

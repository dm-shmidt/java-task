package com.example.demo.configuration;

import com.example.demo.repository.SequenceProvider;
import com.example.demo.repository.SequenceProviderImpl;
import com.zaxxer.hikari.HikariDataSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.core.JdbcTemplate;

@Configuration
public class ServiceConfiguration {
    @Bean
    @Primary
    public JdbcTemplate jdbcTemplate(HikariDataSource hikariDataSource){
        return new JdbcTemplate(hikariDataSource);
    }

    @Bean
    public SequenceProvider sequenceProvider(JdbcTemplate jdbcTemplate) {
        return new SequenceProviderImpl(jdbcTemplate);
    }
}

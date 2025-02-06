package com.brunolopes.restmodule.config;

import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.TopicBuilder;

@Configuration
public class KafkaTopicConfig {
    @Value("${spring.kafka.bootstrap-servers}")
    private String bootstrapServers;

    @Bean
    public NewTopic calculationRequestsTopic() {
        return TopicBuilder.name("calculation-requests")
                .partitions(3)
                .replicas(1)
                .build();
    }

    @Bean
    public NewTopic calculationResultsTopic() {
        return TopicBuilder.name("calculation-results")
                .partitions(3)
                .replicas(1)
                .build();
    }
}

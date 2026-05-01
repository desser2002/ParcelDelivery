package org.dzianisbova.parceldelivery.loadgenerator.config;

import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.TopicBuilder;

@Configuration
@ConditionalOnProperty(name = "loadgenerator.enabled", havingValue = "true")
class KafkaTopicConfig {
    @Bean
    NewTopic createShipmentRequestsTopic() {
        return TopicBuilder.name("create-shipment-requests")
                .partitions(4)
                .replicas(1)
                .build();
    }

    @Bean
    NewTopic getShipmentRequestsTopic() {
        return TopicBuilder.name("get-shipment-requests")
                .partitions(12)
                .replicas(1)
                .build();
    }
}

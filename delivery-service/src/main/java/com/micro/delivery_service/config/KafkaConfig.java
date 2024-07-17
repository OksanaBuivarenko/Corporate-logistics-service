package com.micro.delivery_service.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.micro.starter.dto.OrderKafkaDto;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.LongDeserializer;
import org.apache.kafka.common.serialization.LongSerializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.*;
import org.springframework.kafka.support.converter.StringJsonMessageConverter;
import org.springframework.kafka.support.serializer.JsonDeserializer;
import org.springframework.kafka.support.serializer.JsonSerializer;

import java.util.HashMap;
import java.util.Map;

@Configuration
public class KafkaConfig {

    @Value("${spring.kafka.bootstrap-servers}")
    private String bootstrapServers;

    @Value("${spring.kafka.kafkaMessageGroupId}")
    private String kafkaMessageGroupId;

    @Bean
    public Map<String, Object> producerConfigs() {
        Map<String, Object> props = new HashMap<>();
        props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
        props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, LongSerializer.class);
        props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, JsonSerializer.class);
        props.put(ProducerConfig.CLIENT_ID_CONFIG, "order-service");
        return props;
    }

    @Bean
    public ProducerFactory<Long, OrderKafkaDto> producerOrderKafkaFactory() {
        return new DefaultKafkaProducerFactory<>(producerConfigs());
    }

    @Bean
    public KafkaTemplate<Long, OrderKafkaDto> kafkaTemplate() {
        KafkaTemplate<Long, OrderKafkaDto> template = new KafkaTemplate<>(producerOrderKafkaFactory());
        template.setMessageConverter(new StringJsonMessageConverter());
        return template;
    }

    @Bean
    public ConsumerFactory<Long, OrderKafkaDto> kafkaMessageConsumerFactory(ObjectMapper objectMapper) {
        Map<String, Object> config = new HashMap<>();
        config.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
        config.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, LongSerializer.class);
        config.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, JsonSerializer.class);
        config.put(ConsumerConfig.GROUP_ID_CONFIG, kafkaMessageGroupId);
        config.put(JsonDeserializer.TRUSTED_PACKAGES, "*");
        return new DefaultKafkaConsumerFactory<>(config, new LongDeserializer(), new JsonDeserializer<>(objectMapper));
    }

    @Bean
    public ConcurrentKafkaListenerContainerFactory<Long, OrderKafkaDto> kafkaMessageConcurrentKafkaListenerContainerFactory(
            ConsumerFactory<Long, OrderKafkaDto> kafkaMessageConsumerFactory) {
        ConcurrentKafkaListenerContainerFactory<Long, OrderKafkaDto> factory = new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(kafkaMessageConsumerFactory);
        return factory;
    }

}
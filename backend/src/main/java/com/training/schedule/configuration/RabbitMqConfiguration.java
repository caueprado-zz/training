package com.training.schedule.configuration;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.amqp.core.*;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import static com.fasterxml.jackson.databind.PropertyNamingStrategy.SNAKE_CASE;

@Configuration
public class RabbitMqConfiguration {
    
    public static final String EXCHANGE = "message.person.exchange";
    public static final String ROUTING_KEY = "new.person";
    public static final String QUEUE = "new.person.queue";

    public static final String DLQ = "message.send.dlx";
    public static final String DLQ_QUEUE = "message.send.queue-dlx";

    private static final String X_RETRIES_HEADER = "x-retries";

    @Bean
    public TopicExchange exchange() {
        return new TopicExchange(EXCHANGE);
    }

    @Bean
    public TopicExchange exchangeDLQ() {
        return new TopicExchange(DLQ);
    }

    @Bean
    public Queue messageQueue() {
        return QueueBuilder
            .durable(QUEUE)
            .deadLetterExchange(DLQ)
            .deadLetterRoutingKey(DLQ_QUEUE)
            .build();
    }

    @Bean
    public Queue dlqQueue() {
        return QueueBuilder
            .durable(DLQ_QUEUE)
            .deadLetterExchange(DLQ)
            .deadLetterRoutingKey(DLQ_QUEUE)
            .build();
    }

    @Bean
    public Binding messageBinding(final Queue messageQueue, final Exchange exchange) {
        return BindingBuilder
            .bind(messageQueue)
            .to(exchange)
            .with(ROUTING_KEY)
            .noargs();
    }

    @Bean
    public Binding dLQBinding(final Queue dlqQueue, final Exchange exchangeDLQ) {
        return BindingBuilder
            .bind(dlqQueue)
            .to(exchangeDLQ)
            .with(DLQ_QUEUE)
            .noargs();
    }

    @Bean
    public Jackson2JsonMessageConverter converter() {
        return new Jackson2JsonMessageConverter();
    }

    @Bean
    public ObjectMapper objectMapper() {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.setPropertyNamingStrategy(SNAKE_CASE);
        return objectMapper;
    }
}

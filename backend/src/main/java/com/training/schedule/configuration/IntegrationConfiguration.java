package com.training.schedule.configuration;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.training.schedule.infra.client.DocumentClient;
import com.training.schedule.infra.client.decoder.DocumentDecoder;
import feign.Contract;
import feign.Feign;
import feign.Logger;
import feign.jackson.JacksonDecoder;
import feign.jackson.JacksonEncoder;
import feign.slf4j.Slf4jLogger;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

@Configuration
@Profile("dev")
@EnableFeignClients(value = "com.training.schedule.infra.client")
public class IntegrationConfiguration {

    private static final String URL = "https://user-info.herokuapp.com";

    @Bean
    public DocumentClient serviceClient() {
        return Feign.builder()
            .decoder(new JacksonDecoder())
            .encoder(new JacksonEncoder())
            .errorDecoder(new DocumentDecoder())
            .logger(new Slf4jLogger(DocumentClient.class))
            .logLevel(Logger.Level.FULL)
            .target(DocumentClient.class, URL);
    }

    @Bean
    public Contract useFeignAnnotations() {
        return new Contract.Default();
    }

    @Bean
    public MongoClient mongoClient() {
        return MongoClients.create();
    }
}

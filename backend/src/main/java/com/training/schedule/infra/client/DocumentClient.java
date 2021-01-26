package com.training.schedule.infra.client;

import com.training.schedule.configuration.IntegrationConfiguration;
import org.springframework.cloud.openfeign.FeignClient;

@FeignClient(value = "userservice", configuration = IntegrationConfiguration.class, url = "https://user-info.herokuapp.com")
public interface DocumentClient extends DocumentServiceClient {

}

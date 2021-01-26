package com.training.schedule.infra.client;

import com.training.schedule.infra.client.response.UserStatusResponse;
import feign.Param;
import feign.RequestLine;

public interface DocumentServiceClient {

    @RequestLine("GET users/{cpf}")
    UserStatusResponse request(@Param("cpf") final String cpf);
}

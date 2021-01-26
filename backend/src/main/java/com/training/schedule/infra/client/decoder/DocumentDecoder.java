package com.training.schedule.infra.client.decoder;

import com.fasterxml.jackson.databind.ObjectMapper;
import feign.Response;
import feign.codec.ErrorDecoder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;

import java.io.IOException;
import java.io.InputStream;
import java.io.StringWriter;
import java.nio.charset.Charset;

import static org.apache.commons.io.IOUtils.copy;

@Slf4j
public class DocumentDecoder implements ErrorDecoder {

    private ErrorDecoder delegate = new ErrorDecoder.Default();

    @Autowired
    private ObjectMapper mapper = new ObjectMapper();

    @Override
    public Exception decode(String methodKey, Response response) {
        HttpStatus statusCode = HttpStatus.valueOf(response.status());
        String statusText = response.reason();
        String responseMessage;
        try (InputStream is = response.body().asInputStream()) {

            StringWriter writer = new StringWriter();
            copy(is, writer, String.valueOf(Charset.defaultCharset()));

            responseMessage = writer.toString();
        } catch (IOException e) {
            throw new RuntimeException("Failed to process response body", e);
        }
        if (response.status() == 400) {
            logErrorMessage(responseMessage);
//            return new BadRequestException(statusCode, responseMessage, null, null, null);
        }

        if (response.status() > 400 && response.status() <= 499) {
            logErrorMessage(responseMessage);
            return new HttpClientErrorException(statusCode, statusText, null, null);
        }

        if (response.status() >= 500 && response.status() <= 599) {
            logErrorMessage(responseMessage);
            return new HttpServerErrorException(statusCode, statusText, null, null, null);
        }
        return delegate.decode(methodKey, response);
    }

    public void logErrorMessage(final String message) {
        log.error("Failed to get response with [message = {}] ", message);
    }
}
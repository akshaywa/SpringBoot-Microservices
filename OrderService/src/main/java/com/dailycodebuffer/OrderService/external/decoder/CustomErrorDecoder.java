package com.dailycodebuffer.OrderService.external.decoder;

import com.dailycodebuffer.OrderService.model.ErrorResponse;
import com.dailycodebuffer.OrderService.exception.ProductServiceCustomException;
import com.fasterxml.jackson.databind.ObjectMapper;
import feign.Response;
import feign.codec.ErrorDecoder;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;



@Slf4j
public class CustomErrorDecoder implements ErrorDecoder {

    @Override
    public Exception decode(String s, Response response) {
        ObjectMapper objectMapper = new ObjectMapper();

        log.info("inside decode method in CustomErrorDecoder class. {} {}",
                response.request().url(),
                response.request().headers());

        try{
            ErrorResponse errorResponse = objectMapper.readValue(response.body().asInputStream(), ErrorResponse.class);
            throw new ProductServiceCustomException(errorResponse.getErrorMessage(), errorResponse.getErrorCode());
        } catch (IOException e) {
            throw new ProductServiceCustomException("Internal Server Error", "INTERNAL_SERVER_ERROR");
        }
    }
}

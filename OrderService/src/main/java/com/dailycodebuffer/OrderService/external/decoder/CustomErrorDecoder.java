package com.dailycodebuffer.OrderService.external.decoder;

import com.dailycodebuffer.OrderService.model.ErrorResponse;
import com.dailycodebuffer.OrderService.exception.ProductServiceCustomException;
import com.fasterxml.jackson.databind.ObjectMapper;
import feign.Response;
import feign.codec.ErrorDecoder;
import lombok.extern.log4j.Log4j2;

import java.io.IOException;



@Log4j2
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

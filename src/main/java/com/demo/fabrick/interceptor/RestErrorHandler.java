package com.demo.fabrick.interceptor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpMethod;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.web.client.ResponseErrorHandler;

import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.util.Scanner;

public class RestErrorHandler
        implements ResponseErrorHandler {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());


    @Override
    public boolean hasError(ClientHttpResponse clientHttpResponse)
            throws IOException {

        return (clientHttpResponse.getStatusCode().is4xxClientError()
                || clientHttpResponse.getStatusCode().is5xxServerError());
    }

    @Override
    public void handleError(ClientHttpResponse clientHttpResponse) {

    }

    @Override
    public void handleError(URI url,
                            HttpMethod method,
                            ClientHttpResponse response)
            throws IOException {

        String responseAsString = toString(response.getBody());

        logger.error("----------- START Response -----------");
        logger.error("Status code : {}", response.getStatusCode());
        logger.error("Status text : {}", response.getStatusText());
        logger.error("Headers :");
                    response.getHeaders().forEach((key, patterns) -> patterns.forEach(value -> logger.error(key + " : " + value)));
        logger.error("Body : \n{}", responseAsString);
        logger.error("----------- END Response -----------");

    }

    String toString(InputStream inputStream) {
        Scanner s = new Scanner(inputStream).useDelimiter("\\A");
        return s.hasNext() ? s.next() : "";
    }
}
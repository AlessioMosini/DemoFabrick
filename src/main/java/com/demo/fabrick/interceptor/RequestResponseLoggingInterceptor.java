package com.demo.fabrick.interceptor;

import com.demo.fabrick.utility.ServiceUtility;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpRequest;
import org.springframework.http.MediaType;
import org.springframework.http.client.ClientHttpRequestExecution;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.ClientHttpResponse;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.Collections;
import java.util.Scanner;

public class RequestResponseLoggingInterceptor implements ClientHttpRequestInterceptor {

    private final Logger log = LoggerFactory.getLogger(this.getClass());

    @Override
    public ClientHttpResponse intercept(HttpRequest request, byte[] body, ClientHttpRequestExecution execution) throws IOException {
        setHeaders(request);
        logRequest(request, body);
        ClientHttpResponse response = execution.execute(request, body);
        logResponse(response);
        return response;
    }

    private void logRequest(HttpRequest request, byte[] body) {
        log.info("----------- START Request -----------");
        log.info("Scheme : {}", request.getURI().getScheme());
        log.info("Server : {}", request.getURI().getHost());
        log.info("Path : {}", request.getURI().getPath());
        log.info("Method : {}", request.getMethod());
        log.info("Headers :");
        request.getHeaders().forEach((key, patterns) -> patterns.forEach(value -> log.info(key + " : " + value)));
        log.info("Body : \n{}", new String(body, StandardCharsets.UTF_8));
        log.info("----------- END Request -----------\n");
    }

    private void logResponse(ClientHttpResponse response) throws IOException {
        if (response.getStatusCode().is4xxClientError() || response.getStatusCode().is5xxServerError())
            return;
        log.info("----------- START Response -----------");
        log.info("Status code : {}", response.getStatusCode());
        log.info("Status text : {}", response.getStatusText());
        log.info("Headers :");
        response.getHeaders().forEach((key, patterns) -> patterns.forEach(value -> log.info(key + " : " + value)));
        log.info("Body : \n{}", toString(response.getBody()));
        log.info("----------- END Response -----------\n");
    }

    String toString(InputStream inputStream) {
        Scanner s = new Scanner(inputStream).useDelimiter("\\A");
        return s.hasNext() ? s.next() : "";
    }

    private void setHeaders(HttpRequest request){
        request.getHeaders().set(ServiceUtility.HEADER_AUTH, ServiceUtility.HEADER_AUTH_VALUE);
        request.getHeaders().set(ServiceUtility.HEADER_API_KEY, ServiceUtility.HEADER_API_KEY_VALUE);
        request.getHeaders().setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
        request.getHeaders().setContentType(MediaType.APPLICATION_JSON);
    }
}
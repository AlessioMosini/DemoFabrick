package com.demo.fabrick.service;

import com.demo.fabrick.interceptor.RequestResponseLoggingInterceptor;
import com.demo.fabrick.interceptor.RestErrorHandler;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.BufferingClientHttpRequestFactory;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;

@Service
public abstract class HttpActionService {

    final RestTemplate restTemplate;

    @Value("${server}")
    private String server;

    protected HttpActionService() {
        this.restTemplate = new RestTemplate(new BufferingClientHttpRequestFactory(new SimpleClientHttpRequestFactory()));
        this.restTemplate.setErrorHandler(new RestErrorHandler());

        List<ClientHttpRequestInterceptor> interceptors =
                CollectionUtils.isEmpty(restTemplate.getInterceptors()) ?
                        new ArrayList<>() :
                        restTemplate.getInterceptors();
        interceptors.add(new RequestResponseLoggingInterceptor());
        this.restTemplate.setInterceptors(interceptors);
    }

    private <T> ResponseEntity<T> exec(String url, HttpMethod httpMethod, HttpEntity<?> httpEntity, ParameterizedTypeReference<T> responseClass, Object... uriVariables){
        return restTemplate.exchange(
                server + url,
                        httpMethod,
                        httpEntity,
                        responseClass,
                        uriVariables
                );
    }

    // generic call for get api
    public <T> ResponseEntity<T> execGet(String url, HttpEntity<?> httpEntity, ParameterizedTypeReference<T> responseClass, Object... uriVariables){
        return exec(url, HttpMethod.GET, httpEntity, responseClass, uriVariables);
    }

    // generic call for post api
    public <T> ResponseEntity<T> execPost(String url, HttpEntity<?> httpEntity, ParameterizedTypeReference<T> responseClass, Object... uriVariables){
        return exec(url, HttpMethod.POST, httpEntity, responseClass, uriVariables);
    }
}

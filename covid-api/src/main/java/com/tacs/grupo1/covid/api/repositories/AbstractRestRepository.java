package com.tacs.grupo1.covid.api.repositories;

import com.tacs.grupo1.covid.api.exceptions.RestRepositoryException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.*;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.time.Duration;

@Slf4j
public abstract class AbstractRestRepository {

    protected RestTemplate restClient;

    @Autowired
    public AbstractRestRepository(
            RestTemplateBuilder restBuilder
    ) {
        this.restClient = restBuilder
                .setConnectTimeout(Duration.ofSeconds(10))
                .setReadTimeout(Duration.ofSeconds(10))
                .build();
    }

    protected <T> ResponseEntity<T> get(String uri, Class<T> responseType) {
        Integer totalTimeoutMs = 60000; //if a GET takes more than a minute, throw an exception
        Integer counterTimeout = 0;
        Integer sleepTime = 500;

        ResponseEntity<T> response = this.actualGet(uri, responseType, sleepTime);
        while (response == null && counterTimeout < 60000L){
            counterTimeout = counterTimeout + sleepTime;
            response = this.actualGet(uri, responseType, sleepTime);
        }

        if (totalTimeoutMs > 60000L){
            throw new RestRepositoryException("GET TIMEOUT");
        }

        if (response.getStatusCode().is2xxSuccessful() || response.getStatusCode().is3xxRedirection()) {
            return response;
        } else {
            log.error("Error response", response);
            throw new RestRepositoryException("HTTP Status Code Not Successful while doing GET");
        }
    }

    private <T> ResponseEntity<T> actualGet(String uri, Class<T> responseType, Integer sleepTime){
        HttpHeaders headers = new HttpHeaders();
        headers.set("accept", MediaType.APPLICATION_JSON_VALUE);
        HttpEntity<?> entity = new HttpEntity<>(headers);
        ResponseEntity<T> response;

        try {
            response = restClient.exchange(
                    uri,
                    HttpMethod.GET,
                    entity,
                    responseType);
        } catch (org.springframework.web.client.ResourceAccessException e) {
            log.error("Falló el request HTTP a la URI: " + uri + ", intentando nuevamente...");
            try {
                Thread.sleep(sleepTime);
            } catch (InterruptedException f) {
                f.printStackTrace();
            }
            return null;
        }
        return response;
    }

    protected UriComponentsBuilder getUriBuilder(String uri) {
        return UriComponentsBuilder.fromHttpUrl(uri);
    }

    protected boolean isOkHttpStatus(int status) {
        return status > 199 && status < 300;
    }
}

package com.mile.portal.config.restAdapter;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class AsyncWebClient {
    private WebClientConfig webClientConfig;
    private String url;

    private void setUrl(String url) {
        this.url = url;
    }
}

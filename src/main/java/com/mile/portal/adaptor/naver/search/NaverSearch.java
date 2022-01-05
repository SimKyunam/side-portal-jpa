package com.mile.portal.adaptor.naver.search;

import com.mile.portal.adaptor.naver.search.dto.SearchImageRes;
import com.mile.portal.adaptor.naver.search.dto.SearchLocalRes;
import com.mile.portal.adaptor.naver.search.dto.SearchReq;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;

@Component
@RequiredArgsConstructor
@Slf4j
public class NaverSearch {
    private final RestTemplate retryableRestTemplate;

    @Value("${naver.client.id}")
    private String naverClientId;

    @Value("${naver.client.secret}")
    private String naverClientSecret;

    @Value("${naver.url.search.local}")
    private String naverLocalSearchUrl;

    @Value("${naver.url.search.image}")
    private String naverImageSearchUrl;

    public SearchLocalRes searchLocal(SearchReq.SearchLocalReq searchLocalReq) {
        URI uri = UriComponentsBuilder.fromUriString(naverLocalSearchUrl)
                .queryParams(searchLocalReq.toMultiValueMap())
                .build()
                .encode()
                .toUri();

        HttpEntity httpEntity = new HttpEntity<>(naverCreateHeader());
        ParameterizedTypeReference<SearchLocalRes> responseType = new ParameterizedTypeReference<SearchLocalRes>() {
        };

        ResponseEntity<SearchLocalRes> responseEntity = retryableRestTemplate.exchange(
                uri,
                HttpMethod.GET,
                httpEntity,
                responseType
        );

        return responseEntity.getBody();
    }

    public SearchImageRes searchImage(SearchReq.SearchImageReq searchImageReq) {
        URI uri = UriComponentsBuilder.fromUriString(naverImageSearchUrl)
                .queryParams(searchImageReq.toMultiValueMap())
                .build()
                .encode()
                .toUri();

        HttpEntity httpEntity = new HttpEntity<>(naverCreateHeader());
        ParameterizedTypeReference<SearchImageRes> responseType = new ParameterizedTypeReference<SearchImageRes>() {
        };

        ResponseEntity<SearchImageRes> responseEntity = retryableRestTemplate.exchange(
                uri,
                HttpMethod.GET,
                httpEntity,
                responseType
        );

        return responseEntity.getBody();
    }

    public HttpHeaders naverCreateHeader() {
        HttpHeaders headers = new HttpHeaders();
        headers.set("X-Naver-Client-Id", naverClientId);
        headers.set("X-Naver-Client-Secret", naverClientSecret);
        headers.setContentType(MediaType.APPLICATION_JSON);

        return headers;
    }
}

package com.mile.portal.adaptor.naver.search;

import com.mile.portal.adaptor.naver.search.dto.SearchImageRes;
import com.mile.portal.adaptor.naver.search.dto.SearchLocalRes;
import com.mile.portal.adaptor.naver.search.dto.SearchReq;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.reactive.function.client.WebClient;

@Component
@RequiredArgsConstructor
@Slf4j
public class NaverSearch {
    private final WebClient webClient;

    @Value("${naver.client.id}")
    private String naverClientId;

    @Value("${naver.client.secret}")
    private String naverClientSecret;

    @Value("${naver.url.search.local}")
    private String naverLocalSearchUrl;

    @Value("${naver.url.search.image}")
    private String naverImageSearchUrl;

    public SearchLocalRes searchLocal(SearchReq.SearchLocalReq searchLocalReq) {
        ParameterizedTypeReference<SearchLocalRes> responseType = new ParameterizedTypeReference<SearchLocalRes>() {
        };

        return webClient.get()
                .uri(naverLocalSearchUrl, uriBuilder -> uriBuilder.queryParams(searchLocalReq.toMultiValueMap()).build())
                .accept(MediaType.APPLICATION_JSON)
                .headers(headers -> {
                    headers.addAll(naverCreateHeader());
                })
                .retrieve()
                .bodyToFlux(responseType)
                .blockFirst();
    }

    public SearchImageRes searchImage(SearchReq.SearchImageReq searchImageReq) {
        ParameterizedTypeReference<SearchImageRes> responseType = new ParameterizedTypeReference<SearchImageRes>() {
        };

        return webClient.get()
                .uri(naverImageSearchUrl, uriBuilder -> uriBuilder.queryParams(searchImageReq.toMultiValueMap()).build())
                .accept(MediaType.APPLICATION_JSON)
                .headers(headers -> {
                    headers.addAll(naverCreateHeader());
                })
                .retrieve()
                .bodyToFlux(responseType)
                .blockFirst();
    }

    public MultiValueMap<String, String> naverCreateHeader() {
        MultiValueMap<String, String> headerMap = new LinkedMultiValueMap<>();
        headerMap.set("X-Naver-Client-Id", naverClientId);
        headerMap.set("X-Naver-Client-Secret", naverClientSecret);

        return headerMap;
    }
}

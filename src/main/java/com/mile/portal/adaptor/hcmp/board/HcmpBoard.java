package com.mile.portal.adaptor.hcmp.board;

import com.mile.portal.adaptor.hcmp.board.dto.HcmpBoardReq;
import com.mile.portal.adaptor.hcmp.board.dto.HcmpBoardRes;
import com.mile.portal.adaptor.hcmp.board.dto.comm.ResBody;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.List;

@Component
@RequiredArgsConstructor
@Slf4j
public class HcmpBoard {
    private final WebClient webClient;

    private final RestTemplate retryableRestTemplate;

    @Value("${hcmp.url}")
    private String hcmpUrl;

    public List<HcmpBoardRes.BoardNotice> getBoardNotices() {
        ParameterizedTypeReference<ResBody<List<HcmpBoardRes.BoardNotice>>> responseType = new ParameterizedTypeReference<ResBody<List<HcmpBoardRes.BoardNotice>>>() {
        };

        ResBody<List<HcmpBoardRes.BoardNotice>> listResBody = webClient.mutate()
                .baseUrl(hcmpUrl)
                .build()
                .post()
                .uri("/api/v1/board/getBoardNotices")
                .accept(MediaType.APPLICATION_JSON)
                .headers(httpHeaders -> {
                    httpHeaders.addAll(createHeader("c50cbb0b080d4f149820faromRoot", "user", "44"));
                })
                .retrieve()
                .bodyToFlux(responseType)
                .blockFirst();

        return listResBody.getData();

//        URI uri = UriComponentsBuilder.fromUriString(hcmpUrl + "/api/v1/board/getBoardNotices")
//                .build()
//                .encode()
//                .toUri();
//
//        HttpHeaders headers = new HttpHeaders();
//        headers.set("Authorization", "Bearer c50cbb0b080d4f149820faromRoot");
//        headers.set("Calling", "user");
//        headers.set("MenuId", "44");
//        headers.setContentType(MediaType.APPLICATION_JSON);
//
//        HttpEntity httpEntity = new HttpEntity<>(headers);
//        ParameterizedTypeReference<ResBody<List<HcmpBoardRes.BoardNotice>>> responseType = new ParameterizedTypeReference<ResBody<List<HcmpBoardRes.BoardNotice>>>() {
//        };
//
//        ResponseEntity<ResBody<List<HcmpBoardRes.BoardNotice>>> responseEntity = retryableRestTemplate.exchange(
//                uri,
//                HttpMethod.GET,
//                httpEntity,
//                responseType
//        );
//        ResBody<List<HcmpBoardRes.BoardNotice>> body = responseEntity.getBody();
//
//        return body.getData();
    }

    public HcmpBoardRes.BoardNotice getBoardNoticeDetail(int boardId) {
        URI uri = UriComponentsBuilder.fromUriString(hcmpUrl + "/api/v1/board/getBoardNoticeDetail")
                .queryParam("boardId", boardId)
                .build()
                .encode()
                .toUri();

        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer c50cbb0b080d4f149820faromRoot");
        headers.set("Calling", "user");
        headers.set("MenuId", "44");
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity httpEntity = new HttpEntity<>(headers);
        ParameterizedTypeReference<ResBody<HcmpBoardRes.BoardNotice>> responseType = new ParameterizedTypeReference<ResBody<HcmpBoardRes.BoardNotice>>() {
        };

        ResponseEntity<ResBody<HcmpBoardRes.BoardNotice>> responseEntity = retryableRestTemplate.exchange(
                uri,
                HttpMethod.GET,
                httpEntity,
                responseType
        );
        ResBody<HcmpBoardRes.BoardNotice> body = responseEntity.getBody();

        return body.getData();
    }

    public String createBoardQna(HcmpBoardReq.BoardQna boardQna) {
        URI uri = UriComponentsBuilder.fromUriString(hcmpUrl + "/api/v1/board/createBoardQna")
                .build()
                .encode()
                .toUri();

        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer c50cbb0b080d4f149820faromRoot");
        headers.set("Calling", "user");
        headers.set("MenuId", "44");
        headers.setContentType(MediaType.MULTIPART_FORM_DATA);

        HttpEntity<MultiValueMap<String, Object>> httpEntity = new HttpEntity<>(boardQna.toMultiValueMap(), headers);

        ResponseEntity<String> exchange = retryableRestTemplate.exchange(uri, HttpMethod.POST, httpEntity, String.class);

        return exchange.getBody();
    }

    public MultiValueMap<String, String> createHeader(String authorization, String calling, String menuId) {
        MultiValueMap<String, String> headerMap = new LinkedMultiValueMap<>();
        headerMap.set("Authorization", "Bearer " + authorization);
        headerMap.set("Calling", calling);
        headerMap.set("MenuId", menuId);

        return headerMap;
    }
}

package com.mile.portal.adaptor.hcmp.board;

import com.mile.portal.adaptor.hcmp.board.dto.HcmpBoardRes;
import com.mile.portal.adaptor.hcmp.board.dto.comm.ResBody;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.List;

@Component
@RequiredArgsConstructor
@Slf4j
public class HcmpBoard {
    private final RestTemplate retryableRestTemplate;

    @Value("${hcmp.url}")
    private String hcmpUrl;

    public List<HcmpBoardRes.BoardNotice> getBoardNotices() {
        URI uri = UriComponentsBuilder.fromUriString(hcmpUrl + "/api/v1/board/getBoardNotices")
                .build()
                .encode()
                .toUri();

        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer c50cbb0b080d4f149820faromRoot");
        headers.set("Calling", "user");
        headers.set("MenuId", "44");
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity httpEntity = new HttpEntity<>(headers);
        ParameterizedTypeReference<ResBody<List<HcmpBoardRes.BoardNotice>>> responseType = new ParameterizedTypeReference<ResBody<List<HcmpBoardRes.BoardNotice>>>() {
        };

        ResponseEntity<ResBody<List<HcmpBoardRes.BoardNotice>>> responseEntity = retryableRestTemplate.exchange(
                uri,
                HttpMethod.GET,
                httpEntity,
                responseType
        );
        ResBody<List<HcmpBoardRes.BoardNotice>> body = responseEntity.getBody();

        return body.getData();
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


}

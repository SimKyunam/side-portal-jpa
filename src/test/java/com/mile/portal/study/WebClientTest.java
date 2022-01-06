package com.mile.portal.study;

import com.mile.portal.adaptor.hcmp.board.dto.HcmpBoardReq;
import com.mile.portal.adaptor.hcmp.board.dto.HcmpBoardRes;
import com.mile.portal.adaptor.hcmp.board.dto.comm.ResBody;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.MediaType;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.List;

@SpringBootTest
public class WebClientTest {

    @Value("${hcmp.url}")
    private String hcmpUrl;

    @Autowired
    private WebClient webClient;

    @Test
    @DisplayName("1. 게시판 등록")
    void test1() {
        HcmpBoardReq.BoardQna boardQna = HcmpBoardReq.BoardQna.builder()
                .qnaType("LGN")
                .title("게게시시판판")
                .content("내내요용")
                .build();

        LinkedMultiValueMap<String, String> headerMap = new LinkedMultiValueMap<>();
        headerMap.set("Authorization", "Bearer c50cbb0b080d4f149820faromRoot");
        headerMap.set("Calling", "user");
        headerMap.set("MenuId", "44");

        String result = webClient.mutate()
                .baseUrl(hcmpUrl)
                .build()
                .post()
                .uri("/api/v1/board/createBoardQna")
                .contentType(MediaType.MULTIPART_FORM_DATA)
                .headers(httpHeaders -> {
                    httpHeaders.addAll(headerMap);
                })
                .body(BodyInserters.fromMultipartData(boardQna.toMultiValueMap()))
                .retrieve()
                .bodyToMono(String.class)
                .flux()
                .toStream()
                .findFirst()
                .orElse(null);

        System.out.println(result);
    }

    @Test
    @DisplayName("2. 조회")
    void test2() {
        LinkedMultiValueMap<String, String> headerMap = new LinkedMultiValueMap<>();
        headerMap.set("Authorization", "Bearer c50cbb0b080d4f149820faromRoot");
        headerMap.set("Calling", "user");
        headerMap.set("MenuId", "44");

        ParameterizedTypeReference<ResBody<List<HcmpBoardRes.BoardNotice>>> responseType = new ParameterizedTypeReference<ResBody<List<HcmpBoardRes.BoardNotice>>>() {
        };

        ResBody<List<HcmpBoardRes.BoardNotice>> listResBody = webClient.mutate()
                .baseUrl(hcmpUrl)
                .build()
                .get()
                .uri("/api/v1/board/getBoardNotices")
                .accept(MediaType.APPLICATION_JSON)
                .headers(httpHeaders -> {
                    httpHeaders.addAll(headerMap);
                })
                .retrieve()
                .bodyToMono(responseType)
                .flux()
                .toStream()
                .findFirst()
                .orElse(null);

        System.out.println(listResBody);
    }
}

package com.mile.portal.study;

import com.mile.portal.adaptor.hcmp.board.dto.HcmpBoardReq;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.web.reactive.function.client.WebClient;

@SpringBootTest
public class WebClientTest {

    @Value("${hcmp.url}")
    private String hcmpUrl;

    @Autowired
    private WebClient webClient;

    @Test
    @DisplayName("1. 게시판 조회 - 비동기")
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
                .accept(MediaType.APPLICATION_JSON)
                .headers(httpHeaders -> {
                    httpHeaders.addAll(headerMap);
                })
                .bodyValue(boardQna.toMultiValueMap())
                .retrieve()
                .bodyToMono(String.class)
                .flux()
                .toStream()
                .findFirst()
                .orElse(null);

        System.out.println(result);
    }
}

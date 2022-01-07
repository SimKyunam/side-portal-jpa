package com.mile.portal.study;

import com.mile.portal.adaptor.hcmp.board.dto.HcmpBoardRes;
import com.mile.portal.adaptor.hcmp.board.dto.comm.ResBody;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.MediaType;
import org.springframework.http.client.MultipartBodyBuilder;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@SpringBootTest
public class WebClientTest {

    @Value("${hcmp.url}")
    private String hcmpUrl;

    @Autowired
    private WebClient webClient;

    @Test
    @DisplayName("1. 게시판 등록 - bodyMono (동기)")
    void test1() {
        MultipartBodyBuilder builder = new MultipartBodyBuilder();
        builder.part("qnaType", "LGN");
        builder.part("title", "게게시시파판");
        builder.part("content", "내내요용");
        MultiValueMap<String, HttpEntity<?>> parts = builder.build();

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
                .body(BodyInserters.fromMultipartData(parts))
                .retrieve()
                .bodyToMono(String.class)
                .flux()
                .toStream()
                .findFirst()
                .orElse(null);

        System.out.println(result);
    }

    @Test
    @DisplayName("2. 조회 - bodyMono (동기)")
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

    @DisplayName("3. 조회 - bodyToFlux (동기)")
    @Test
    void test_3() {
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
                .bodyToFlux(responseType)
                .blockFirst();

        System.out.println(listResBody);
    }

    @DisplayName("4. 비동기 처리")
    @Test
    void test_4() {
        LinkedMultiValueMap<String, String> headerMap = new LinkedMultiValueMap<>();
        headerMap.set("Authorization", "Bearer c50cbb0b080d4f149820faromRoot");
        headerMap.set("Calling", "user");
        headerMap.set("MenuId", "44");

        webClient.mutate()
                .baseUrl(hcmpUrl)
                .build()
                .get()
                .uri("/api/v1/board/getBoardNotices")
                .accept(MediaType.APPLICATION_JSON)
                .headers(httpHeaders -> {
                    httpHeaders.addAll(headerMap);
                })
                .retrieve()
                .bodyToMono(String.class)
                .subscribe(System.out::println);

        webClient.mutate()
                .baseUrl(hcmpUrl)
                .build()
                .get()
                .uri("/api/v1/board/getBoardQnAs")
                .accept(MediaType.APPLICATION_JSON)
                .headers(httpHeaders -> {
                    httpHeaders.addAll(headerMap);
                })
                .retrieve()
                .bodyToMono(String.class)
                .subscribe(System.out::println);

        try {
            Thread.sleep(6000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @DisplayName("5. 비동기 블락킹")
    @Test
    void test_5() {
        LinkedMultiValueMap<String, String> headerMap = new LinkedMultiValueMap<>();
        headerMap.set("Authorization", "Bearer c50cbb0b080d4f149820faromRoot");
        headerMap.set("Calling", "user");
        headerMap.set("MenuId", "44");

        Mono<String> boardNotice = webClient.mutate()
                .baseUrl(hcmpUrl)
                .build()
                .get()
                .uri("/api/v1/board/getBoardNotices")
                .accept(MediaType.APPLICATION_JSON)
                .headers(httpHeaders -> {
                    httpHeaders.addAll(headerMap);
                })
                .retrieve()
                .bodyToMono(String.class);

        Mono<String> boardQnas = webClient.mutate()
                .baseUrl(hcmpUrl)
                .build()
                .get()
                .uri("/api/v1/board/getBoardQnAs")
                .accept(MediaType.APPLICATION_JSON)
                .headers(httpHeaders -> {
                    httpHeaders.addAll(headerMap);
                })
                .retrieve()
                .bodyToMono(String.class);

        Map<String, String> block = Mono.zip(boardNotice, boardQnas, (notice, qna) -> {
            Map<String, String> map = new LinkedHashMap<>();
            map.put("notice", notice);
            map.put("qna", qna);
            return map;
        }).block();

        System.out.println(block.get("notice"));
        System.out.println(block.get("qna"));
    }
}

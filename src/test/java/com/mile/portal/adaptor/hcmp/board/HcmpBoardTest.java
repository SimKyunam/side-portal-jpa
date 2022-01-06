package com.mile.portal.adaptor.hcmp.board;

import com.mile.portal.adaptor.hcmp.board.dto.HcmpBoardReq;
import io.netty.channel.ChannelOption;
import io.netty.handler.timeout.ReadTimeoutHandler;
import io.netty.handler.timeout.WriteTimeoutHandler;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpMethod;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.netty.http.client.HttpClient;

import java.time.Duration;
import java.util.concurrent.TimeUnit;

@SpringBootTest
class HcmpBoardTest {

    @Value("${hcmp.url}")
    private String hcmpUrl;

    @Autowired
    private HcmpBoard hcmpBoard;

    @Test
    @DisplayName("1. 게시판 조회")
    void test1() {
        System.out.println(hcmpBoard.getBoardNotices());
    }

    @Test
    @DisplayName("2. 게시판 상세조회")
    void test2() {
        System.out.println(hcmpBoard.getBoardNoticeDetail(327));
    }

    @Test
    @DisplayName("3. 게시판 등록")
    void test3() {
        HcmpBoardReq.BoardQna boardQna = HcmpBoardReq.BoardQna.builder()
                .qnaType("LGN")
                .title("게게시시판판")
                .content("내내요용")
                .build();

        hcmpBoard.createBoardQna(boardQna);
    }

    @Test
    @DisplayName("4. 게시판 조회 - webClient")
    void test4() {
        HttpClient httpClient = HttpClient.create()
                .option(ChannelOption.CONNECT_TIMEOUT_MILLIS, 5000)
                .responseTimeout(Duration.ofMillis(5000))
                .doOnConnected(conn ->
                        conn.addHandlerLast(new ReadTimeoutHandler(5000, TimeUnit.MILLISECONDS))
                                .addHandlerLast(new WriteTimeoutHandler(5000, TimeUnit.MILLISECONDS)));

        WebClient client = WebClient.builder()
                .clientConnector(new ReactorClientHttpConnector(httpClient))
                .build();

        WebClient.RequestBodyUriSpec uriSpec = client.method(HttpMethod.POST);
        WebClient.RequestBodySpec bodySpec = uriSpec.uri(hcmpUrl + "/api/v1/board/createBoardQna");

    }

}
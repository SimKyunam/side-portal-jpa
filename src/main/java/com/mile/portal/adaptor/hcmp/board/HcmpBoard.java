package com.mile.portal.adaptor.hcmp.board;

import com.mile.portal.adaptor.hcmp.board.dto.HcmpBoardReq;
import com.mile.portal.adaptor.hcmp.board.dto.HcmpBoardRes;
import com.mile.portal.adaptor.hcmp.board.dto.comm.ResBody;
import com.mile.portal.util.CommonUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.List;

@Component
@RequiredArgsConstructor
@Slf4j
public class HcmpBoard {
    private final WebClient webClient;

    @Value("${hcmp.url}")
    private String hcmpUrl;

    public List<HcmpBoardRes.BoardNotice> getBoardNotices() {
        ParameterizedTypeReference<ResBody<List<HcmpBoardRes.BoardNotice>>> responseType = new ParameterizedTypeReference<ResBody<List<HcmpBoardRes.BoardNotice>>>() {
        };

        ResBody<List<HcmpBoardRes.BoardNotice>> listBoardNotice = webClient.get()
                .uri(hcmpUrl + "/api/v1/board/getBoardNotices")
                .accept(MediaType.APPLICATION_JSON)
                .headers(httpHeaders -> {
                    httpHeaders.addAll(CommonUtil.createHeaderByHcmp("c50cbb0b080d4f149820faromRoot", "user", "44"));
                })
                .retrieve()
                .bodyToFlux(responseType)
                .blockFirst();

        return listBoardNotice != null ? listBoardNotice.getData() : null;
    }

    public HcmpBoardRes.BoardNotice getBoardNoticeDetail(int boardId) {
        ParameterizedTypeReference<ResBody<HcmpBoardRes.BoardNotice>> responseType = new ParameterizedTypeReference<ResBody<HcmpBoardRes.BoardNotice>>() {
        };

        ResBody<HcmpBoardRes.BoardNotice> boardNotice = webClient.get()
                .uri(hcmpUrl + "/api/v1/board/getBoardNoticeDetail",
                        uriBuilder -> uriBuilder.queryParam("boardId", boardId).build())
                .accept(MediaType.APPLICATION_JSON)
                .headers(httpHeaders -> {
                    httpHeaders.addAll(CommonUtil.createHeaderByHcmp("c50cbb0b080d4f149820faromRoot", "user", "44"));
                })
                .retrieve()
                .bodyToFlux(responseType)
                .blockFirst();

        return boardNotice != null ? boardNotice.getData() : null;
    }

    public String createBoardQna(HcmpBoardReq.BoardQna boardQna) {
        return webClient.post()
                .uri(hcmpUrl + "/api/v1/board/createBoardQna")
                .contentType(MediaType.MULTIPART_FORM_DATA)
                .accept(MediaType.APPLICATION_JSON)
                .headers(httpHeaders -> {
                    httpHeaders.addAll(CommonUtil.createHeaderByHcmp("c50cbb0b080d4f149820faromRoot", "user", "44"));
                })
                .body(BodyInserters.fromMultipartData(boardQna.toMultiValueMap()))
                .retrieve()
                .bodyToFlux(String.class)
                .blockFirst();
    }
}

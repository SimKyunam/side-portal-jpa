package com.mile.portal.adaptor.naver.search;

import com.mile.portal.adaptor.naver.search.dto.SearchImageRes;
import com.mile.portal.adaptor.naver.search.dto.SearchLocalRes;
import com.mile.portal.adaptor.naver.search.dto.SearchReq;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class NaverSearchTest {

    @Autowired
    private NaverSearch naverSearch;

    @Test
    @DisplayName("1. 지역 조회")
    void test1() {
        SearchReq.SearchLocalReq search = new SearchReq.SearchLocalReq();
        search.setQuery("갈비집");

        SearchLocalRes result = naverSearch.searchLocal(search);

        System.out.println(result);
        Assertions.assertNotNull(result.getItems().stream().findFirst().get().getCategory());
    }

    @Test
    @DisplayName("2. 이미지 조회")
    void test2() {
        SearchReq.SearchImageReq search = new SearchReq.SearchImageReq();
        search.setQuery("갈비집");

        SearchImageRes result = naverSearch.searchImage(search);

        System.out.println(result);
    }
}
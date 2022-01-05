package com.mile.portal.adaptor.naver.search.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

public class SearchReq {

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class SearchLocalReq {

        private String query = "";
        private int display = 1;
        private int start = 1;
        private String sort = "random";

        public MultiValueMap<String, String> toMultiValueMap() {
            LinkedMultiValueMap<String, String> map = new LinkedMultiValueMap<>();

            map.add("query", query);
            map.add("display", String.valueOf(display));
            map.add("start", String.valueOf(start));
            map.add("sort", sort);

            return map;
        }
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class SearchImageReq {

        private String query = "";
        private int display = 1;
        private int start = 1;
        private String sort = "sim";
        private String filter = "all";

        public MultiValueMap<String, String> toMultiValueMap() {
            LinkedMultiValueMap<String, String> map = new LinkedMultiValueMap<>();

            map.add("query", query);
            map.add("display", String.valueOf(display));
            map.add("start", String.valueOf(start));
            map.add("sort", sort);
            map.add("filter", filter);

            return map;
        }
    }
}

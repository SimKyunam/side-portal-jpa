package com.mile.portal.rest.mng.controller;

import com.mile.portal.rest.mng.service.MngQnaService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/mng/qna")
public class MngQnaController {
    private final MngQnaService mngQnaService;
}

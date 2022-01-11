package com.mile.portal.rest.mng.service;

import com.mile.portal.rest.mng.repository.ManagerQnaRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class MngQnaService {
    private final ManagerQnaRepository managerQnaRepository;
}

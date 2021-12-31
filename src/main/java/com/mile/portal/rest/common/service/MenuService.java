package com.mile.portal.rest.common.service;

import com.mile.portal.config.cache.CacheProperties;
import com.mile.portal.config.exception.exceptions.ResultNotFoundException;
import com.mile.portal.rest.common.model.comm.ReqCommon;
import com.mile.portal.rest.common.model.domain.Code;
import com.mile.portal.rest.common.model.domain.Menu;
import com.mile.portal.rest.common.model.dto.CodeDto;
import com.mile.portal.rest.common.repository.MenuRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class MenuService {

    private final MenuRepository menuRepository;

    @PersistenceContext
    private EntityManager entityManager;

    @Transactional(readOnly = true)
    @Cacheable(value = CacheProperties.MENU)
    public List<Menu> listMenu() {
        return menuRepository.findTreeAll();
    }

    @Transactional(readOnly = true)
    @Cacheable(value = CacheProperties.MENU, key = "#codeId.concat(':').concat(#childCode)", unless = "#result == null")
    public Code selectMenu(String codeId, String childCode) {
        return menuRepository.findTreeCode(codeId, childCode);
    }

    @CacheEvict(value = CacheProperties.MENU, allEntries = true)
    public Code createMenu(ReqCommon.Code reqCode) {
        int depth = 1, ord = 1;
        String parentId = reqCode.getParentId();
        String codeId = reqCode.getCodeId();
        Code parentCode = null;

        if (parentId != null) {
            CodeDto parent = Optional.ofNullable(menuRepository.findParentCode(parentId, codeId)).orElseThrow(ResultNotFoundException::new);

            depth = parent.getDepth() + 1;
            ord = parent.getChildCount().intValue() + 1;

            parentCode = Code.builder()
                    .code(parent.getCode())
                    .codeValue(parent.getCodeValue())
                    .codeName(parent.getCodeName())
                    .depth(parent.getDepth())
                    .ord(parent.getOrd())
                    .build();
        } else {
            long parentCnt = menuRepository.countByParentIsNullAndCodeNot(codeId);
            ord = (int) parentCnt + 1;
        }

        Code code = Code.builder()
                .code(reqCode.getCodeId())
                .codeName(reqCode.getCodeName())
                .codeValue(reqCode.getCodeValue())
                .depth(depth)
                .ord(ord)
                .parent(parentCode)
                .build();

        entityManager.persist(code);
        return code;
    }

    @CacheEvict(value = CacheProperties.MENU, allEntries = true)
    public Code updateMenu(ReqCommon.Code reqCode) {
        String codeName = reqCode.getCodeName();
        String codeValue = reqCode.getCodeValue();
        String codeId = reqCode.getCodeId();

        Code code = menuRepository.findById(codeId).orElseThrow(ResultNotFoundException::new);
        code.setCodeName(codeName);
        code.setCodeValue(codeValue);

        return menuRepository.save(code);
    }

    @CacheEvict(value = CacheProperties.MENU, allEntries = true)
    public void deleteMenu(String codeId) {
        menuRepository.deleteById(codeId);
    }
}

package com.mile.portal.rest.common.service;

import com.mile.portal.config.cache.CacheProperties;
import com.mile.portal.config.exception.exceptions.ResultNotFoundException;
import com.mile.portal.rest.common.model.comm.ReqCommon;
import com.mile.portal.rest.common.model.domain.Menu;
import com.mile.portal.rest.common.model.dto.MenuDto;
import com.mile.portal.rest.common.repository.MenuRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.time.LocalDateTime;
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
    @Cacheable(value = CacheProperties.MENU, unless = "#result == null")
    public List<Menu> listMenu() {
        return menuRepository.findMenuAll();
    }

    @Transactional(readOnly = true)
    @Cacheable(value = CacheProperties.MENU, key = "#menuId + ':' + #childMenuId", unless = "#result == null")
    public Menu selectMenu(Long menuId, Long childMenuId) {
        return menuRepository.findMenuDetail(menuId, childMenuId);
    }

    @CacheEvict(value = CacheProperties.MENU, allEntries = true)
    public Menu createMenu(ReqCommon.Menu reqMenu) {
        int depth = 1, ord = 1;
        Long parentId = reqMenu.getParentId();
        Menu parentMenu = null;

        if (parentId != null) {
            MenuDto parent = Optional.ofNullable(menuRepository.findParentMenu(parentId)).orElseThrow(ResultNotFoundException::new);

            depth = parent.getDepth() + 1;
            ord = parent.getChildCount().intValue() + 1;

            parentMenu = Menu.builder()
                    .id(parent.getId())
                    .menuName(parent.getMenuName())
                    .menuValue(parent.getMenuValue())
                    .depth(parent.getDepth())
                    .ord(parent.getOrd())
                    .build();
        } else {
            long parentCnt = menuRepository.countByParentIsNull();
            ord = (int) parentCnt + 1;
        }

        Menu code = Menu.builder()
                .id(reqMenu.getMenuId())
                .menuName(reqMenu.getMenuName())
                .menuValue(reqMenu.getMenuValue())
                .depth(depth)
                .ord(ord)
                .parent(parentMenu)
                .build();

        entityManager.persist(code);
        return code;
    }

    @CacheEvict(value = CacheProperties.MENU, allEntries = true)
    public Menu updateMenu(ReqCommon.Menu reqMenu) {
        Long menuId = reqMenu.getMenuId();
        String menuName = reqMenu.getMenuName();
        String menuValue = reqMenu.getMenuValue();

        Menu menu = menuRepository.findById(menuId).orElseThrow(ResultNotFoundException::new);
        menu.setMenuName(menuName);
        menu.setMenuValue(menuValue);

        return menuRepository.save(menu);
    }

    @CacheEvict(value = CacheProperties.MENU, allEntries = true)
    public void deleteMenu(Long menuId) {
        Menu menu = menuRepository.findById(menuId).orElseThrow(ResultNotFoundException::new);
        menu.setDeleted(LocalDateTime.now());
    }
}

package com.mile.portal.rest.common.controller;

import com.mile.portal.rest.common.model.comm.ReqCommon;
import com.mile.portal.rest.common.model.comm.ResBody;
import com.mile.portal.rest.common.model.domain.Menu;
import com.mile.portal.rest.common.service.MenuService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/v1/common")
@RequiredArgsConstructor
public class MenuController {

    private final MenuService menuService;

    @GetMapping("/menu")
    public ResBody listMenu() {
        List<Menu> menuList = menuService.listMenu();
        return new ResBody(ResBody.CODE_SUCCESS, "", menuList);
    }

    @GetMapping("/menu/{menuId}")
    public ResBody selectMenu(@PathVariable Long menuId,
                              @RequestParam(required = false, defaultValue = "") Long childMenuId) {

        Menu menuList = menuService.selectMenu(menuId, childMenuId);
        return new ResBody(ResBody.CODE_SUCCESS, "", menuList);
    }

    @PostMapping("/menu/create")
    public ResBody createMenu(@Valid @RequestBody ReqCommon.Menu reqMenu) {
        Menu menu = menuService.createMenu(reqMenu);
        return new ResBody(ResBody.CODE_SUCCESS, "", null);
    }

    @PutMapping("/menu/update")
    public ResBody updateMenu(@Valid @RequestBody ReqCommon.Menu reqMenu) {
        Menu menu = menuService.updateMenu(reqMenu);
        return new ResBody(ResBody.CODE_SUCCESS, "", null);
    }

    @DeleteMapping("/menu/{menuId}")
    public ResBody deleteMenu(@PathVariable Long menuId) {
        menuService.deleteMenu(menuId);
        return new ResBody(ResBody.CODE_SUCCESS, "", null);
    }
}

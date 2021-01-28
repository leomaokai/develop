package com.kai.server.controller;


import com.kai.server.pojo.Menu;
import com.kai.server.service.IMenuService;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author kai
 * @since 2021-01-27
 */
@RestController
@RequestMapping("/system/cfg")
public class MenuController {

    @Autowired
    private IMenuService menuService;

    @ApiOperation(value = "通过用户Id查询菜单列表")
    @GetMapping("/menu")
    public List<Menu> getMenusByAdminId(){
        return menuService.getMenusByAdminId();
    }
}

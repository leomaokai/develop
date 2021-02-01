package com.kai.server.service;

import com.kai.server.pojo.Menu;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author kai
 * @since 2021-01-27
 */
public interface IMenuService extends IService<Menu> {

    /**
     * 根据用户id查询菜单列表
     * @return
     */
    List<Menu> getMenusByAdminId();


    /**
     * 根据角色获取菜单列表
     * @return
     */
    List<Menu> getMenusWithRole();


    List<Menu> getAllMenus();
}

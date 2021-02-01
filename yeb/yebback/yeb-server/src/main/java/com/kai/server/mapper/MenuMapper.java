package com.kai.server.mapper;

import com.kai.server.pojo.Menu;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import java.util.List;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author kai
 * @since 2021-01-27
 */
public interface MenuMapper extends BaseMapper<Menu> {

    /**
     * 根据用户id查询菜单列表
     * @param id
     * @return
     */
    List<Menu> getMenusByAdminId(Integer id);

    // 根据角色查询菜单列表
    List<Menu> getMenusWithRole();

    // 查询所有菜单
    List<Menu> getAllMenus();
}

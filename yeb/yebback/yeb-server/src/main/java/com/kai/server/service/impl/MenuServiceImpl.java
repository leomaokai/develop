package com.kai.server.service.impl;

import com.kai.server.Utils.AdminUtils;
import com.kai.server.pojo.Admin;
import com.kai.server.pojo.Menu;
import com.kai.server.mapper.MenuMapper;
import com.kai.server.service.IMenuService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.util.List;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author kai
 * @since 2021-01-27
 */
@Service
public class MenuServiceImpl extends ServiceImpl<MenuMapper, Menu> implements IMenuService {

    @Resource
    private MenuMapper menuMapper;

    @Resource
    private RedisTemplate redisTemplate;

    /**
     * 根据用户id查询菜单列表
     *
     * @return
     */
    @Override
    public List<Menu> getMenusByAdminId() {
        Integer adminId = AdminUtils.getCurrentAdmin().getId();
        ValueOperations valueOperations = redisTemplate.opsForValue();
        // 从redis获取菜单数据,如果为空从数据库获取
        List<Menu> menus = (List<Menu>) valueOperations.get("menu_" + adminId);
        if(CollectionUtils.isEmpty(menus)){
            // 从数据库中查询
            menus=menuMapper.getMenusByAdminId(adminId);
            // 将数据设置到redis中
            valueOperations.set("menu_"+adminId,menus);
        }
        return menus;
    }

    // 通过角色获取角色列表
    @Override
    public List<Menu> getMenusWithRole() {
        return menuMapper.getMenusWithRole();
    }

    // 查询所有菜单
    @Override
    public List<Menu> getAllMenus() {
        return menuMapper.getAllMenus();
    }
}

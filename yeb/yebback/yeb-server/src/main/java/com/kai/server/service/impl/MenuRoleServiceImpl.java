package com.kai.server.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.kai.server.pojo.MenuRole;
import com.kai.server.mapper.MenuRoleMapper;
import com.kai.server.Utils.RespBean;
import com.kai.server.service.IMenuRoleService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author kai
 * @since 2021-01-27
 */
@Service
public class MenuRoleServiceImpl extends ServiceImpl<MenuRoleMapper, MenuRole> implements IMenuRoleService {

    @Resource
    private MenuRoleMapper menuRoleMapper;

    @Override
    @Transactional
    public RespBean updateMenuRole(Integer rid, Integer[] mids) {
        if (null == mids || 0 == mids.length) {
            return RespBean.error("更新失败");
        }
        menuRoleMapper.delete(new QueryWrapper<MenuRole>().eq("rid", rid));
        Integer result = menuRoleMapper.insertRecord(rid, mids);
        if(result==mids.length){
            return RespBean.success("更新成功");
        }
        return RespBean.error("更新失败");
    }
}

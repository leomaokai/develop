package com.kai.server.service;

import com.kai.server.pojo.MenuRole;
import com.baomidou.mybatisplus.extension.service.IService;
import com.kai.server.Utils.RespBean;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author kai
 * @since 2021-01-27
 */
public interface IMenuRoleService extends IService<MenuRole> {

    RespBean updateMenuRole(Integer rid, Integer[] mids);
}

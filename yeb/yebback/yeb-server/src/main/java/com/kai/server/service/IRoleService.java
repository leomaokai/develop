package com.kai.server.service;

import com.kai.server.pojo.Role;
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
public interface IRoleService extends IService<Role> {

    // 根据用户id查询角色列表
    List<Role> getRoles(Integer adminId);

}

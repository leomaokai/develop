package com.kai.server.service;

import com.kai.server.pojo.Admin;
import com.baomidou.mybatisplus.extension.service.IService;
import com.kai.server.pojo.RespBean;

import javax.servlet.http.HttpServletRequest;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author kai
 * @since 2021-01-27
 */
public interface IAdminService extends IService<Admin> {

    //登录之后返回token
    RespBean login(String username, String password, HttpServletRequest request);

    //根据用户名获取用户
    Admin getAdminByUserName(String username);
}

package com.kai.server.service;

import com.kai.server.pojo.Admin;
import com.baomidou.mybatisplus.extension.service.IService;
import com.kai.server.Utils.RespBean;
import org.springframework.security.core.Authentication;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author kai
 * @since 2021-01-27
 */
public interface IAdminService extends IService<Admin> {

    // 登录之后返回token
    RespBean login(String username, String password, String code,HttpServletRequest request);

    // 根据用户名获取用户
    Admin getAdminByUserName(String username);

    // 获取所有操作员
    List<Admin> getAllAdmins(String keywords);

    RespBean updateAdminRole(Integer adminId, Integer[] rids);

    RespBean updateAdminPassword(String oldPass, String pass, Integer adminId);

    RespBean updateAdminUserFace(String url, Integer id, Authentication authentication);
}

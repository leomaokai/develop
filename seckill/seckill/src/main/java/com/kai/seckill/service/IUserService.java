package com.kai.seckill.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.kai.seckill.pojo.User;
import com.kai.seckill.vo.LoginVo;
import com.kai.seckill.vo.RespBean;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author kai
 * @since 2021-01-28
 */
public interface IUserService extends IService<User> {

    RespBean doLogin(LoginVo loginVo, HttpServletRequest request, HttpServletResponse response);

    // 根据cookie获取用户
    User getUserByCookie(String userTicket,HttpServletRequest request,HttpServletResponse response);
}

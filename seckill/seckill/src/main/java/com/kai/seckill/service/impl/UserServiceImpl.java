package com.kai.seckill.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.kai.seckill.exception.GlobalException;
import com.kai.seckill.mapper.UserMapper;
import com.kai.seckill.pojo.User;
import com.kai.seckill.service.IUserService;
import com.kai.seckill.utils.CookieUtil;
import com.kai.seckill.utils.MD5Utils;
import com.kai.seckill.utils.UUIDUtil;
import com.kai.seckill.vo.LoginVo;
import com.kai.seckill.vo.RespBean;
import com.kai.seckill.vo.RespBeanEnum;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author kai
 * @since 2021-01-28
 */
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements IUserService {

    @Resource
    private UserMapper userMapper;
    @Autowired
    private RedisTemplate redisTemplate;
    /**
     * 登录
     * @param loginVo
     * @param request
     * @param response
     * @return
     */
    @Override
    public RespBean doLogin(LoginVo loginVo, HttpServletRequest request, HttpServletResponse response) {
        String mobile = loginVo.getMobile();
        String password = loginVo.getPassword();

//        //参数校验
//        if(StringUtils.isEmpty(mobile) || StringUtils.isEmpty(password)){
//            return RespBean.error(RespBeanEnum.LOGIN_ERROR);
//        }
//        if(!ValidatorUtil.isMobile(mobile)){
//            return RespBean.error(RespBeanEnum.MOBILE_ERROR);
//        }
        // 根据手机号查用户
        User user = userMapper.selectById(mobile);
        System.out.println("userMapper==>"+user);
        if(user==null){
            //return RespBean.error(RespBeanEnum.LOGIN_ERROR);
            throw new GlobalException(RespBeanEnum.LOGIN_ERROR);
        }
        // 判断密码是否正确
        if(!MD5Utils.formPassToDBPass(password,user.getSlat()).equals(user.getPassword())){
            //return RespBean.error(RespBeanEnum.LOGIN_ERROR);
            throw new GlobalException(RespBeanEnum.LOGIN_ERROR);
        }

        // 生成cookie
        String ticket = UUIDUtil.uuid();

        // 将信息存放到session中
        // request.getSession().setAttribute(ticket,user);

        // 将信息存放到redis中
        redisTemplate.opsForValue().set("user:"+ticket,user);
        System.out.println("toRedis:user"+ticket);

        CookieUtil.setCookie(request,response,"userTicket",ticket);

        return RespBean.success();
    }

    // 根据cookie获取用户
    @Override
    public User getUserByCookie(String userTicket,HttpServletRequest request,HttpServletResponse response) {
        if(StringUtils.isEmpty(userTicket)){
            return null;
        }

        User user = (User) redisTemplate.opsForValue().get("user:" + userTicket);
        if (user!=null){
            CookieUtil.setCookie(request,response,"userTicket",userTicket);
        }
        return user;
    }

    // 更新密码
    @Override
    public RespBean updatePassword(String userTicket, String password,HttpServletRequest request,HttpServletResponse response) {

        User user = getUserByCookie(userTicket, request, response);
        if(user==null){
            throw new GlobalException(RespBeanEnum.MOBILE_NOT);
        }
        user.setPassword(MD5Utils.inputPassToDBPass(password,user.getSlat()));
        int i = userMapper.updateById(user);
        if(1==i){
            // 删除redis
            redisTemplate.delete("user"+userTicket);
            return RespBean.success();
        }
        return RespBean.error(RespBeanEnum.UPDATE_ERROR);
    }
}

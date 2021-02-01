package com.kai.server.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.kai.server.Utils.AdminUtils;
import com.kai.server.config.security.component.JwtTokenUtil;
import com.kai.server.mapper.RoleMapper;
import com.kai.server.pojo.Admin;
import com.kai.server.mapper.AdminMapper;
import com.kai.server.pojo.RespBean;
import com.kai.server.pojo.Role;
import com.kai.server.service.IAdminService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.mysql.cj.util.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author kai
 * @since 2021-01-27
 */
@Service
public class AdminServiceImpl extends ServiceImpl<AdminMapper, Admin> implements IAdminService {

    @Resource
    private AdminMapper adminMapper;
    @Resource
    private UserDetailsService userDetailsService;
    @Resource
    private PasswordEncoder passwordEncoder;
    @Resource
    private JwtTokenUtil jwtTokenUtil;
    @Value("${jwt.tokenHead}")
    private String tokenHead;

    @Resource
    private RoleMapper roleMapper;

    //登录之后返回token
    @Override
    public RespBean login(String username, String password, String code,HttpServletRequest request) {

        //验证码校验
        String captcha = (String) request.getSession().getAttribute("captcha");
        if(StringUtils.isNullOrEmpty(code) || !captcha.equalsIgnoreCase(code)){
            return RespBean.error("验证码错误,请重新输入");
        }

        //登录
        UserDetails userDetails = userDetailsService.loadUserByUsername(username);
        if (null == userDetails || !passwordEncoder.matches(password, userDetails.getPassword())) {
            return RespBean.error("用户名或密码错误");
        }
        if (!userDetails.isEnabled()) {
            return RespBean.error("账号被禁用");
        }

        //更新security登录用户对象
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(authenticationToken);

        //生成token
        String token = jwtTokenUtil.generateToken(userDetails);
        Map<String, Object> tokenMap = new HashMap<>();
        tokenMap.put("token", token);
        tokenMap.put("tokenHead", tokenHead);

        System.out.println(token);

        return RespBean.success("登录成功", tokenMap);
    }

    // 根据用户名获取用户
    @Override
    public Admin getAdminByUserName(String username) {
        return adminMapper.selectOne(new QueryWrapper<Admin>()
                .eq("username",username)
                .eq("enabled",true));
    }

    @Override
    public List<Admin> getAllAdmins(String keywords) {
        return adminMapper.getAllAdmins(AdminUtils.getCurrentAdmin().getId(),keywords);
    }


}

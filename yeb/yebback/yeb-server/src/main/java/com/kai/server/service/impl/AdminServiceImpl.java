package com.kai.server.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.kai.server.Utils.AdminUtils;
import com.kai.server.config.security.component.JwtTokenUtil;
import com.kai.server.mapper.AdminRoleMapper;
import com.kai.server.mapper.RoleMapper;
import com.kai.server.pojo.Admin;
import com.kai.server.mapper.AdminMapper;
import com.kai.server.pojo.AdminRole;
import com.kai.server.Utils.RespBean;
import com.kai.server.service.IAdminService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.mysql.cj.util.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
    @Resource
    private AdminRoleMapper adminRoleMapper;

    //登录之后返回token
    @Override
    public RespBean login(String username, String password, String code, HttpServletRequest request) {

        //验证码校验
        String captcha = (String) request.getSession().getAttribute("captcha");
        if (StringUtils.isNullOrEmpty(code) || !captcha.equalsIgnoreCase(code)) {
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
                .eq("username", username)
                .eq("enabled", true));
    }

    @Override
    public List<Admin> getAllAdmins(String keywords) {
        return adminMapper.getAllAdmins(AdminUtils.getCurrentAdmin().getId(), keywords);
    }

    @Override
    @Transactional
    public RespBean updateAdminRole(Integer adminId, Integer[] rids) {

        adminRoleMapper.delete(new QueryWrapper<AdminRole>().eq("adminId", adminId));
        Integer result = adminRoleMapper.addAdminRole(adminId, rids);
        if (rids.length == result) {
            return RespBean.success("更新角色成功");
        }
        return RespBean.error("更新失败");
    }

    @Override
    public RespBean updateAdminPassword(String oldPass, String pass, Integer adminId) {
        Admin admin = adminMapper.selectById(adminId);
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        if (encoder.matches(oldPass, admin.getPassword())) {
            admin.setPassword(encoder.encode(pass));
            int result = adminMapper.updateById(admin);
            if (result == 1) {
                return RespBean.success("更新成功");
            }
        }
        return RespBean.error("更新失败");
    }

    @Override
    public RespBean updateAdminUserFace(String url, Integer id, Authentication authentication) {
        Admin admin = adminMapper.selectById(id);
        admin.setUserFace(url);
        int result = adminMapper.updateById(admin);
        if (1 == result) {
            Admin principal = (Admin) authentication.getPrincipal();
            principal.setUserFace(url);
            SecurityContextHolder.getContext().setAuthentication(new UsernamePasswordAuthenticationToken(admin, null, authentication.getAuthorities()));
            return RespBean.success("更新成功", url);
        }
        return RespBean.error("更新失败");
    }


}

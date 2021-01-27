package com.kai.server.service.impl;

import com.kai.server.pojo.Role;
import com.kai.server.mapper.RoleMapper;
import com.kai.server.service.IRoleService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author kai
 * @since 2021-01-27
 */
@Service
public class RoleServiceImpl extends ServiceImpl<RoleMapper, Role> implements IRoleService {

}

package com.kai.server.mapper;

import com.kai.server.pojo.Admin;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.kai.server.pojo.Menu;
import com.kai.server.pojo.Role;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author kai
 * @since 2021-01-27
 */
public interface AdminMapper extends BaseMapper<Admin> {


    List<Admin> getAllAdmins(@Param("id") Integer id, @Param("keywords") String keywords);
}

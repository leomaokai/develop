package com.kai.server.mapper;

import com.kai.server.pojo.Department;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import io.swagger.models.auth.In;

import java.util.List;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author kai
 * @since 2021-01-27
 */
public interface DepartmentMapper extends BaseMapper<Department> {

    List<Department> getAllDepartments(Integer id);

    void addDep(Department dep);

    void deleteDep(Department department);
}

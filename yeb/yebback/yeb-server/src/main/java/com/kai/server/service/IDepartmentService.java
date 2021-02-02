package com.kai.server.service;

import com.kai.server.pojo.Department;
import com.baomidou.mybatisplus.extension.service.IService;
import com.kai.server.Utils.RespBean;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author kai
 * @since 2021-01-27
 */
public interface IDepartmentService extends IService<Department> {

    List<Department> getAllDepartments();

    RespBean addDep(Department dep);

    RespBean deleteDep(Integer id);
}

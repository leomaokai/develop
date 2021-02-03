package com.kai.server.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.kai.server.Utils.RespBean;
import com.kai.server.Utils.RespPageBean;
import com.kai.server.mapper.MailLogMapper;
import com.kai.server.pojo.Employee;
import com.kai.server.mapper.EmployeeMapper;
import com.kai.server.pojo.MailConstants;
import com.kai.server.pojo.MailLog;
import com.kai.server.service.IEmployeeService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.text.DecimalFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author kai
 * @since 2021-01-27
 */
@Service
public class EmployeeServiceImpl extends ServiceImpl<EmployeeMapper, Employee> implements IEmployeeService {
    @Resource
    private EmployeeMapper employeeMapper;

    @Resource
    private RabbitTemplate rabbitTemplate;

    @Resource
    private MailLogMapper mailLogMapper;

    @Override
    public RespPageBean getEmployeeByPage(Integer currentPage, Integer size, Employee employee, LocalDate[] beginDateScope) {

        // 开启分页,使用mybatisPlus的page
        Page<Employee> page = new Page<>(currentPage, size);
        IPage<Employee> employeeByPage = employeeMapper.getEmployeeByPage(page, employee, beginDateScope);
        RespPageBean respPageBean = new RespPageBean(employeeByPage.getTotal(), employeeByPage.getRecords());
        return respPageBean;
    }

    @Override
    public RespBean maxWorkId() {
        List<Map<String, Object>> maps = employeeMapper.selectMaps(new QueryWrapper<Employee>().select("max(workID)"));

        return RespBean.success(null, String.format("%08d", Integer.parseInt(maps.get(0).get("max(workID)").toString()) + 1));
    }

    @Override
    public RespBean addEmp(Employee employee) {
        // 得到合同开始日期
        LocalDate beginContract = employee.getBeginContract();
        // 得到合同结束日期
        LocalDate endContract = employee.getEndContract();
        // 得到两个日期相差的天数
        long days = beginContract.until(endContract, ChronoUnit.DAYS);
        // 设置格式,保留两位小数
        DecimalFormat decimalFormat = new DecimalFormat("##.00");
        // 天数/365.00
        double contractTerm = Double.parseDouble(decimalFormat.format(days / 365.00));
        employee.setContractTerm(contractTerm);

        if (1 == employeeMapper.insert(employee)) {

            Employee emp = employeeMapper.getEmployee(employee.getId()).get(0);

            // 消息落库,数据库记录发送的消息
            String msgID = UUID.randomUUID().toString();
            MailLog mailLog = new MailLog();
            mailLog.setMsgId(msgID);
            mailLog.setEid(employee.getId());
            mailLog.setStatus(0);
            mailLog.setRouteKey(MailConstants.MAIL_ROUTING_KEY_NAME);
            mailLog.setExchange(MailConstants.MAIL_EXCHANGE_NAME);
            mailLog.setCount(0);
            mailLog.setTryTime(LocalDateTime.now().plusMinutes(MailConstants.MSG_TIMEOUT));
            mailLog.setCreateTime(LocalDateTime.now());
            mailLog.setUpdateTime(LocalDateTime.now());
            mailLogMapper.insert(mailLog);

//            // 发送消息
//            rabbitTemplate.convertAndSend(MailConstants.MAIL_EXCHANGE_NAME,
//                    MailConstants.MAIL_ROUTING_KEY_NAME,
//                    emp, new CorrelationData(msgID));

            return RespBean.success("添加成功");
        }
        return RespBean.error("添加失败");
    }

    @Override
    public List<Employee> getEmployee(Integer id) {
        return employeeMapper.getEmployee(id);
    }
}

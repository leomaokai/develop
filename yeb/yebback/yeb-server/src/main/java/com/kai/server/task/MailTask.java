package com.kai.server.task;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.kai.server.pojo.Employee;
import com.kai.server.pojo.MailConstants;
import com.kai.server.pojo.MailLog;
import com.kai.server.service.IEmployeeService;
import com.kai.server.service.IMailLogService;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.List;

// 邮件发送定时任务
@Component
public class MailTask {

    @Resource
    private IMailLogService mailLogService;
    @Resource
    private IEmployeeService employeeService;
    @Resource
    private RabbitTemplate rabbitTemplate;

    // 邮件发送定时任务,十秒执行一次
    @Scheduled(cron = "0/30 * * * * ?")
    public void mailTask() {
        System.out.println("**************定时任务");
        List<MailLog> list = mailLogService
                .list(new QueryWrapper<MailLog>()
                        .eq("status", 0)
                        .lt("tryTime", LocalDateTime.now()));
        list.forEach(mailLog -> {
            // 如果重试次数超过三次,更新状态为投递失败
            if (2 <= mailLog.getCount()) {
                mailLogService
                        .update(new UpdateWrapper<MailLog>()
                                .set("status", 2)
                                .eq("msgId", mailLog.getMsgId()));
            }

            mailLogService.update(new UpdateWrapper<MailLog>()
                    .set("count", mailLog.getCount() + 1)
                    .set("updateTime", LocalDateTime.now())
                    .set("tryTime", LocalDateTime.now().plusMinutes(MailConstants.MSG_TIMEOUT))
                    .eq("msgId",mailLog.getMsgId()));
            Employee employee = employeeService.getEmployee(mailLog.getEid()).get(0);
            // 重新发送消息
            rabbitTemplate.convertAndSend(MailConstants.MAIL_EXCHANGE_NAME,
                    MailConstants.MAIL_ROUTING_KEY_NAME,
                    employee,
                    new CorrelationData(mailLog.getMsgId()));
        });
    }
}

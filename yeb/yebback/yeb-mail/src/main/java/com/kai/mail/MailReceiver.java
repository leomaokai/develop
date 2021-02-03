package com.kai.mail;


import com.kai.server.pojo.Employee;
import com.kai.server.pojo.MailConstants;
import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.Channel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.support.AmqpHeaderMapper;
import org.springframework.amqp.support.AmqpHeaders;
import org.springframework.boot.autoconfigure.mail.MailProperties;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageHeaders;
import org.springframework.stereotype.Component;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import javax.annotation.Resource;
import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.io.IOException;
import java.util.Date;

@Component
public class MailReceiver {

    private static final Logger LOGGER = LoggerFactory.getLogger(MailReceiver.class);

    @Resource
    private JavaMailSender javaMailSender;
    @Resource
    private MailProperties mailProperties;
    @Resource
    private TemplateEngine templateEngine;
    @Resource
    private RedisTemplate redisTemplate;

    //    @RabbitListener(queues = MailConstants.MAIL_QUEUE_NAME)
//    public void handler(Employee employee) {
//        MimeMessage mimeMessage = javaMailSender.createMimeMessage();
//        MimeMessageHelper helper = new MimeMessageHelper(mimeMessage);
//        try {
//            // 发件人
//            helper.setFrom(mailProperties.getUsername());
//            // 收件人
//            helper.setTo(employee.getEmail());
//            // 主题
//            helper.setSubject("入职欢迎邮件");
//            // 日期
//            helper.setSentDate(new Date());
//            // 设置内容
//            Context context = new Context();
//            context.setVariable("name", employee.getName());
//            context.setVariable("posName", employee.getPosition().getName());
//            context.setVariable("jobLevelName", employee.getJoblevel().getName());
//            context.setVariable("departmentName", employee.getDepartment().getName());
//            String mail = templateEngine.process("mail", context);
//            // 发送
//            helper.setText(mail,true);
//            javaMailSender.send(mimeMessage);
//
//        } catch (MessagingException e) {
//            LOGGER.error("邮件发送失败:",e.getMessage());
//        }
//    }

    /**
     * 消费端幂等性操作(防止一条消息被消费两次),通过redis判断
     * 如果redis中存在消息id则已经被消费过了,直接返回,否则正常消费
     *
     * @param message
     * @param channel
     */
    @RabbitListener(queues = MailConstants.MAIL_QUEUE_NAME)
    public void handler(Message message, Channel channel) {
        // 通过message获得员工对象
        Employee employee = (Employee) message.getPayload();
        MessageHeaders headers = message.getHeaders();
        // 消息序号
        Long tag = (Long) headers.get(AmqpHeaders.DELIVERY_TAG);
        // 得到msgId,固定方式
        String msgId = (String) headers.get("spring_returned_message_correlation");
        HashOperations hashOperations = redisTemplate.opsForHash();
        try {
            // 如果redis中存在消息id,说明已经被消费过了
            if (hashOperations.entries("mail_log").containsKey(msgId)) {
                LOGGER.error("消息已经被消费======>{}", msgId);
                /**
                 * 手动确认消息
                 * tag:消息序号
                 * multiple:是否确认多条,false只确认一条
                 */
                channel.basicAck(tag, false);
                return;
            }
            MimeMessage mimeMessage = javaMailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage);
            // 发件人
            helper.setFrom(mailProperties.getUsername());
            // 收件人
            helper.setTo(employee.getEmail());
            // 主题
            helper.setSubject("入职欢迎邮件");
            // 日期
            helper.setSentDate(new Date());
            // 设置内容
            Context context = new Context();
            context.setVariable("name", employee.getName());
            context.setVariable("posName", employee.getPosition().getName());
            context.setVariable("jobLevelName", employee.getJoblevel().getName());
            context.setVariable("departmentName", employee.getDepartment().getName());
            String mail = templateEngine.process("mail", context);
            // 发送
            helper.setText(mail, true);
            javaMailSender.send(mimeMessage);
            LOGGER.info("邮件发送成功");
            // 将msgId存到redis中
            hashOperations.put("mail_log", msgId, "OK");
            // 手动确认
            channel.basicAck(tag, false);
        } catch (Exception e) {
            /**
             * 手动确认消息
             * tag:消息序号
             * multiple:是否确认多条
             * requeue:是否退回队列
             */
            try {
                channel.basicNack(tag, false, true);
            } catch (IOException e1) {
                LOGGER.error("邮件发送失败:=====>{}", e1.getMessage());
            }
            LOGGER.error("邮件发送失败:=====>{}", e.getMessage());
        }
    }
}

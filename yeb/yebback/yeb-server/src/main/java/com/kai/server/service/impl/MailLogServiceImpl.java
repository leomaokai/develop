package com.kai.server.service.impl;

import com.kai.server.pojo.MailLog;
import com.kai.server.mapper.MailLogMapper;
import com.kai.server.service.IMailLogService;
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
public class MailLogServiceImpl extends ServiceImpl<MailLogMapper, MailLog> implements IMailLogService {

}

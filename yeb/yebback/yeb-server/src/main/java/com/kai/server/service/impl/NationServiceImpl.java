package com.kai.server.service.impl;

import com.kai.server.pojo.Nation;
import com.kai.server.mapper.NationMapper;
import com.kai.server.service.INationService;
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
public class NationServiceImpl extends ServiceImpl<NationMapper, Nation> implements INationService {

}

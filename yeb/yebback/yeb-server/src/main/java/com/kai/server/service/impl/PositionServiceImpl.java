package com.kai.server.service.impl;

import com.kai.server.pojo.Position;
import com.kai.server.mapper.PositionMapper;
import com.kai.server.service.IPositionService;
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
public class PositionServiceImpl extends ServiceImpl<PositionMapper, Position> implements IPositionService {

}

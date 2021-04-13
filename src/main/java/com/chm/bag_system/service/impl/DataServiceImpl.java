package com.chm.bag_system.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.chm.bag_system.entity.Data;
import com.chm.bag_system.mapper.DataMapper;
import com.chm.bag_system.service.DataService;
import org.springframework.stereotype.Service;

/**
 * @program: bag_system
 * @description
 * @author: BeiKe
 * @create: 2021-04-13 16:22
 **/
@Service
public class DataServiceImpl extends ServiceImpl<DataMapper, Data> implements DataService {
}

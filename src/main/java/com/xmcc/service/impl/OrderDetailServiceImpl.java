package com.xmcc.service.impl;

import com.xmcc.dao.impl.BatchDaoImpl;
import com.xmcc.entity.OrderDetail;
import com.xmcc.service.OrderDetailService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
@Service
public class OrderDetailServiceImpl extends BatchDaoImpl<OrderDetail> implements OrderDetailService {
    @Override
    @Transactional//加入事务管理，增删改触发
    public void batchInsert(List<OrderDetail> orderDetailList) {
        super.batchInsert(orderDetailList);

    }
}

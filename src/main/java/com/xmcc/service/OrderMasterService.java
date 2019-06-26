package com.xmcc.service;

import com.xmcc.common.ResultResponse;
import com.xmcc.dto.OrderMasterDto;
import com.xmcc.entity.OrderDetail;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;


public interface OrderMasterService {
    ResultResponse insertOrder(OrderMasterDto orderMasterDto);
    //查询单个订单
    ResultResponse queryById(String orderId);
    //查询订单列表
    ResultResponse<OrderDetail> findList(String buyerOpenid , Pageable pageable);
    //取消订单
    ResultResponse cancel(OrderMasterDto orderMasterDto);
}

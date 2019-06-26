package com.xmcc.service.impl;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.xmcc.common.*;
import com.xmcc.dto.OrderDetailDto;
import com.xmcc.dto.OrderMasterDto;
import com.xmcc.entity.OrderDetail;
import com.xmcc.entity.OrderMaster;
import com.xmcc.entity.ProductInfo;
import com.xmcc.exception.CustomException;
import com.xmcc.repository.OrderDetailRepository;
import com.xmcc.repository.OrderMasterRepository;
import com.xmcc.service.OrderDetailService;
import com.xmcc.service.OrderMasterService;
import com.xmcc.service.ProductInfoService;
import com.xmcc.util.BigDecimalUtil;
import com.xmcc.util.IDUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
@Service
public class OrderMasterServiceImpl implements OrderMasterService {
    @Autowired
    private ProductInfoService productInfoService;
    @Autowired
    private OrderDetailService orderDetailService;
    @Autowired
    private OrderMasterRepository orderMasterRepository;
    @Autowired
    private OrderDetailRepository orderDetailRepository;
    @Autowired
    private OrderMasterService orderMasterService;
    @Override
    public ResultResponse insertOrder(OrderMasterDto orderMasterDto) {
/**
 *  @Valid 用于配合jsr303注解 验证参数，只能在controller层进行验证
 *  validator 在service层验证
 */
        //取出订单项
        List<OrderDetailDto> items = orderMasterDto.getItems();
        //创建集合来存储OrderDetail
        List<OrderDetail> orderDetailList = Lists.newArrayList();
        //初始化订单的总金额
        BigDecimal totalPrice = new BigDecimal("0");
        //遍历订单项，获取商品详情
        for (OrderDetailDto orderDetailDto:items
             ) {
            //查询订单
            ResultResponse<ProductInfo> productInfoResultResponse = productInfoService.queryById(orderDetailDto.getProductId());
            //判断ResultResponse的code即可
            if (productInfoResultResponse.getCode()== ResultEnums.FAIL.getCode()){
                throw new CustomException(productInfoResultResponse.getMsg());
            }
            //得到商品
            ProductInfo productInfo = productInfoResultResponse.getData();
            //比较库存
            if (productInfo.getProductStock()<orderDetailDto.getProductQuantity()){
                throw new CustomException(ProductEnums.PRODUCT_NOT_ENOUGH.getMsg());
            }
            //创建订单项
            OrderDetail orderDetail = OrderDetail.builder().detailId(IDUtils.createIdbyUUID()).productIcon(productInfo.getProductIcon())
                    .productId(orderDetailDto.getProductId()).productName(productInfo.getProductName())
                    .productPrice(productInfo.getProductPrice()).productQuantity(orderDetailDto.getProductQuantity()).build();
            //添加到订单项集合中
            orderDetailList.add(orderDetail);
            //减少库存
            productInfo.setProductStock(productInfo.getProductStock()-orderDetailDto.getProductQuantity());
            //更新产品数据
            productInfoService.updateProduct(productInfo);
            //计算价格
             totalPrice = BigDecimalUtil.add(totalPrice, BigDecimalUtil.multi(productInfo.getProductPrice(), orderDetailDto.getProductQuantity()));
        }
        //生成订单id
        String order_id = IDUtils.createIdbyUUID();
        //构建订单信息
        OrderMaster orderMaster = OrderMaster.builder().orderId(order_id).buyerAddress(orderMasterDto.getAddress())
                .buyerName(orderMasterDto.getName()).buyerOpenid(orderMasterDto.getOpenid())
                .buyerPhone(orderMasterDto.getPhone()).orderAmount(totalPrice)
                .orderStatus(OrderEnums.NEW.getCode()).payStatus(PayEnums.WAIT.getCode()).build();
        //将订单id设置到订单项中
        List<OrderDetail> detailList = orderDetailList.stream().map(orderDetail -> {
            orderDetail.setOrderId(order_id);
            return orderDetail;
        }).collect(Collectors.toList());
        //批量插入订单项
        orderDetailService.batchInsert(detailList);
        //插入订单
        orderMasterRepository.save(orderMaster);
        HashMap<String,String> map= Maps.newHashMap();

        map.put("orderId",order_id);
        return ResultResponse.success(map);
    }

    @Override
    public ResultResponse queryById(String orderId) {
      /*  Optional<OrderMaster> orderMaster = orderMasterRepository.findById(orderId);
        if (orderMaster==null){
            throw new CustomException(OrderEnums.ORDER_NOT_EXITS.getMsg());
        }
          orderDetailRepository.findById(orderId);*/


        return null;
    }

    @Override
    public ResultResponse findList(String buyerOpenid, Pageable pageable) {
       Page<OrderMaster> orderMasterPage= orderMasterRepository.findByBuyerOpenid(buyerOpenid,pageable);
        System.out.println("11111");
        System.out.println(orderMasterPage);
        List<OrderMasterDto> orderMasterDtoList = orderMasterPage.stream().map(orderMaster ->
                OrderMasterDto.build(orderMaster)).collect(Collectors.toList());
        HashMap<String,String> map= Maps.newHashMap();

        PageImpl<OrderMasterDto> orderMasterDtos = new PageImpl<>(orderMasterDtoList, pageable, orderMasterPage.getTotalElements());
        return ResultResponse.success(orderMasterDtos);
    }

    @Override
    public ResultResponse cancel(OrderMasterDto orderMasterDto) {
        return null;
    }
}

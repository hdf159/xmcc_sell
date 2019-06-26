package com.xmcc.repository;

import com.xmcc.common.ResultResponse;
import com.xmcc.dto.OrderMasterDto;
import com.xmcc.entity.OrderMaster;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

@Service
public interface OrderMasterRepository extends JpaRepository<OrderMaster,String> {


    Page<OrderMaster> findByBuyerOpenid(String buyerOpenid, Pageable pageable);
}

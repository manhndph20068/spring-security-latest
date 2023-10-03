package com.backend.repository;

import com.backend.dto.response.VoucherOrderResponse;
import com.backend.entity.VoucherOrder;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import javax.persistence.Tuple;
import java.util.List;

@Repository
public interface VoucherOrderRepository extends JpaRepository<VoucherOrder, Long> {
    @Query(value = "SELECT v.code,v.name, v.quantity, v.discountamount, v.minbillvalue, v.startdate, v.enddate ,v.createdate ,v.updateat ,v.reduceform ,v.status FROM voucherorder v ",nativeQuery = true)
    Page<Tuple> getAllVoucherOrder(Pageable pageable);
}

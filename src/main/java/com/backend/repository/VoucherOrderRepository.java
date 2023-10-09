package com.backend.repository;

import com.backend.entity.VoucherOrder;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface VoucherOrderRepository extends JpaRepository<VoucherOrder, Long> {
    @Query(value = "SELECT v.id,v.code,v.name, v.quantity, v.discountamount, v.minbillvalue, v.startdate, v.enddate ,v.createdate ,v.updateat ,v.reduceform ,v.status\n" +
            "FROM voucherorder v \n" +
            "WHERE v.status in(0,1,2)",nativeQuery = true)
    Page<VoucherOrder> getAllVoucherOrder(Pageable pageable);

    @Query(value = "SELECT v.id,v.code,v.name, v.quantity, v.discountamount, v.minbillvalue, v.startdate, v.enddate ,v.createdate ,v.updateat ,v.reduceform ,v.status\n" +
            "FROM voucherorder v \n" +
            "WHERE v.status in(0)",nativeQuery = true)
    Page<VoucherOrder> getAllVoucherStatus0(Pageable pageable);

    @Query(value = "SELECT v.id,v.code,v.name, v.quantity, v.discountamount, v.minbillvalue, v.startdate, v.enddate ,v.createdate ,v.updateat ,v.reduceform ,v.status\n" +
            "FROM voucherorder v \n" +
            "WHERE v.status in(1)",nativeQuery = true)
    Page<VoucherOrder> getAllVoucherStatus1(Pageable pageable);

    @Query(value = "SELECT v.id,v.code,v.name, v.quantity, v.discountamount, v.minbillvalue, v.startdate, v.enddate ,v.createdate ,v.updateat ,v.reduceform ,v.status\n" +
            "FROM voucherorder v \n" +
            "WHERE v.status in(2)",nativeQuery = true)
    Page<VoucherOrder> getAllVoucherStatus2(Pageable pageable);
}

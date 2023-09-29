package com.backend.service;

import com.backend.ServiceResult;
import com.backend.dto.request.VoucherOrderRequest;
import com.backend.dto.response.VoucherOrderResponse;
import com.backend.entity.VoucherOrder;

public interface IVoucherOrderService {
    ServiceResult<VoucherOrderResponse> addVoucher(VoucherOrderRequest voucherOrderRequest);

    ServiceResult<VoucherOrderResponse> updateVoucher(VoucherOrderRequest voucherOrderRequest,Long id);

    void updateVoucherStatus();

    VoucherOrderResponse convertToResponse(VoucherOrder voucherOrder);
}

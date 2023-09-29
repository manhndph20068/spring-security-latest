package com.backend.service.impl;

import com.backend.ServiceResult;
import com.backend.config.AppConstant;
import com.backend.dto.request.VoucherOrderRequest;
import com.backend.dto.response.VoucherOrderResponse;
import com.backend.entity.VoucherOrder;
import com.backend.repository.VoucherOrderRepository;
import com.backend.service.IVoucherOrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;

import java.security.SecureRandom;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class VoucherServiceImpl implements IVoucherOrderService {

    @Autowired
    private VoucherOrderRepository voucherOrderRepository;

    public ServiceResult<VoucherOrderResponse> result(String mess) {
        return new ServiceResult<>(AppConstant.FAIL, mess, null);
    }

    public ServiceResult<VoucherOrderResponse> validateVoucher(VoucherOrderRequest voucherOrderRequest) {
        List<String> errorMessages = new ArrayList<>();
        if (voucherOrderRequest.getCode() == null || voucherOrderRequest.getName() == null) {
            return result("Dữ liệu không được để trống");
        }
        return null;
    }

    private static final String CHARACTERS = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
    private static final SecureRandom RANDOM = new SecureRandom();

    public static String generateRandomString(int length) {
        StringBuilder sb = new StringBuilder(length);
        for (int i = 0; i < length; i++) {
            int randomIndex = RANDOM.nextInt(CHARACTERS.length());
            char randomChar = CHARACTERS.charAt(randomIndex);
            sb.append(randomChar);
        }
        return sb.toString();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public ServiceResult<VoucherOrderResponse> addVoucher(VoucherOrderRequest voucherOrderRequest) {
        LocalDateTime currentDateTime = LocalDateTime.now();
        String randomString = generateRandomString(10);
        VoucherOrder voucherHoaDon = new VoucherOrder();
        ServiceResult<VoucherOrderResponse> validationResult = validateVoucher(voucherOrderRequest);
        try {
            if (validationResult != null) {
                return validationResult;
            } else {
                voucherHoaDon.setCode(randomString);
                voucherHoaDon.setName(voucherOrderRequest.getName());
                voucherHoaDon.setQuantity(voucherOrderRequest.getQuantity());
                voucherHoaDon.setDiscountAmount(voucherOrderRequest.getDiscountAmount());
                voucherHoaDon.setMinBillValue(voucherOrderRequest.getMinBillValue());
                voucherHoaDon.setStartDate(voucherOrderRequest.getStartDate());
                voucherHoaDon.setEndDate(voucherOrderRequest.getEndDate());
                voucherHoaDon.setCreateDate(currentDateTime);
                voucherHoaDon.setUpdateAt(null);
                voucherHoaDon.setReduceForm(voucherOrderRequest.getReduceForm());
                voucherHoaDon.setStatus(0);
                voucherHoaDon = voucherOrderRepository.save(voucherHoaDon);
            }
        } catch (Exception e) {
            // Xảy ra lỗi, gọi rollback để hoàn tác các thay đổi
            VoucherOrderResponse convertVoucherOrderResponse=convertToResponse(voucherHoaDon);
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return new ServiceResult<>(AppConstant.FAIL, e.getMessage(), convertVoucherOrderResponse); // hoặc xử lý lỗi một cách thích hợp dựa trên nhu cầu của bạn
        }
        return new ServiceResult<>(AppConstant.SUCCESS, "Them thanh cong", null);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public ServiceResult<VoucherOrderResponse> updateVoucher(VoucherOrderRequest voucherOrderRequest, Long id) {
        LocalDateTime currentDateTime = LocalDateTime.now();
        Optional<VoucherOrder> optionalVoucherHoaDon = voucherOrderRepository.findById(id);
        VoucherOrder voucherHoaDon = optionalVoucherHoaDon.get();
        try {
            if (optionalVoucherHoaDon.isPresent()) {
                voucherHoaDon.setName(voucherOrderRequest.getName());
                voucherHoaDon.setQuantity(voucherOrderRequest.getQuantity());
                voucherHoaDon.setDiscountAmount(voucherOrderRequest.getDiscountAmount());
                voucherHoaDon.setMinBillValue(voucherOrderRequest.getMinBillValue());
                voucherHoaDon.setStartDate(voucherOrderRequest.getStartDate());
                voucherHoaDon.setEndDate(voucherOrderRequest.getEndDate());
                voucherHoaDon.setUpdateAt(currentDateTime);
                voucherHoaDon.setReduceForm(voucherOrderRequest.getReduceForm());
                voucherHoaDon.setStatus(0);
                voucherHoaDon=voucherOrderRepository.save(voucherHoaDon);
            } else {
                throw new RuntimeException("Id không tồn tại");
            }
        } catch (Exception e) {
            // Xảy ra lỗi, gọi rollback để hoàn tác các thay đổi
            VoucherOrderResponse convertVoucherOrderResponse=convertToResponse(voucherHoaDon);
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return new ServiceResult<>(AppConstant.FAIL, e.getMessage(), convertVoucherOrderResponse); // hoặc xử lý lỗi một cách thích hợp dựa trên nhu cầu của bạn
        }
        VoucherOrderResponse convertVoucherOrderResponse=convertToResponse(voucherHoaDon);
        return new ServiceResult<>(AppConstant.SUCCESS, "Sua thanh cong", convertVoucherOrderResponse);
    }

    @Override
    @Scheduled(fixedRate = 60000)
    public void updateVoucherStatus() {
        List<VoucherOrder> vouchers = voucherOrderRepository.findAll();
        LocalDateTime currentDateTime = LocalDateTime.now();

        for (VoucherOrder voucher : vouchers) {
            if (voucher.getStartDate() == null || voucher.getEndDate() == null) {
                // Bỏ qua voucher này nếu có startDate hoặc endDate là null
                continue;
            }
            if (currentDateTime.isAfter(voucher.getStartDate()) && currentDateTime.isBefore(voucher.getEndDate())) {
                voucher.setStatus(1); // Cập nhật thành "đã kích hoạt"
            } else if (currentDateTime.isAfter(voucher.getEndDate())) {
                voucher.setStatus(2); // Cập nhật thành "hết hạn"
            } else {
                voucher.setStatus(0);
            }
            voucherOrderRepository.save(voucher);
        }
    }

    @Override
    public VoucherOrderResponse convertToResponse(VoucherOrder voucherOrder) {
        return VoucherOrderResponse.builder()
                .code(voucherOrder.getCode())
                .name(voucherOrder.getName())
                .quantity(voucherOrder.getQuantity())
                .discountAmount(voucherOrder.getDiscountAmount())
                .minBillValue(voucherOrder.getMinBillValue())
                .startDate(voucherOrder.getStartDate())
                .endDate(voucherOrder.getEndDate())
                .createDate(voucherOrder.getCreateDate())
                .updateAt(voucherOrder.getUpdateAt())
                .reduceForm(voucherOrder.getReduceForm())
                .status(voucherOrder.getStatus())
                .build();
    }
}

package com.backend.service;

import com.backend.ServiceResult;
import com.backend.dto.request.ShoeRequest;
import com.backend.entity.Shoe;

import java.util.List;

public interface IShoeService {

    ServiceResult<?> resultValidate(String mess);

    String validateNhanVien(ShoeRequest shoeRequest);

    ServiceResult<Shoe> addNewShoe(ShoeRequest shoeRequest);
}

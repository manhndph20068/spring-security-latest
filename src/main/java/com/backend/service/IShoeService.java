package com.backend.service;

import com.backend.ServiceResult;
import com.backend.dto.request.ShoeRequest;
import com.backend.entity.Shoe;

public interface IShoeService {

    ServiceResult<Shoe> addNewShoe(ShoeRequest shoeRequest);
}

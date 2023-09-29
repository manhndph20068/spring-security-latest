package com.backend.service.impl;

import com.backend.ServiceResult;
import com.backend.config.AppConstant;
import com.backend.dto.request.ShoeRequest;
import com.backend.entity.Brand;
import com.backend.entity.Category;
import com.backend.entity.Color;
import com.backend.entity.Image;
import com.backend.entity.Shoe;
import com.backend.entity.ShoeDetail;
import com.backend.entity.Size;
import com.backend.entity.Thumbnail;
import com.backend.repository.CategoryRepository;
import com.backend.repository.ColorRepository;
import com.backend.repository.ImageRepository;
import com.backend.repository.ShoeDetailRepository;
import com.backend.repository.ShoeRepository;
import com.backend.repository.SizeRepository;
import com.backend.repository.ThumbnailRepository;
import com.backend.service.IShoeService;
import com.backend.service.ImageUploadService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;

import java.io.IOException;
import java.util.Calendar;
import java.util.Date;
import java.util.Optional;

@Service
public class ShoeServiceImpl implements IShoeService {

    @Autowired
    private ShoeRepository shoeRepository;

    @Autowired
    private ShoeDetailRepository shoeDetailRepository;

    @Autowired
    private SizeRepository sizeRepository;

    @Autowired
    private ImageUploadService imageUploadService;

    @Autowired
    private ThumbnailRepository thumbnailRepository;

    @Autowired
    private ImageRepository imageRepository;

    @Autowired
    private ColorRepository colorRepository;

    @Autowired
    private CategoryRepository categoryRepository;


    @Override
    @Transactional(rollbackFor = Exception.class)
    public ServiceResult<Shoe> addNewShoe(ShoeRequest shoeRequest) {
        try {
            Shoe shoe = new Shoe();
            Calendar cal = Calendar.getInstance();
            Date dateNow = cal.getTime();
            shoe.setName(shoeRequest.getName());
            shoe.setCreatedAt(dateNow);
            shoe.setUpdatedAt(dateNow);
            shoe.setStatus(shoeRequest.getStatusShoe());
            shoe = shoeRepository.save(shoe);


            for (ShoeDetail requestShoeDetail : shoeRequest.getShoeDetailList()) {
                Optional<Color> optionalColor = colorRepository.findById(requestShoeDetail.getColor().getId());
                Optional<Category> optionalCategory = categoryRepository.findById(requestShoeDetail.getCategory().getId());
                if (optionalCategory.isPresent()) {
                    if (optionalColor.isPresent()) {
                        ShoeDetail shoeDetail = new ShoeDetail();
                        shoeDetail.setShoe(shoe);
                        shoeDetail.setColor(Color.builder().id(requestShoeDetail.getColor().getId()).build());
                        shoeDetail.setCategory(Category.builder().id(requestShoeDetail.getCategory().getId()).build());
                        shoeDetail.setBrand(Brand.builder().id(requestShoeDetail.getBrand().getId()).build());
                        shoeDetail.setSize(Size.builder().id(requestShoeDetail.getSize().getId()).build());
                        shoeDetail.setPriceInput(requestShoeDetail.getPriceInput());
                        shoeDetail.setQuantity(requestShoeDetail.getQuantity());
                        shoeDetail.setCreatedAt(dateNow);
                        shoeDetail.setUpdatedAt(dateNow);
                        String shoeName = shoe.getName();
                        String colorName = optionalColor.get().getName();
                        shoeDetail.setCode(shoeName + " - " + colorName.toLowerCase());

                        shoeDetail = shoeDetailRepository.save(shoeDetail);

                        for (Thumbnail thumbnail : requestShoeDetail.getThumbnails()) {
                            try {
                                String thumbnailUrl = imageUploadService.uploadImageByName(String.valueOf(thumbnail.getImgName()));
                                thumbnail.setImgName(thumbnail.getImgName());
                                thumbnail.setImgUrl(thumbnailUrl);
                                thumbnail.setShoeDetail(shoeDetail);
                                thumbnailRepository.save(thumbnail);
                            } catch (IOException e) {
                                return new ServiceResult<>(AppConstant.ERROR, "Error uploading thumbnail", null);
                            }
                        }

                        for (Image image : requestShoeDetail.getImages()) {
                            try {
                                String thumbnailUrl = imageUploadService.uploadImageByName(String.valueOf(image.getImgName()));
                                image.setImgName(image.getImgName());
                                image.setImgUrl(thumbnailUrl);
                                image.setShoeDetail(shoeDetail);
                                imageRepository.save(image);
                            } catch (IOException e) {
                                return new ServiceResult<>(AppConstant.ERROR, "Error uploading image", null);
                            }
                        }
                    } else {
                        throw new RuntimeException("Màu sắc không tồn tại");
                    }
                } else {
                    throw new RuntimeException("Loại giày không tồn tại");
                }

            }
            return new ServiceResult<>(AppConstant.SUCCESS, "Shoe added successfully", shoe);
        } catch (Exception e) {
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return new ServiceResult<>(AppConstant.BAD_REQUEST, e.getMessage(), null);
        }
    }
}

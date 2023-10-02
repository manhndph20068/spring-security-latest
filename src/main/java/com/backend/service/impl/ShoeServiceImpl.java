package com.backend.service.impl;

import com.backend.ServiceResult;
import com.backend.config.AppConstant;
import com.backend.dto.request.ShoeRequest;
import com.backend.dto.response.shoedetail.DataPaginate;
import com.backend.dto.response.shoedetail.Meta;
import com.backend.dto.response.shoedetail.ResultItem;
import com.backend.entity.Brand;
import com.backend.entity.Category;
import com.backend.entity.Color;
import com.backend.entity.Image;
import com.backend.entity.Shoe;
import com.backend.entity.ShoeDetail;
import com.backend.entity.Size;
import com.backend.entity.Sole;
import com.backend.entity.Thumbnail;
import com.backend.repository.BrandCategory;
import com.backend.repository.CategoryRepository;
import com.backend.repository.ColorRepository;
import com.backend.repository.ImageRepository;
import com.backend.repository.ShoeDetailRepository;
import com.backend.repository.ShoeRepository;
import com.backend.repository.SizeRepository;
import com.backend.repository.SoleRepository;
import com.backend.repository.ThumbnailRepository;
import com.backend.service.IShoeService;
import com.backend.service.ImageUploadService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;

import javax.persistence.Tuple;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

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

    @Autowired
    private BrandCategory brandCategory;

    @Autowired
    private SoleRepository soleRepository;


//    @Override
//    @Transactional(rollbackFor = Exception.class)
//    public ServiceResult<Shoe> addNewShoe(ShoeRequest shoeRequest) {
//        try {
//            Shoe shoe = new Shoe();
//            Calendar cal = Calendar.getInstance();
//            Date dateNow = cal.getTime();
//            shoe.setName(shoeRequest.getName());
//            shoe.setCreatedAt(dateNow);
//            shoe.setUpdatedAt(dateNow);
//            shoe.setStatus(shoeRequest.getStatusShoe());
//            shoe = shoeRepository.save(shoe);
//
//
//            for (ShoeDetail requestShoeDetail : shoeRequest.getShoeDetailList()) {
//                Optional<Color> optionalColor = colorRepository.findById(requestShoeDetail.getColor().getId());
//                Optional<Category> optionalCategory = categoryRepository.findById(requestShoeDetail.getCategory().getId());
//                Optional<Brand> optionalBrand = brandCategory.findById(requestShoeDetail.getBrand().getId());
//                Optional<Size> optionalSize = sizeRepository.findById(requestShoeDetail.getSize().getId());
//                if (optionalSize.isPresent()){
//                    if (optionalBrand.isPresent()){
//                        if (optionalCategory.isPresent()) {
//                            if (optionalColor.isPresent()) {
//                                ShoeDetail shoeDetail = new ShoeDetail();
//                                shoeDetail.setShoe(shoe);
//                                shoeDetail.setColor(Color.builder().id(requestShoeDetail.getColor().getId()).build());
//                                shoeDetail.setCategory(Category.builder().id(requestShoeDetail.getCategory().getId()).build());
//                                shoeDetail.setBrand(Brand.builder().id(requestShoeDetail.getBrand().getId()).build());
//                                shoeDetail.setSize(Size.builder().id(requestShoeDetail.getSize().getId()).build());
//                                shoeDetail.setPriceInput(requestShoeDetail.getPriceInput());
//                                shoeDetail.setQuantity(requestShoeDetail.getQuantity());
//                                shoeDetail.setCreatedAt(dateNow);
//                                shoeDetail.setUpdatedAt(dateNow);
//                                String shoeName = shoe.getName();
//                                String colorName = optionalColor.get().getName();
//                                shoeDetail.setCode(shoeName + " - " + colorName.toLowerCase());
//
//                                shoeDetail = shoeDetailRepository.save(shoeDetail);
//
//                                for (Thumbnail thumbnail : requestShoeDetail.getThumbnails()) {
//                                    try {
//                                        String thumbnailUrl = imageUploadService.uploadImageByName(String.valueOf(thumbnail.getImgName()));
//                                        thumbnail.setImgName(thumbnail.getImgName());
//                                        thumbnail.setImgUrl(thumbnailUrl);
//                                        thumbnail.setShoeDetail(shoeDetail);
//                                        thumbnailRepository.save(thumbnail);
//                                    } catch (IOException e) {
//                                        return new ServiceResult<>(AppConstant.ERROR, "Error uploading thumbnail", null);
//                                    }
//                                }
//
//                                for (Image image : requestShoeDetail.getImages()) {
//                                    try {
//                                        String thumbnailUrl = imageUploadService.uploadImageByName(String.valueOf(image.getImgName()));
//                                        image.setImgName(image.getImgName());
//                                        image.setImgUrl(thumbnailUrl);
//                                        image.setShoeDetail(shoeDetail);
//                                        imageRepository.save(image);
//                                    } catch (IOException e) {
//                                        return new ServiceResult<>(AppConstant.ERROR, "Error uploading image", null);
//                                    }
//                                }
//                            } else {
//                                throw new RuntimeException("Màu sắc không tồn tại");
//                            }
//                        } else {
//                            throw new RuntimeException("Loại giày không tồn tại");
//                        }
//                    }else {
//                        throw new RuntimeException("Brand không tồn tại");
//                    }
//                }else {
//                    throw new RuntimeException("Size không tồn tại");
//                }
//            }
//            return new ServiceResult<>(AppConstant.SUCCESS, "Shoe added successfully", shoe);
//        } catch (Exception e) {
//            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
//            return new ServiceResult<>(AppConstant.BAD_REQUEST, e.getMessage(), null);
//        }
//    }

    @Override
    public ServiceResult<Shoe> resultValidate(String mess) {
        return new ServiceResult<>(AppConstant.FAIL, mess, null);
    }

    @Override
    public String validateNhanVien(ShoeRequest shoeRequest) {
        List<String> errorMessages = new ArrayList<>();
        if (shoeRequest.getName() == null || shoeRequest.getStatusShoe() == null) {
            errorMessages.add("Thông tin giày không được để trống");
        }
        for (ShoeDetail requestShoeDetail : shoeRequest.getShoeDetailList()) {
            if (requestShoeDetail.getPriceInput() == null) {
                errorMessages.add("Thuộc tính không được để trống");
            }
        }
        if (errorMessages.size() > 0) {
            return String.join(", ", errorMessages);
        } else {
            return null;
        }

    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public ServiceResult<Shoe> addNewShoe(ShoeRequest shoeRequest) {
        String result = validateNhanVien(shoeRequest);
        if (result != null) {
            return resultValidate(result);
        } else {
            try {
                Shoe shoe = createShoe(shoeRequest);
                for (ShoeDetail requestShoeDetail : shoeRequest.getShoeDetailList()) {
                    ShoeDetail shoeDetail = createShoeDetail(shoe, requestShoeDetail);
                    saveThumbnails(shoeDetail, requestShoeDetail.getThumbnails());
                    saveImages(shoeDetail, requestShoeDetail.getImages());
                }
                return new ServiceResult<>(AppConstant.SUCCESS, "Shoe added successfully", shoe);
            } catch (Exception e) {
                TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
                return new ServiceResult<>(AppConstant.BAD_REQUEST, e.getMessage(), null);
            }
        }

    }

//    @Override
//    public ServiceResult<List<ResultItem>> getAllShoeItems(int page, int size) {
//        Page<Tuple> tuplePage = shoeDetailRepository.getAllShoeDetail(PageRequest.of(page, size));
//        System.out.println("tuplePage"+tuplePage);
//        List<ResultItem> result = new ArrayList<>();
//        result = tuplePage.stream()
//                .map(tuple -> {
//                    Long id = tuple.get(0, BigInteger.class).longValue();
//                    String shoeName = tuple.get(1, String.class);
//                    Float sizeName = tuple.get(2, Double.class).floatValue();
//                    String categoryName = tuple.get(3, String.class);
//                    String brandName = tuple.get(4, String.class);
//                    String soleName = tuple.get(5, String.class);
//                    String colorName = tuple.get(6, String.class);
//                    String codeName = tuple.get(7, String.class);
//                    BigDecimal priceInput = tuple.get(8, BigDecimal.class);
//                    Integer qty = tuple.get(9, Integer.class);
//                    String thumbnail = tuple.get(10, String.class);
//                    return new ResultItem(id,shoeName,sizeName,categoryName,brandName
//                            ,soleName,colorName,codeName,priceInput,qty,thumbnail);
//                }).collect(Collectors.toList());
//        return new ServiceResult<>(AppConstant.SUCCESS,
//                "Successfully retrieved",
//                result
//        );
//    }

    @Override
    public ServiceResult<List<DataPaginate>> getAllShoeItemstest(int page, int size) {
        Page<ShoeDetail> shoeDetails = shoeDetailRepository.findAll(PageRequest.of(page, size)); // Lấy danh sách ShoeDetail từ cơ sở dữ liệu

        int current = shoeDetails.getNumber();
        int pageSize = shoeDetails.getSize();
        int pages = shoeDetails.getTotalPages();
        long total = shoeDetails.getTotalElements();

        Meta meta = new Meta();
        meta.setCurrent(current);
        meta.setPageSize(pageSize);
        meta.setPages(pages);
        meta.setTotal(total);

        List<ResultItem> resultItems = new ArrayList<>();

        for (ShoeDetail shoeDetail : shoeDetails) {
            ResultItem resultItem = new ResultItem();
            resultItem.setId(shoeDetail.getId());
            resultItem.setNameShoe(shoeDetail.getShoe().getName());
            resultItem.setSize(shoeDetail.getSize().getName());
            resultItem.setCategory(shoeDetail.getCategory().getName());
            resultItem.setBrand(shoeDetail.getBrand().getName());
            resultItem.setSole(shoeDetail.getSole().getName());
            resultItem.setColor(shoeDetail.getColor().getName());
            resultItem.setCode(shoeDetail.getCode());
            resultItem.setPriceInput(shoeDetail.getPriceInput());
            resultItem.setQty(shoeDetail.getQuantity());
            resultItem.setThumbnail(shoeDetail.getThumbnails().get(0).getImgUrl());

            List<String> images = getImagesForShoeDetail(shoeDetail.getId());
            resultItem.setImages(images);

            resultItems.add(resultItem);
        }
        DataPaginate dataPaginate = new DataPaginate();
        dataPaginate.setMeta(meta);
        dataPaginate.setResult(resultItems);

        return new ServiceResult(AppConstant.SUCCESS,
                "Successfully retrieved",
                dataPaginate
        );
    }

    public List<String> getImagesForShoeDetail(Long shoeDetailId) {
        List<Image> images = imageRepository.findByShoeDetailId(shoeDetailId);

        List<String> imageUrls = images.stream()
                .map(Image::getImgUrl)
                .collect(Collectors.toList());

        return imageUrls;
    }

    private Shoe createShoe(ShoeRequest shoeRequest) {
        Shoe shoe = new Shoe();
        Calendar cal = Calendar.getInstance();
        Date dateNow = cal.getTime();
        shoe.setName(shoeRequest.getName());
        shoe.setCreatedAt(dateNow);
        shoe.setUpdatedAt(dateNow);
        shoe.setStatus(shoeRequest.getStatusShoe());
        return shoeRepository.save(shoe);
    }

    private ShoeDetail createShoeDetail(Shoe shoe, ShoeDetail requestShoeDetail) {
        Optional<Color> optionalColor = colorRepository.findById(requestShoeDetail.getColor().getId());
        Optional<Category> optionalCategory = categoryRepository.findById(requestShoeDetail.getCategory().getId());
        Optional<Brand> optionalBrand = brandCategory.findById(requestShoeDetail.getBrand().getId());
        Optional<Size> optionalSize = sizeRepository.findById(requestShoeDetail.getSize().getId());
        Optional<Sole> optionalSole = soleRepository.findById(requestShoeDetail.getSole().getId());

        List<String> errors = new ArrayList<>();

        if (!optionalSize.isPresent()) {
            errors.add("Size không tồn tại");
        }

        if (!optionalSole.isPresent()) {
            errors.add("Sole không tồn tại");
        }

        if (!optionalBrand.isPresent()) {
            errors.add("Brand không tồn tại");
        }

        if (!optionalCategory.isPresent()) {
            errors.add("Loại giày không tồn tại");
        }

        if (!optionalColor.isPresent()) {
            errors.add("Màu sắc không tồn tại");
        }

        if (!errors.isEmpty()) {
            throw new RuntimeException(String.join(", ", errors));
        }

        ShoeDetail shoeDetail = new ShoeDetail();
        shoeDetail.setShoe(shoe);
        shoeDetail.setColor(optionalColor.get());
        shoeDetail.setCategory(optionalCategory.get());
        shoeDetail.setBrand(optionalBrand.get());
        shoeDetail.setSize(optionalSize.get());
        shoeDetail.setSole(optionalSole.get());
        shoeDetail.setPriceInput(requestShoeDetail.getPriceInput());
        shoeDetail.setQuantity(requestShoeDetail.getQuantity());
        shoeDetail.setCreatedAt(new Date());
        shoeDetail.setUpdatedAt(new Date());
        shoeDetail.setStatus(0);
        Float sizeName = optionalSize.get().getName();
        String shoeName = shoe.getName();
        String colorName = optionalColor.get().getName();
        shoeDetail.setCode(shoeName.toLowerCase() + " - " + colorName.toLowerCase() + " - " + sizeName);
        return shoeDetailRepository.save(shoeDetail);
    }

    private void saveThumbnails(ShoeDetail shoeDetail, List<Thumbnail> thumbnails) {
        for (Thumbnail thumbnail : thumbnails) {
            try {
                String thumbnailUrl = imageUploadService.uploadImageByName(String.valueOf(thumbnail.getImgName()));
                thumbnail.setImgName(thumbnail.getImgName());
                thumbnail.setImgUrl(thumbnailUrl);
                thumbnail.setShoeDetail(shoeDetail);
                thumbnailRepository.save(thumbnail);
            } catch (IOException e) {
                throw new RuntimeException("Error uploading thumbnail");
            }
        }
    }

    private void saveImages(ShoeDetail shoeDetail, List<Image> images) {
        for (Image image : images) {
            try {
                String thumbnailUrl = imageUploadService.uploadImageByName(String.valueOf(image.getImgName()));
                image.setImgName(image.getImgName());
                image.setImgUrl(thumbnailUrl);
                image.setShoeDetail(shoeDetail);
                imageRepository.save(image);
            } catch (IOException e) {
                throw new RuntimeException("Error uploading image");
            }
        }
    }


}

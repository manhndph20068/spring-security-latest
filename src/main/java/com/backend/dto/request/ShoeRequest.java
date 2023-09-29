package com.backend.dto.request;

import com.backend.entity.ShoeDetail;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class ShoeRequest {
    private String name;

    private Date createdAt;

    private Date updatedAt;

    private Integer statusShoe;

    private List<ShoeDetail> shoeDetailList;

//    private String thumbnail;
//
//    private List<String> imageList;
}

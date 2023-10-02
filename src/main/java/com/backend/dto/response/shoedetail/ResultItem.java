package com.backend.dto.response.shoedetail;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.List;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ResultItem {
    private Long id;
    private String nameShoe;
    private Float size;
    private String category;
    private String brand;
    private String sole;
    private String color;
    private String code;
    private BigDecimal priceInput;
    private Integer qty;
    private String thumbnail;
    private List<String> images;
}

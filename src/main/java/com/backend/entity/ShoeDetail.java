package com.backend.entity;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinColumns;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Table(name = "shoedetail")
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ShoeDetail {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "shoeid")
    private Shoe shoe;

    @ManyToOne
    @JoinColumn(name = "categoryid")
    private Category category;///

    @ManyToOne
    @JoinColumn(name = "sizeid")
    private Size size;

    @ManyToOne
    @JoinColumn(name = "colorid")
    private Color color;//

    @ManyToOne
    @JoinColumn(name = "soleid")
    private Sole sole;///

    @ManyToOne
    @JoinColumn(name = "brandid")
    private Brand brand;///

    @Column(name = "code")
    private String code;

    @Column(name = "qrcode")
    private String qrCode;

    @Column(name = "priceinput")
    private BigDecimal priceInput;

    @Column(name = "priceoutput")
    private BigDecimal priceOutput;

    @Column(name = "quantity")
    private Integer quantity;

    @Column(name = "createdby")
    private String createdBy;

    @Column(name = "updatedby")
    private String updatedBy;

    @Column(name = "createdat")
    private Date createdAt;

    @Column(name = "updatedat")
    private Date updatedAt;

    @Column(name = "status")
    private Integer status;

    @OneToMany(mappedBy = "shoeDetail")
    private List<Thumbnail> thumbnails;

    @OneToMany(mappedBy = "shoeDetail")
    private List<Image> images;

}

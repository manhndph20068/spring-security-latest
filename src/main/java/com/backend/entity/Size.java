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
import javax.persistence.OneToMany;
import javax.persistence.Table;
import java.util.Date;
import java.util.List;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Table(name = "Size")
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Size {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "name")
    private Float name;

    @Column(name = "createdat")
    private Date createdAt;

    @Column(name = "updatedat")
    private Date updatedAt;

    @Column(name = "status")
    private Integer status;

    @OneToMany(mappedBy = "size")
    private List<ShoeDetail> shoeDetails;
}

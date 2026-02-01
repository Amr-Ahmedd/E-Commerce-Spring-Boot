package com.project.ecommerce.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

@Entity
@Table(name = "product")
@Data
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    private Long price;

    @Lob
    private String description;


    @ManyToOne(fetch = FetchType.EAGER, optional = true)
    @JoinColumn(name = "category_id", nullable = true)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JsonIgnore
    private Category category;

    // new fields for grocery
    @Column(unique = true)
    private String barcode;

    private String brand;

    private String imageUrl;

    @Lob
    @Column(columnDefinition ="Longblob")
    private byte[] img;


    private Double calories;

    @Lob
    private String nutrientsJson;

    private Boolean approved = false;

}

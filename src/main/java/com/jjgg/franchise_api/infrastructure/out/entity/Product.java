package com.jjgg.franchise_api.infrastructure.out.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "products")
public class Product {

    @Id
    @Column("product_id")
    private Long id;

    @Column("name")
    private String name;

    @Column("stock")
    private Integer stock;

    @Column("branch_id")
    private Long branchId;

    public void setStock(Integer stock) {
        if (stock == null || stock < 0) {
            throw new IllegalArgumentException("stock must be >= 0");
        }
        this.stock = stock;
    }
}


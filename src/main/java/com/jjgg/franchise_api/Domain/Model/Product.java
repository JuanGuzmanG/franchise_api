package com.jjgg.franchise_api.Domain.Model;

public class Product {

    private Long id;
    private String name;
    private Integer stock;

    public Product() {
    }

    public Product(Long id, String name, Integer stock) {
        this.id = id;
        this.name = name;
        setStock(stock);
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getStock() {
        return stock;
    }

    public void setStock(Integer stock) {
        if (stock == null || stock < 0) {
            throw new IllegalArgumentException("stock must be >= 0");
        }
        this.stock = stock;
    }
}


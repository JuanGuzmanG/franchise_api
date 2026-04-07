package com.jjgg.franchise_api.Domain.Model;

import java.util.ArrayList;
import java.util.List;

public class Branch {

    private Long id;
    private String name;
    private List<Product> products;

    public Branch() {
        this.products = new ArrayList<>();
    }

    public Branch(Long id, String name, List<Product> products) {
        this.id = id;
        this.name = name;
        this.products = products == null ? new ArrayList<>() : products;
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

    public List<Product> getProducts() {
        return products;
    }

    public void setProducts(List<Product> products) {
        this.products = products == null ? new ArrayList<>() : products;
    }

    public void addProduct(Product product) {
        this.products.add(product);
    }

    public void removeProductById(Long productId) {
        this.products.removeIf(product -> product.getId() != null && product.getId().equals(productId));
    }
}


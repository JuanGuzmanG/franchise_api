package com.jjgg.franchise_api.Domain.Model;

import java.util.ArrayList;
import java.util.List;

public class Franchise {

    private Long id;
    private String name;
    private String description;
    private List<Branch> branches;

    public Franchise() {
        this.branches = new ArrayList<>();
    }

    public Franchise(Long id, String name, String description, List<Branch> branches) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.branches = branches == null ? new ArrayList<>() : branches;
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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public List<Branch> getBranches() {
        return branches;
    }

    public void setBranches(List<Branch> branches) {
        this.branches = branches == null ? new ArrayList<>() : branches;
    }

    public void addBranch(Branch branch) {
        this.branches.add(branch);
    }

    public void removeBranchById(Long branchId) {
        this.branches.removeIf(branch -> branch.getId() != null && branch.getId().equals(branchId));
    }
}


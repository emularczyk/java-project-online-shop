package com.candyshop.islodycze.mainPage;

import org.springframework.data.domain.Sort;

import java.io.Serializable;

public class ProductPage implements Serializable {

    private int pageNumber = 0;
    private int pageSize = 10;
    private Sort.Direction sortingDirection = Sort.Direction.ASC;
    private String sortBy = "productName";

    public ProductPage(int pageNumber, int pageSize, Sort.Direction sortingDirection, String sortBy) {
        this.pageNumber = pageNumber;
        this.pageSize = pageSize;
        this.sortingDirection = sortingDirection;
        this.sortBy = sortBy;
    }

    public ProductPage() {
        this.pageNumber = 0;
        this.pageSize = 10;
        this.sortingDirection = Sort.Direction.ASC;
        this.sortBy = "productName";
    }

    public int getPageNumber() {
        return pageNumber;
    }

    public void setPageNumber(int pageNumber) {
        if (pageNumber > 0) {
            this.pageNumber = pageNumber;
        }
    }

    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        if (pageSize > 0) {
            this.pageSize = pageSize;
        }
    }

    public Sort.Direction getSortingDirection() {
        return sortingDirection;
    }

    public void setSortingDirection(Sort.Direction sortingDirection) {
        this.sortingDirection = sortingDirection;
    }

    public String getSortBy() {
        return sortBy;
    }

    public void setSortBy(String sortBy) {
        this.sortBy = sortBy;
    }

    public void changeSortingDirection() {
        if (this.sortingDirection == Sort.Direction.ASC) {
            this.sortingDirection = Sort.Direction.DESC;
        } else {
            this.sortingDirection = Sort.Direction.ASC;
        }
    }
}

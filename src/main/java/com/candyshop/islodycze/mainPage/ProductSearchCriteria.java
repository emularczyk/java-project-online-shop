package com.candyshop.islodycze.mainPage;

import lombok.experimental.Accessors;

import java.util.List;

@Accessors(chain = true)
public class ProductSearchCriteria {

    private String searchedPhrase;
    public List<String> searchedCategories;
    private int searchedPriceLimit;
    private boolean showOnlyAvailable;

    public ProductSearchCriteria() {
        this.searchedPhrase = "";
        this.searchedPriceLimit = 1000;
        this.showOnlyAvailable = false;
    }

    public String getSearchedPhrase() {
        return searchedPhrase;
    }

    public void setSearchedPhrase(String searchedPhrase) {
        this.searchedPhrase = searchedPhrase;
    }

    public List<String> getSearchedCategories() {
        return searchedCategories;
    }

    public void setSearchedCategories(List<String> searchedCategories) {
        this.searchedCategories = searchedCategories;
    }

    public int getSearchedPriceLimit() {
        return searchedPriceLimit;
    }

    public void setSearchedPriceLimit(int searchedPriceLimit) {
        this.searchedPriceLimit = searchedPriceLimit;
    }

    public boolean isShowOnlyAvailable() {
        return showOnlyAvailable;
    }

    public void setShowOnlyAvailable(boolean showOnlyAvailable) {
        this.showOnlyAvailable = showOnlyAvailable;
    }
}

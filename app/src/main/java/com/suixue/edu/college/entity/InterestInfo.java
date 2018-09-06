package com.suixue.edu.college.entity;

import java.util.List;

/**
 * 兴趣爱好页面使用
 * Created by cuiyan on 2018/9/6.
 */
public class InterestInfo {
    private String categoryId;
    private String categoryName;
    private List<InterestInfo> subCategoryList;
    // 客户端本地自定义字段，用于标识该项是否被选中
    private boolean isChecked;
    // 客户端本地自定义字段，用于标识是否为子类别
    private boolean isChildCategory;
    // 客户端本地自定义字段，用于InterestAdapter区分View类型
    private boolean isLocalCategoryTitle;
    // 客户端自定义字段，存储item背景色
    private int bgColor;
    // 客户端自定义字段，标识子类数据是否已填充到RecyclerView
    private boolean isChildAdded;

    public String getCategoryId() {
        return categoryId;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public List<InterestInfo> getSubCategoryList() {
        return subCategoryList;
    }

    public boolean isChecked() {
        return isChecked;
    }

    public boolean isChildCategory() {
        return isChildCategory;
    }

    public boolean isLocalCategoryTitle() {
        return isLocalCategoryTitle;
    }

    public int getBgColor() {
        return bgColor;
    }

    public boolean isChildAdded() {
        return isChildAdded;
    }
    //////////////////////////////

    public void setCategoryId(String categoryId) {
        this.categoryId = categoryId;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public void setSubCategoryList(List<InterestInfo> subCategoryList) {
        this.subCategoryList = subCategoryList;
    }

    public void setChecked(boolean checked) {
        isChecked = checked;
    }

    public void setChildCategory(boolean childCategory) {
        isChildCategory = childCategory;
    }

    public void setLocalCategoryTitle(boolean localCategoryTitle) {
        isLocalCategoryTitle = localCategoryTitle;
    }

    public void setBgColor(int bgColor) {
        this.bgColor = bgColor;
    }

    public void setChildAdded(boolean childAdded) {
        isChildAdded = childAdded;
    }
}

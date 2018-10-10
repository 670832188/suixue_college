package com.suixue.edu.college.entity;

import java.util.List;

/**
 * Created by cuiyan on 2018/10/10.
 */
public class BaseListResult<T> {
    private List<T> dataList;
    private boolean hasMoreData;
    private int currentPageIndex;

    public List<T> getDataList() {
        return dataList;
    }

    public boolean isHasMoreData() {
        return hasMoreData;
    }

    public int getCurrentPageIndex() {
        return currentPageIndex;
    }

    ////////////////


    public void setDataList(List<T> dataList) {
        this.dataList = dataList;
    }

    public void setHasMoreData(boolean hasMoreData) {
        this.hasMoreData = hasMoreData;
    }

    public void setCurrentPageIndex(int currentPageIndex) {
        this.currentPageIndex = currentPageIndex;
    }
}

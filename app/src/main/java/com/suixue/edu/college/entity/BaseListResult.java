package com.suixue.edu.college.entity;

import java.util.List;

/**
 * Created by cuiyan on 2018/10/10.
 */
public class BaseListResult<T> {
    private List<T> dataList;
    private boolean hasMoreData;

    public List<T> getDataList() {
        return dataList;
    }

    public boolean isHasMoreData() {
        return hasMoreData;
    }
}

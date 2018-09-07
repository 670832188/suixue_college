package com.suixue.edu.college.entity;

import java.util.List;

/**
 * Created by cuiyan on 2018/9/7.
 */
public class InterestResult {
    private List<InterestInfo> majorInterestList;
    private List<InterestInfo> lifeInterestList;

    public List<InterestInfo> getMajorInterestList() {
        return majorInterestList;
    }

    public List<InterestInfo> getLifeInterestList() {
        return lifeInterestList;
    }

    public void setMajorInterestList(List<InterestInfo> majorInterestList) {
        this.majorInterestList = majorInterestList;
    }

    public void setLifeInterestList(List<InterestInfo> lifeInterestList) {
        this.lifeInterestList = lifeInterestList;
    }
}

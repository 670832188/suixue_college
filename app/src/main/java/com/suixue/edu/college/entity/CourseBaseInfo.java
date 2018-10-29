package com.suixue.edu.college.entity;

import java.util.List;

/**
 * 每个学年的课程基本信息
 * Created by cuiyan on 2018/10/29.
 */
public class CourseBaseInfo {
    // 年份
    private String year;
    // 年级
    private String grade;
    // 专业
    private String major;
    private List<CourseInfo> courseInfoList;
    public static class CourseInfo {
        private String name;
        private String id;

        public String getName() {
            return name;
        }

        public String getId() {
            return id;
        }

        //////////////

        public void setName(String name) {
            this.name = name;
        }

        public void setId(String id) {
            this.id = id;
        }
    }

    ////////////////////////

    public void setYear(String year) {
        this.year = year;
    }

    public void setGrade(String grade) {
        this.grade = grade;
    }

    public void setMajor(String major) {
        this.major = major;
    }

    public void setCourseInfoList(List<CourseInfo> courseInfoList) {
        this.courseInfoList = courseInfoList;
    }
}

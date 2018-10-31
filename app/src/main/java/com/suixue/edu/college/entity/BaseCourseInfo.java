package com.suixue.edu.college.entity;

import java.util.List;

/**
 * 每个学年的课程基本信息
 * Created by cuiyan on 2018/10/29.
 */
public class BaseCourseInfo {
    // 年份
    private String year;
    // 年级
    private String grade;
    // 年级id
    private String gradeId;
    // 专业
    private String major;
    private List<CourseInfo> courseInfoList;

    public String getYear() {
        return year;
    }

    public String getGrade() {
        return grade;
    }

    public String getGradeId() {
        return gradeId;
    }

    public String getMajor() {
        return major;
    }

    public List<CourseInfo> getCourseInfoList() {
        return courseInfoList;
    }

////////////////////////

    public void setYear(String year) {
        this.year = year;
    }

    public void setGrade(String grade) {
        this.grade = grade;
    }

    public void setGradeId(String gradeId) {
        this.gradeId = gradeId;
    }

    public void setMajor(String major) {
        this.major = major;
    }

    public void setCourseInfoList(List<CourseInfo> courseInfoList) {
        this.courseInfoList = courseInfoList;
    }

    public static class CourseInfo {
        private String name;
        private String id;
        private String gradeId;

        public String getName() {
            return name;
        }

        public String getId() {
            return id;
        }

        public String getGradeId() {
            return gradeId;
        }

        //////////////

        public void setName(String name) {
            this.name = name;
        }

        public void setId(String id) {
            this.id = id;
        }

        public void setGradeId(String gradeId) {
            this.gradeId = gradeId;
        }
    }

}

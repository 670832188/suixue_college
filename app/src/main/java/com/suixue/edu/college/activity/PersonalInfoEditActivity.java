package com.suixue.edu.college.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.dev.kit.basemodule.activity.BaseActivity;
import com.suixue.edu.college.R;
import com.suixue.edu.college.config.Constants;
import com.suixue.edu.college.util.PickerUtil;
import com.suixue.edu.college.widget.BottomSelectorDialog;

import java.util.Calendar;
import java.util.Locale;

import cn.qqtheme.framework.picker.DatePicker;

/**
 * Created by cuiyan on 2018/11/2.
 */
public class PersonalInfoEditActivity extends BaseActivity implements View.OnClickListener {
    private BottomSelectorDialog genderDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_personal_info_edit);
        init();
    }

    private void init() {
        setText(R.id.tv_title, R.string.personal_info);
        setText(R.id.tv_right, R.string.action_edit);
        setVisibility(R.id.tv_right, View.VISIBLE);
        setOnClickListener(R.id.iv_left, this);
        setOnClickListener(R.id.tv_right, this);
        setOnClickListener(R.id.tv_user_birthday, this);
        setOnClickListener(R.id.tv_user_gender, this);
        setOnClickListener(R.id.tv_user_grade, this);
        setOnClickListener(R.id.tv_college_start_year, this);
    }

    private void getPersonalInfo() {

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_left: {
                finish();
                break;
            }
            case R.id.tv_right: {
                startEditOrSave();
                break;
            }
            case R.id.tv_user_birthday: {
//                PickerUtil.showDatePicker();
                Calendar calendar = Calendar.getInstance(Locale.CHINESE);
                int[] startDate = {calendar.get(Calendar.YEAR) - Constants.MAX_AGE, calendar.get(Calendar.MONTH) + 1, calendar.get(Calendar.DATE)};
                int[] selectedDate = {calendar.get(Calendar.YEAR) - 18, 1, 1};
                int[] endDate = {calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH) + 1, calendar.get(Calendar.DATE)};
                PickerUtil.showDatePicker(startDate, endDate, selectedDate, this, new DatePicker.OnYearMonthDayPickListener() {
                    @Override
                    public void onDatePicked(String year, String month, String day) {

                    }
                });
                break;
            }
            case R.id.tv_user_gender: {
                break;
            }
            case R.id.tv_user_grade: {
                break;
            }
            case R.id.tv_college_start_year: {
                break;
            }
        }
    }

    private void startEditOrSave() {
        TextView tvEdit = findViewById(R.id.tv_right);
        boolean enabled;
        if (getString(R.string.action_edit).equals(tvEdit.getText().toString())) {
            setText(R.id.tv_right, R.string.action_save);
            enabled = true;
        } else {
            setText(R.id.tv_right, R.string.action_edit);
            enabled = false;
        }
        setEnabled(R.id.et_user_name, enabled);
        setEnabled(R.id.et_college_name, enabled);
        setEnabled(R.id.iv_left, enabled);
        setEnabled(R.id.tv_user_birthday, enabled);
        setEnabled(R.id.tv_user_gender, enabled);
        setEnabled(R.id.tv_user_grade, enabled);
        setEnabled(R.id.tv_college_start_year, enabled);
    }
}

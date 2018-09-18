package com.suixue.edu.college.activity;

import android.content.Intent;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.os.Bundle;
import android.text.Html;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.dev.kit.basemodule.activity.BaseStateViewActivity;
import com.dev.kit.basemodule.activity.RecordVideoActivity;
import com.dev.kit.basemodule.util.LogUtil;
import com.suixue.edu.college.R;
import com.suixue.edu.college.config.TextStyleConfig;
import com.vincent.filepicker.Constant;
import com.vincent.filepicker.activity.ImagePickActivity;
import com.vincent.filepicker.activity.VideoPickActivity;

import static com.vincent.filepicker.activity.BaseActivity.IS_NEED_FOLDER_LIST;
import static com.vincent.filepicker.activity.ImagePickActivity.IS_NEED_CAMERA;

/**
 * Created by cuiyan on 2018/9/17.
 */
public class PublishBlogActivity extends BaseStateViewActivity implements View.OnClickListener {

    private EditText etContent;
    private RadioGroup rgFontSize;
    private RadioGroup rgFontAlign;
    private CheckBox ckbFontBold;
    private CheckBox ckbFontItalic;
    private CheckBox ckbFontUnderline;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        showContent(true);
        super.onCreate(savedInstanceState);
        init();
    }

    @Override
    public View createContentView(LayoutInflater inflater, ViewGroup contentRoot) {
        return inflater.inflate(R.layout.activity_publish_blog, contentRoot, false);
    }

    private void init() {
        etContent = findViewById(R.id.et_content);
        rgFontSize = findViewById(R.id.rg_font_size);
        rgFontAlign = findViewById(R.id.rg_font_align);
        ckbFontBold = findViewById(R.id.ckb_font_bold);
        ckbFontItalic = findViewById(R.id.ckb_font_italic);
        ckbFontUnderline = findViewById(R.id.ckb_font_underline);
        registerTestStyleListener();
        final TextView tvTest = findViewById(R.id.tv_test);
        setOnClickListener(R.id.btn_add_text, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tvTest.setText(Html.fromHtml(getHtmlString()));
            }
        });
        setOnClickListener(R.id.iv_add_text_trigger, this);
        setOnClickListener(R.id.iv_add_img_trigger, this);
        setOnClickListener(R.id.iv_add_video_trigger, this);
    }

    /**
     * 设置字体大小的监听
     */
    private void registerTestStyleListener() {
        ckbFontBold.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    if (ckbFontItalic.isChecked()) {
                        etContent.setTypeface(Typeface.defaultFromStyle(Typeface.BOLD_ITALIC));
                    } else {
                        etContent.setTypeface(Typeface.DEFAULT_BOLD);
                    }
                } else {
                    if (ckbFontItalic.isChecked()) {
                        etContent.setTypeface(Typeface.defaultFromStyle(Typeface.ITALIC));
                    } else {
                        etContent.setTypeface(Typeface.DEFAULT);
                    }
                }
            }
        });
        ckbFontItalic.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    if (ckbFontBold.isChecked()) {
                        etContent.setTypeface(Typeface.defaultFromStyle(Typeface.BOLD_ITALIC));
                    } else {
                        etContent.setTypeface(Typeface.defaultFromStyle(Typeface.ITALIC));
                    }
                } else {
                    if (ckbFontBold.isChecked()) {
                        etContent.setTypeface(Typeface.DEFAULT_BOLD);
                    } else {
                        etContent.setTypeface(Typeface.DEFAULT);
                    }
                }
            }
        });
        ckbFontUnderline.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    etContent.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG);
                } else {
                    etContent.getPaint().setFlags(0);
                }
                etContent.setText(etContent.getText().toString());
                etContent.setSelection(etContent.getText().toString().length());
            }
        });

        rgFontSize.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.rb_font_size_large: {
                        etContent.setTextSize(TextStyleConfig.FontSize.SIZE_18);
                        break;
                    }
                    case R.id.rb_font_size_normal: {
                        etContent.setTextSize(TextStyleConfig.FontSize.SIZE_16);
                        break;
                    }
                    case R.id.rb_font_size_small: {
                        etContent.setTextSize(TextStyleConfig.FontSize.SIZE_14);
                        break;
                    }
                }
            }
        });

        rgFontAlign.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.rb_font_align_left: {
                        etContent.setGravity(Gravity.START);
                        break;
                    }
                    case R.id.rb_font_align_center: {
                        etContent.setGravity(Gravity.CENTER_HORIZONTAL);
                        break;
                    }
                    case R.id.rb_font_align_right: {
                        etContent.setGravity(Gravity.END);
                        break;
                    }
                }
            }
        });
    }


    /**
     * 获取数据
     */
    private String getHtmlStr() {
        String style = "";
        String content = etContent.getText().toString().trim();
        int checkedFontSizeId = rgFontSize.getCheckedRadioButtonId();
        if (checkedFontSizeId == R.id.rb_font_size_large) {
            style += TextStyleConfig.FontSize.KEY_SIZE_18;
        } else if (checkedFontSizeId == R.id.rb_font_size_normal) {
            style += TextStyleConfig.FontSize.KEY_SIZE_16;
        } else if (checkedFontSizeId == R.id.rb_font_size_small) {
            style += TextStyleConfig.FontSize.KEY_SIZE_14;
        }

        int checkedAlignmentId = rgFontAlign.getCheckedRadioButtonId();
        if (checkedAlignmentId == R.id.rb_font_align_center) {
            style += TextStyleConfig.FontAlign.KEY_ALIGN_CENTER;
        } else if (checkedAlignmentId == R.id.rb_font_align_right) {
            style += TextStyleConfig.FontAlign.KEY_ALIGN_RIGHT;
        } else if (checkedAlignmentId == R.id.rb_font_align_left) {
            style += TextStyleConfig.FontAlign.KEY_ALIGN_LEFT;
        }

        if (ckbFontBold.isChecked()) {
            style += TextStyleConfig.FontBold.KEY_STYLE_BOLD;//加粗
        }
        if (ckbFontItalic.isChecked()) {
            style += TextStyleConfig.FontBold.KEY_STYLE_ITALIC;//斜体
        }
        if (ckbFontUnderline.isChecked()) {
            style += TextStyleConfig.FontBold.KEY_STYLE_UNDERLINE;//下划线
        }
        String htmlStr = "<p style='" + style + "' >" + content + "</p>";
        LogUtil.e("mytag", "getHtmlData: " + htmlStr);
        return htmlStr;
    }

    private String getHtmlString() {
        String content = etContent.getText().toString();
        String startTag;
        String endTag = "</p>";
        switch (rgFontAlign.getCheckedRadioButtonId()) {
            case R.id.rb_font_align_left: {
                startTag = "<p style='text-align: start;'>";
                break;
            }
            case R.id.rb_font_align_center: {
                startTag = "<p style='text-align: center;'>";
                break;
            }
            case R.id.rb_font_align_right: {
                startTag = "<p style='text-align: end;'>";
                break;
            }
            default: {
                startTag = "<p>";
                break;
            }
        }
        if (ckbFontBold.isChecked()) {
            startTag += "<b>";
            endTag = "</b>" + endTag;
        }
        if (ckbFontItalic.isChecked()) {
            startTag += "<i>";
            endTag = "</i>" + endTag;
        }
        if (ckbFontUnderline.isChecked()) {
            startTag += "<u>";
            endTag = "</u>" + endTag;
        }
        String htmlStr = startTag + content + endTag;
        LogUtil.e("mytag", "htmlString: " + htmlStr);
        return htmlStr;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_add_text: {
                break;
            }
            case R.id.iv_add_text_trigger: {
                setVisibility(R.id.ll_text_edit, View.VISIBLE);
                break;
            }
            case R.id.iv_add_img_trigger: {
                startPictureSelect();
                break;
            }
            case R.id.iv_add_video_trigger: {
                startVideoSelect();
                break;
            }
        }
    }

    private void startPictureSelect() {
        Intent intent1 = new Intent(this, ImagePickActivity.class);
        intent1.putExtra(IS_NEED_CAMERA, true);
        intent1.putExtra(Constant.MAX_NUMBER, 9);
        intent1.putExtra(IS_NEED_FOLDER_LIST, true);
        startActivityForResult(intent1, Constant.REQUEST_CODE_PICK_IMAGE);
    }

    private void startVideoSelect() {
        Intent intent = new Intent(this, VideoPickActivity.class);
        intent.putExtra(IS_NEED_CAMERA, true);
        intent.putExtra(Constant.MAX_NUMBER, 1);
        intent.putExtra(IS_NEED_FOLDER_LIST, true);
        startActivityForResult(intent, Constant.REQUEST_CODE_PICK_VIDEO);
    }
}

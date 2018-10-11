package com.suixue.edu.college.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.media.MediaMetadataRetriever;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomSheetDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.dev.kit.basemodule.activity.BaseStateViewActivity;
import com.dev.kit.basemodule.activity.RecordVideoActivity;
import com.dev.kit.basemodule.surpport.RecyclerDividerDecoration;
import com.dev.kit.basemodule.util.Config;
import com.dev.kit.basemodule.util.DisplayUtil;
import com.dev.kit.basemodule.util.ImageUtil;
import com.dev.kit.basemodule.util.LogUtil;
import com.dev.kit.basemodule.videoCompress.IVideoCompress;
import com.dev.kit.basemodule.videoCompress.VideoCompressUtil;
import com.dev.kit.basemodule.view.NetProgressDialog;
import com.google.gson.Gson;
import com.suixue.edu.college.R;
import com.suixue.edu.college.adapter.PublishBlogAdapter;
import com.suixue.edu.college.config.Constants;
import com.suixue.edu.college.config.TextStyleConfig;
import com.suixue.edu.college.entity.BlogContentInfo;
import com.suixue.edu.college.widget.BottomSelectorDialog;
import com.vincent.filepicker.Constant;
import com.vincent.filepicker.activity.ImagePickActivity;
import com.vincent.filepicker.filter.entity.ImageFile;
import com.vincent.filepicker.filter.entity.VideoFile;
import com.vincent.filepicker.utils.VideoSelectBuilder;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import me.shaohui.advancedluban.OnMultiCompressListener;

import static com.dev.kit.basemodule.activity.RecordVideoActivity.RECODE_FILE_PATH;
import static com.dev.kit.basemodule.activity.RecordVideoActivity.VIDEO_HEIGHT;
import static com.dev.kit.basemodule.activity.RecordVideoActivity.VIDEO_WIDTH;
import static com.vincent.filepicker.Constant.REQUEST_CODE_TAKE_VIDEO;
import static com.vincent.filepicker.activity.BaseActivity.IS_NEED_FOLDER_LIST;
import static com.vincent.filepicker.activity.ImagePickActivity.IS_NEED_CAMERA;

/**
 * Created by cuiyan on 2018/9/17.
 */
public class PublishBlogActivity extends BaseStateViewActivity implements View.OnClickListener {

    private TextView tvPublishTrigger;
    private EditText etContent;
    private RadioGroup rgFontSize;
    private RadioGroup rgFontAlign;
    private CheckBox ckbFontBold;
    private CheckBox ckbFontItalic;
    private CheckBox ckbFontUnderline;
    private View textEditView;
    private PublishBlogAdapter adapter;
    private RecyclerView rvBlogContent;
    private LinearLayoutManager layoutManager;
    private RecyclerView.AdapterDataObserver dataObserver;
    private NetProgressDialog progressDialog;
    private BottomSheetDialog videoSelectorDialog;

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
        setText(R.id.tv_title, R.string.title_publish_blog);
        rvBlogContent = findViewById(R.id.rv_blog_content);
        layoutManager = new LinearLayoutManager(this);
        rvBlogContent.setLayoutManager(layoutManager);
        rvBlogContent.addItemDecoration(new RecyclerDividerDecoration(RecyclerDividerDecoration.DIVIDER_TYPE_HORIZONTAL, getResources().getColor(R.color.color_common_ashen), DisplayUtil.dp2px(5)));
        adapter = new PublishBlogAdapter(this, new ArrayList<BlogContentInfo>());
        rvBlogContent.setAdapter(adapter);
        etContent = findViewById(R.id.et_content);
        rgFontSize = findViewById(R.id.rg_font_size);
        rgFontAlign = findViewById(R.id.rg_font_align);
        ckbFontBold = findViewById(R.id.ckb_font_bold);
        ckbFontItalic = findViewById(R.id.ckb_font_italic);
        ckbFontUnderline = findViewById(R.id.ckb_font_underline);
        textEditView = findViewById(R.id.ll_text_edit);
        tvPublishTrigger = findViewById(R.id.tv_right);
        tvPublishTrigger.setText(R.string.action_publish);
        tvPublishTrigger.setVisibility(View.VISIBLE);
        tvPublishTrigger.setTextColor(getResources().getColor(R.color.color_common_ashen));
        tvPublishTrigger.setEnabled(false);
        registerTestStyleListener();
        setOnClickListener(R.id.tv_right, this);
        setOnClickListener(R.id.btn_add_text, this);
        setOnClickListener(R.id.iv_add_text_trigger, this);
        setOnClickListener(R.id.iv_add_img_trigger, this);
        setOnClickListener(R.id.iv_add_video_trigger, this);
        setOnClickListener(R.id.iv_add_gif_trigger, this);
        registerTestStyleListener();
        registerDataObserver();
        progressDialog = NetProgressDialog.getInstance(this);
        videoSelectorDialog = new BottomSelectorDialog(this) {
            @NonNull
            @Override
            public View createRealContentView(FrameLayout rootContainer) {
                View contentView = LayoutInflater.from(PublishBlogActivity.this).inflate(R.layout.video_source_selector, rootContainer, false);
                contentView.findViewById(R.id.tv_select_by_camera).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        videoSelectorDialog.dismiss();
                        Intent intent = new Intent(PublishBlogActivity.this, RecordVideoActivity.class);
                        startActivityForResult(intent, REQUEST_CODE_TAKE_VIDEO);
                    }
                });
                contentView.findViewById(R.id.tv_select_by_gallery).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        videoSelectorDialog.dismiss();
                        startVideoSelect();
                    }
                });
                return contentView;
            }
        };
    }

    private void registerDataObserver() {
        dataObserver = new RecyclerView.AdapterDataObserver() {
            @Override
            public void onChanged() {
                boolean enabled = adapter.getItemCount() > 0;
                int color = enabled ? getResources().getColor(R.color.color_common_white) : getResources().getColor(R.color.color_common_ashen);
                tvPublishTrigger.setEnabled(enabled);
                tvPublishTrigger.setTextColor(color);
            }
        };
        adapter.registerAdapterDataObserver(dataObserver);
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
        content = content.replace("\n", "<br>");
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
        LogUtil.e("mytag", "content: " + content);
        etContent.setText("");
        return htmlStr;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_right: {
                tryToPublish();
                break;
            }
            case R.id.btn_add_text: {
                BlogContentInfo info = new BlogContentInfo(BlogContentInfo.CONTENT_TYPE_TEXT, getHtmlString());
                adapter.appendItem(info, true);
//                layoutManager.scrollToPositionWithOffset(adapter.getItemCount() - 1, 0);
                scrollBlogItem(adapter.getItemCount() - 1);
                setVisibility(R.id.ll_text_edit, View.GONE);
                break;
            }
            case R.id.iv_add_text_trigger: {
                textEditView.setVisibility(textEditView.getVisibility() == View.VISIBLE ? View.GONE : View.VISIBLE);
                break;
            }
            case R.id.iv_add_img_trigger: {
                textEditView.setVisibility(View.GONE);
                startPictureSelect();
                break;
            }
            case R.id.iv_add_video_trigger: {
                textEditView.setVisibility(View.GONE);
                if (adapter.getVideoSize() > 0) {
                    showToast(R.string.tip_video_publish_size_limit);
                    return;
                }
                videoSelectorDialog.show();
                break;
            }
            case R.id.iv_add_gif_trigger: {
                textEditView.setVisibility(View.GONE);
                startGifVideoSelect();
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
        new VideoSelectBuilder(this, Constant.REQUEST_CODE_PICK_VIDEO)
                .isTakenAutoSelected(true)
                .needCamera(false)
                .onlyMp4(true)
                .setMaxSelectNumber(1)
                .setMinDuration(1)
                .setMaxDuration(15)
                .start();
    }

    private void startGifVideoSelect() {
        new VideoSelectBuilder(this, Constant.REQUEST_CODE_GIF_VIDEO)
                .isTakenAutoSelected(true)
                .needCamera(false)
                .onlyMp4(true)
                .setMaxSelectNumber(1)
                .setMinDuration(1)
                .setMaxDuration(15)
                .start();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case Constant.REQUEST_CODE_PICK_IMAGE: {
                    List<ImageFile> imageFileList = data.getParcelableArrayListExtra(Constant.RESULT_PICK_IMAGE);
                    handleSelectedImage(imageFileList);
                    break;
                }
                case Constant.REQUEST_CODE_PICK_VIDEO: {
                    ArrayList<VideoFile> videoFileList = data.getParcelableArrayListExtra(Constant.RESULT_PICK_VIDEO);
                    handleVideo(videoFileList.get(0));
                    break;
                }
                case Constant.REQUEST_CODE_GIF_VIDEO: {
                    ArrayList<VideoFile> videoFileList = data.getParcelableArrayListExtra(Constant.RESULT_PICK_VIDEO);
                    Intent intent = new Intent(this, VideoEditActivity.class);
                    intent.putExtra(VideoEditActivity.MAX_ENABLED_TIME, 3);
                    intent.putExtra(VideoEditActivity.CONVERT_TO_GIF, true);
                    intent.putExtra(VideoEditActivity.TARGET_VIDEO_PATH, videoFileList.get(0).getPath());
                    startActivityForResult(intent, Constants.REQUEST_CODE_CONVERT_GIF);
                    break;
                }
                case Constants.REQUEST_CODE_CONVERT_GIF: {
                    String outputGifPath = data.getStringExtra(VideoEditActivity.OUTPUT_FILE_PATH);
                    int width = data.getIntExtra(VideoEditActivity.WIDTH, 0);
                    int height = data.getIntExtra(VideoEditActivity.HEIGHT, 0);
                    BlogContentInfo info = new BlogContentInfo(BlogContentInfo.CONTENT_TYPE_PICTURE, outputGifPath);
                    info.setWidth(width);
                    info.setHeight(height);
                    adapter.appendItem(info, false);
                    scrollBlogItem(adapter.getItemCount() - 1);
                    break;
                }
                case REQUEST_CODE_TAKE_VIDEO: {
                    String videoPath = data.getStringExtra(RECODE_FILE_PATH);
                    int width = data.getIntExtra(VIDEO_WIDTH, 0);
                    int height = data.getIntExtra(VIDEO_HEIGHT, 0);
                    LogUtil.e("mytag", "path: " + videoPath + " " + width + " " + height);
                    BlogContentInfo info = new BlogContentInfo(BlogContentInfo.CONTENT_TYPE_VIDEO, videoPath);
                    info.setWidth(width);
                    info.setHeight(height);
                    adapter.appendItem(info, false);
                    scrollBlogItem(adapter.getItemCount() - 1);
                }
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    private void scrollBlogItem(int targetPosition) {
        layoutManager.scrollToPositionWithOffset(targetPosition, 0);
    }

    private void tryToPublish() {
        LogUtil.e("mytag", "publishContent: " + new Gson().toJson(adapter.getDataList()));

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (adapter != null && dataObserver != null) {
            adapter.unregisterAdapterDataObserver(dataObserver);
        }
    }

    private void handleSelectedImage(List<ImageFile> imageFileList) {
        final List<BlogContentInfo> blogContentInfoList = new ArrayList<>();
        List<String> imgPathList = new ArrayList<>();
        for (ImageFile imageFile : imageFileList) {
            String imgPath = imageFile.getPath();
            imgPathList.add(imgPath);
        }
        ImageUtil.compressImgByPaths(this, imgPathList, new OnMultiCompressListener() {
            @Override
            public void onStart() {
                progressDialog.show();
            }

            @Override
            public void onSuccess(List<File> fileList) {
                progressDialog.dismiss();
                for (File file : fileList) {
                    BlogContentInfo info = new BlogContentInfo(BlogContentInfo.CONTENT_TYPE_PICTURE, file.getAbsolutePath());
                    Bitmap bitmap = BitmapFactory.decodeFile(file.getAbsolutePath());
                    info.setWidth(bitmap.getWidth());
                    info.setHeight(bitmap.getHeight());
                    blogContentInfoList.add(info);
                }
                adapter.appendData(blogContentInfoList);
                scrollBlogItem(adapter.getItemCount() - blogContentInfoList.size());
            }

            @Override
            public void onError(Throwable e) {
                progressDialog.dismiss();
                showToast(R.string.tip_image_handle_failed);
                e.printStackTrace();
            }
        });
    }

    private void handleVideo(VideoFile videoFile) {
        String targetDir = Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator + Config.VIDEO_CACHE_DIR_NAME;
        File dirFile = new File(targetDir);
        if (!dirFile.exists()) {
            if (!dirFile.mkdirs()) {
                showToast(R.string.tip_dir_make_failed);
                return;
            }
        }
        VideoCompressUtil.compressVideo(videoFile.getPath(), Config.getOutputVideoPath(), new IVideoCompress() {
            @Override
            public void onPrePared() {
                progressDialog.show();
            }

            @Override
            public void onSuccess(String sourcePath, String newPath) {
                progressDialog.dismiss();
                BlogContentInfo info = new BlogContentInfo(BlogContentInfo.CONTENT_TYPE_VIDEO, newPath);
                try {
                    MediaMetadataRetriever retriever = new MediaMetadataRetriever();
                    retriever.setDataSource(newPath);
                    Bitmap firstFrameBitmap = retriever.getFrameAtTime();
                    retriever.release();
                    int width = firstFrameBitmap.getWidth(); // 视频宽度
                    int height = firstFrameBitmap.getHeight(); // 视频高度
                    info.setWidth(width);
                    info.setHeight(height);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                adapter.appendItem(info, false);
                scrollBlogItem(adapter.getItemCount() - 1);
            }

            @Override
            public void onFail() {
                progressDialog.dismiss();
                showToast(R.string.tip_video_handle_failed);
            }

            @Override
            public void onProgress(int percent) {

            }
        });
    }
}

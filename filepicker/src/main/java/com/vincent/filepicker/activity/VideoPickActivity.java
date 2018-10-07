package com.vincent.filepicker.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.vincent.filepicker.Constant;
import com.vincent.filepicker.DividerGridItemDecoration;
import com.vincent.filepicker.R;
import com.vincent.filepicker.adapter.FolderListAdapter;
import com.vincent.filepicker.adapter.OnSelectStateListener;
import com.vincent.filepicker.adapter.VideoPickAdapter;
import com.vincent.filepicker.filter.FileFilter;
import com.vincent.filepicker.filter.callback.FilterResultCallback;
import com.vincent.filepicker.filter.entity.Directory;
import com.vincent.filepicker.filter.entity.VideoFile;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import static com.vincent.filepicker.Config.DEFAULT_MAX_SELECT_NUMBER;
import static com.vincent.filepicker.Config.IS_TAKEN_AUTO_SELECTED;
import static com.vincent.filepicker.Config.MAX_DURATION;
import static com.vincent.filepicker.Config.MAX_SELECT_NUMBER;
import static com.vincent.filepicker.Config.MIN_DURATION;
import static com.vincent.filepicker.Config.NEED_CAMERA;
import static com.vincent.filepicker.Config.ONLY_MP4;

/**
 * Created by Vincent Woo
 * Date: 2016/10/21
 * Time: 14:02
 */

public class VideoPickActivity extends BaseActivity {
    public static final String THUMBNAIL_PATH = "FilePick";
    public static final int COLUMN_NUMBER = 3;
    private int currentNumber = 0;
    private VideoPickAdapter mAdapter;
    private boolean isNeedCamera;
    private boolean isTakenAutoSelected;
    private ArrayList<VideoFile> mSelectedList = new ArrayList<>();
    private List<Directory<VideoFile>> mAll;
    private ProgressBar mProgressBar;

    private TextView tv_count;
    private TextView tv_folder;
    private RelativeLayout tb_pick;

    // 视频最大时常：ms
    private int maxDuration;
    // 视频最小时常：ms
    private int minDuration;
    private int maxSelectNumber;
    private boolean needCamera;
    private boolean needFolderList;
    private boolean onlyMp4;
    private int requestCode;

    @Override
    void permissionGranted() {
        loadData();
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.vw_activity_video_pick);
        Intent intent = getIntent();
        maxDuration = intent.getIntExtra(MAX_DURATION, Integer.MAX_VALUE);
        minDuration = intent.getIntExtra(MIN_DURATION, 0);
        maxSelectNumber = intent.getIntExtra(MAX_SELECT_NUMBER, DEFAULT_MAX_SELECT_NUMBER);
        onlyMp4 = intent.getBooleanExtra(ONLY_MP4, false);
        isNeedCamera = intent.getBooleanExtra(NEED_CAMERA, false);
        isTakenAutoSelected = intent.getBooleanExtra(IS_TAKEN_AUTO_SELECTED, true);
        initView();
    }

    private void initView() {
        tv_count = findViewById(R.id.tv_count);
        final String currentIndexDesc = currentNumber + "/" + maxSelectNumber;
        tv_count.setText(currentIndexDesc);

        RecyclerView mRecyclerView = findViewById(R.id.rv_video_pick);
        GridLayoutManager layoutManager = new GridLayoutManager(this, COLUMN_NUMBER);
        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.addItemDecoration(new DividerGridItemDecoration(this));

        mAdapter = new VideoPickAdapter(this, isNeedCamera, maxSelectNumber);
        mRecyclerView.setAdapter(mAdapter);

        mAdapter.setOnSelectStateListener(new OnSelectStateListener<VideoFile>() {
            @Override
            public void OnSelectStateChanged(boolean state, VideoFile file) {
                if (state) {
                    mSelectedList.add(file);
                    currentNumber++;
                } else {
                    mSelectedList.remove(file);
                    currentNumber--;
                }
                tv_count.setText(currentIndexDesc);
            }
        });

        mProgressBar = findViewById(R.id.pb_video_pick);
        String cacheDirPath = getExternalCacheDir() == null ? "" : getExternalCacheDir().getAbsolutePath();
        File folder = new File(cacheDirPath + File.separator + THUMBNAIL_PATH);
        if (!folder.exists()) {
            mProgressBar.setVisibility(View.VISIBLE);
        } else {
            mProgressBar.setVisibility(View.GONE);
        }

        RelativeLayout rl_done = findViewById(R.id.rl_done);
        rl_done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.putParcelableArrayListExtra(Constant.RESULT_PICK_VIDEO, mSelectedList);
                setResult(RESULT_OK, intent);
                finish();
            }
        });

        tb_pick = findViewById(R.id.tb_pick);
        LinearLayout ll_folder = findViewById(R.id.ll_folder);
        if (isNeedFolderList) {
            ll_folder.setVisibility(View.VISIBLE);
            ll_folder.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mFolderHelper.toggle(tb_pick);
                }
            });
            tv_folder = findViewById(R.id.tv_folder);
            tv_folder.setText(getResources().getString(R.string.vw_all));

            mFolderHelper.setFolderListListener(new FolderListAdapter.FolderListListener() {
                @Override
                public void onFolderListClick(Directory directory) {
                    mFolderHelper.toggle(tb_pick);
                    tv_folder.setText(directory.getName());

                    if (TextUtils.isEmpty(directory.getPath())) { //All
                        refreshData(mAll);
                    } else {
                        for (Directory<VideoFile> dir : mAll) {
                            if (dir.getPath().equals(directory.getPath())) {
                                List<Directory<VideoFile>> list = new ArrayList<>();
                                list.add(dir);
                                refreshData(list);
                                break;
                            }
                        }
                    }
                }
            });
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case Constant.REQUEST_CODE_TAKE_VIDEO:
                if (resultCode == RESULT_OK) {
//                    Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
//                    File file = new File(data.getStringExtra("recordFilePath"));
//                    Uri contentUri = Uri.fromFile(file);
//                    mediaScanIntent.setData(contentUri);
//                    sendBroadcast(mediaScanIntent);
                    loadData();
                }
                break;
        }
    }

//    private void loadData() {
//        FileFilter.getVideos(this, new FilterResultCallback<VideoFile>() {
//            @Override
//            public void onResult(List<Directory<VideoFile>> directories) {
//                mProgressBar.setVisibility(View.GONE);
//                // Refresh folder list
//                if (isNeedFolderList) {
//                    ArrayList<Directory> list = new ArrayList<>();
//                    Directory all = new Directory();
//                    all.setName(getResources().getString(R.string.vw_all));
//                    list.add(all);
//                    list.addAll(directories);
//                    mFolderHelper.fillData(list);
//                }
//
//                mAll = directories;
//                refreshData(directories);
//            }
//        });
//    }

    private void refreshData(List<Directory<VideoFile>> directories) {
        boolean tryToFindTaken = isTakenAutoSelected;

        // if auto-select taken file is enabled, make sure requirements are met
        if (tryToFindTaken && !TextUtils.isEmpty(mAdapter.mVideoPath)) {
            File takenFile = new File(mAdapter.mVideoPath);
            tryToFindTaken = !mAdapter.isUpToMax() && takenFile.exists(); // try to select taken file only if max isn't reached and the file exists
        }

        List<VideoFile> list = new ArrayList<>();
        for (Directory<VideoFile> directory : directories) {
            list.addAll(directory.getFiles());

            // auto-select taken file?
            if (tryToFindTaken) {
                tryToFindTaken = findAndAddTaken(directory.getFiles());   // if taken file was found, we're done
            }
        }

        for (VideoFile file : mSelectedList) {
            int index = list.indexOf(file);
            if (index != -1) {
                list.get(index).setSelected(true);
            }
        }
        mAdapter.refresh(list);
    }

    private boolean findAndAddTaken(List<VideoFile> list) {
        final String currentIndexDesc = currentNumber + "/" + maxSelectNumber;
        for (VideoFile videoFile : list) {
            if (videoFile.getPath().equals(mAdapter.mVideoPath)) {
                mSelectedList.add(videoFile);
                currentNumber++;
                mAdapter.setCurrentNumber(currentNumber);
                tv_count.setText(currentIndexDesc);

                return true;   // taken file was found and added
            }
        }
        return false;    // taken file wasn't found
    }

    private void loadData() {
        FileFilter.loadVideos(this, new FilterResultCallback<VideoFile>() {
            @Override
            public void onResult(List<Directory<VideoFile>> directories) {
                mProgressBar.setVisibility(View.GONE);
                // Refresh folder list
                if (isNeedFolderList) {
                    ArrayList<Directory> list = new ArrayList<>();
                    Directory all = new Directory();
                    all.setName(getResources().getString(R.string.vw_all));
                    list.add(all);
                    list.addAll(directories);
                    mFolderHelper.fillData(list);
                }

                mAll = directories;
                refreshData(directories);
            }
        }, minDuration, maxDuration, onlyMp4);
    }
}

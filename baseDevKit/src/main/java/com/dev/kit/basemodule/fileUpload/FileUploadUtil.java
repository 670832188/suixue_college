package com.dev.kit.basemodule.fileUpload;

import android.content.Context;
import android.support.annotation.NonNull;

import com.dev.kit.basemodule.netRequest.model.BaseController;
import com.dev.kit.basemodule.netRequest.subscribers.NetRequestCallback;
import com.dev.kit.basemodule.netRequest.subscribers.NetRequestSubscriber;
import com.dev.kit.basemodule.netRequest.util.BaseServiceUtil;
import com.dev.kit.basemodule.result.BaseResult;
import com.dev.kit.basemodule.util.FileUtil;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import io.reactivex.Observable;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;

/**
 * Created by cuiyan on 2018/8/6.
 */
public class FileUploadUtil {
    private static void uploadFiles(Context context, @NonNull List<File> fileList, Map<String, String> strParams, @NonNull final UploadFileListener uploadFileListener) {
        List<MultipartBody.Part> filePartList = new ArrayList<>();
        if (strParams != null) {
            for (String key : strParams.keySet()) {
                MultipartBody.Part strParamPart = MultipartBody.Part.createFormData(key, strParams.get(key));
                filePartList.add(strParamPart);
            }
        }
        for (File file : fileList) {
            String fileType = FileUtil.getMimeType(file.getAbsolutePath());
            MediaType mediaType = MediaType.parse(fileType);
            RequestBody fileParamBody = RequestBody.create(mediaType, file);
            MultipartBody.Part filePart = MultipartBody.Part.createFormData("file", file.getName(), fileParamBody);
            filePartList.add(filePart);
        }
        NetRequestSubscriber<BaseResult<List<FileUploadResult>>> subscriber = new NetRequestSubscriber<>(new NetRequestCallback<BaseResult<List<FileUploadResult>>>() {
            @Override
            public void onSuccess(@NonNull BaseResult<List<FileUploadResult>> result) {
                uploadFileListener.onSuccess(result);
            }

            @Override
            public void onResultNull() {
                uploadFileListener.onResultNull();
            }

            @Override
            public void onError(Throwable throwable) {
                uploadFileListener.onError(throwable);
            }
        }, context, true, null);
        Observable observable = BaseServiceUtil.createService(FileUploadApiService.class).uploadFile(filePartList);
        BaseController.sendRequest(subscriber, observable);
    }

    public interface UploadFileListener {
        void onSuccess(BaseResult<List<FileUploadResult>> result);

        void onError(Throwable throwable);

        void onResultNull();
    }
}

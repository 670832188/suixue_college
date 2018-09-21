package com.vincent.filepicker.filter.entity;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Vincent Woo
 * Date: 2016/10/11
 * Time: 15:23
 */

public class VideoFile extends BaseFile implements Parcelable {
    private long duration;
    private String thumbnail;
    private int width;
    private int height;

    public long getDuration() {
        return duration;
    }

    public void setDuration(long duration) {
        this.duration = duration;
    }

    public String getThumbnail() {
        return thumbnail;
    }

    public void setThumbnail(String thumbnail) {
        this.thumbnail = thumbnail;
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(getId());
        dest.writeString(getName());
        dest.writeString(getPath());
        dest.writeLong(getSize());
        dest.writeString(getBucketId());
        dest.writeString(getBucketName());
        dest.writeLong(getDate());
        dest.writeByte((byte) (isSelected() ? 1 : 0));
        dest.writeLong(getDuration());
        dest.writeString(getThumbnail());
        dest.writeInt(getWidth());
        dest.writeInt(getHeight());
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<VideoFile> CREATOR = new Creator<VideoFile>() {
        @Override
        public VideoFile[] newArray(int size) {
            return new VideoFile[size];
        }

        @Override
        public VideoFile createFromParcel(Parcel in) {
            VideoFile file = new VideoFile();
            file.setId(in.readLong());
            file.setName(in.readString());
            file.setPath(in.readString());
            file.setSize(in.readLong());
            file.setBucketId(in.readString());
            file.setBucketName(in.readString());
            file.setDate(in.readLong());
            file.setSelected(in.readByte() != 0);
            file.setDuration(in.readLong());
            file.setThumbnail(in.readString());
            file.setWidth(in.readInt());
            file.setHeight(in.readInt());
            return file;
        }
    };
}

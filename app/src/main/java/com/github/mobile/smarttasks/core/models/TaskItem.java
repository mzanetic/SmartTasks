package com.github.mobile.smarttasks.core.models;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.j256.ormlite.field.DatabaseField;

import java.util.Date;

public class TaskItem implements Parcelable {

    public static final String TARGET_DATE = "TargetDate";
    public static final String DUE_DATE    = "DueDate";

    @DatabaseField(id = true)
    @Expose
    private String id;

    @DatabaseField
    @Expose
    private String title;

    @DatabaseField
    @SerializedName(TARGET_DATE)
    @Expose
    private Date targetDate;

    @DatabaseField
    @SerializedName(DUE_DATE)
    @Expose
    private Date dueDate;

    @DatabaseField
    @SerializedName("Description")
    @Expose
    private String description;

    @DatabaseField
    @SerializedName("Priority")
    @Expose
    private Integer priority;

    @DatabaseField
    private Status status;

    @DatabaseField
    private String comment;

    @DatabaseField
    private String pageKey;

    public TaskItem() {
        status = Status.UNRESOLVED;
        comment = "";
    }

    public TaskItem(String id, String title, Date targetDate, Date dueDate, String description, Integer priority, Status status, String comment) {
        this.id = id;
        this.title = title;
        this.targetDate = targetDate;
        this.dueDate = dueDate;
        this.description = description;
        this.priority = priority;
        this.status = status;
        this.comment = comment;
    }

    public static final Creator<TaskItem> CREATOR = new Creator<TaskItem>() {
        @Override
        public TaskItem createFromParcel(Parcel in) {
            return new TaskItem(in);
        }

        @Override
        public TaskItem[] newArray(int size) {
            return new TaskItem[size];
        }
    };

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Date getTargetDate() {
        return targetDate;
    }

    public void setTargetDate(Date targetDate) {
        this.targetDate = targetDate;
    }

    public Date getDueDate() {
        return dueDate;
    }

    public void setDueDate(Date dueDate) {
        this.dueDate = dueDate;
    }

    public Integer getPriority() {
        return priority;
    }

    public void setPriority(Integer priority) {
        this.priority = priority;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getPageKey() {
        return pageKey;
    }

    public void setPageKey(String pageKey) {
        this.pageKey = pageKey;
    }

    @Override
    public String toString() {
        return "TaskItem{" +
                "id='" + id + '\'' +
                ", title='" + title + '\'' +
                ", targetDate=" + targetDate +
                ", dueDate=" + dueDate +
                ", description='" + description + '\'' +
                ", priority=" + priority +
                ", status=" + status +
                ", comment='" + comment + '\'' +
                '}';
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeString(title);
        dest.writeSerializable(targetDate);
        dest.writeSerializable(dueDate);
        dest.writeString(description);
        dest.writeInt(priority);
        dest.writeSerializable(status);
        dest.writeString(comment);
        dest.writeString(pageKey);
    }

    public TaskItem(Parcel in) {
        id = in.readString();
        title = in.readString();
        targetDate = (Date) in.readSerializable();
        dueDate = (Date) in.readSerializable();
        description = in.readString();
        priority = in.readInt();
        status = (Status) in.readSerializable();
        comment = in.readString();
        pageKey = in.readString();
    }
}

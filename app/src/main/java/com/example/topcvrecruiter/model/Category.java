package com.example.topcvrecruiter.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.List;

public class Category implements Parcelable {
    private String category_name;
    private List<Jobs> jobs_list;

    // Constructor
    public Category(String category_name, List<Jobs> jobs_list) {
        this.category_name = category_name;
        this.jobs_list = jobs_list;
    }

    // Constructor nhận đối tượng Parcel
    protected Category(Parcel in) {
        category_name = in.readString();
        jobs_list = new ArrayList<>();
        in.readList(jobs_list, Jobs.class.getClassLoader());
    }

    // Ghi dữ liệu vào Parcel
    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(category_name);
        dest.writeList(jobs_list);
    }

    // Phương thức mô tả nội dung
    @Override
    public int describeContents() {
        return 0;
    }

    // Đối tượng CREATOR để tạo đối tượng từ Parcel
    public static final Creator<Category> CREATOR = new Creator<Category>() {
        @Override
        public Category createFromParcel(Parcel in) {
            return new Category(in);
        }

        @Override
        public Category[] newArray(int size) {
            return new Category[size];
        }
    };

    // Getter và Setter
    public String getCategory_name() {
        return category_name;
    }

    public void setCategory_name(String category_name) {
        this.category_name = category_name;
    }

    public List<Jobs> getJobs_list() {
        return jobs_list;
    }

    public void setJobs_list(List<Jobs> jobs_list) {
        this.jobs_list = jobs_list;
    }
}

package com.lxqhmlwyh.qingtingfm.databaseentities;

import com.orm.SugarRecord;

/**
 * 李兴权
 * 电台类型倾向
 */
public class CategoriesRecordTable extends SugarRecord {
    private int categoryId;
    private  String categoryTitle;
    private int count;
    private long timeStamp;

    public int getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(int categoryId) {
        this.categoryId = categoryId;
    }

    public String getCategoryTitle() {
        return categoryTitle;
    }

    public void setCategoryTitle(String categoryTitle) {
        this.categoryTitle = categoryTitle;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public long getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(long timeStamp) {
        this.timeStamp = timeStamp;
    }

    @Override
    public String toString() {
        return "CategoriesRecordTable{" +
                "categoryId=" + categoryId +
                ", categoryTitle='" + categoryTitle + '\'' +
                ", count=" + count +
                ", timeStamp=" + timeStamp +
                '}';
    }
}

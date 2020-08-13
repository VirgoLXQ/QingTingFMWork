package com.lxqhmlwyh.qingtingfm.databaseentities;


import com.orm.SugarRecord;


/**
 * 李兴权
 * 最喜欢的电台
 * 2020年8月13日
 */
public class FavouriteTable extends SugarRecord {

    private String title;
    private String imgUrl;
    private int channel_id;

    public void setTitle(String title) {
        this.title = title;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    public void setChannel_id(int channel_id) {
        this.channel_id = channel_id;
    }

    @Override
    public String toString() {
        return "FavouriteTable{" +
                "title='" + title + '\'' +
                ", imgUrl='" + imgUrl + '\'' +
                ", channel_id=" + channel_id +
                '}';
    }

    @Override
    public long save() {
        return super.save();
    }
}

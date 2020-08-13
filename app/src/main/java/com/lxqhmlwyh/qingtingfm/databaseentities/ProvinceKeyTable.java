package com.lxqhmlwyh.qingtingfm.databaseentities;

import com.orm.SugarRecord;

/**
 * 李兴权
 * 2020年8月12日
 * 记录访问某省份的访问次数
 */
public class ProvinceKeyTable extends SugarRecord {
    //省份
    private String province;
    //时间戳
    private long stamp;
    //次数
    private int count;

    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province;
    }

    public long getStamp() {
        return stamp;
    }

    public void setStamp(long stamp) {
        this.stamp = stamp;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    @Override
    public long save() {
        return super.save();
    }

    @Override
    public String toString() {
        return "ProvinceKeyTable{" +
                "province='" + province + '\'' +
                ", stamp=" + stamp +
                ", count=" + count +
                '}';
    }
}

package com.lxqhmlwyh.qingtingfm.databaseentities;


import com.orm.SugarRecord;

public class APPVisitTable extends SugarRecord {
    //private int id;
    private long timeStamp;
    private int count;

    /*public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }*/

    public long getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(long timeStamp) {
        this.timeStamp = timeStamp;
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
    public long update() {
        return super.update();
    }



    @Override
    public String toString() {
        return "APPVisitTable{" +
                "timeStamp=" + timeStamp +
                ", count=" + count +
                '}';
    }
}

package com.lxqhmlwyh.qingtingfm.databaseentities;

import com.orm.SugarRecord;

/**
 * 李兴权
 * 2020年8月11日
 * 最受欢迎的节目
 */
public class PreferProgramTable extends SugarRecord {

    private int programId;
    private String programName;
    private int count;
    private long timeStamp;

    public void setProgramId(int programId) {
        this.programId = programId;
    }

    public int getProgramId() {
        return programId;
    }

    public String getProgramName() {
        return programName;
    }

    public void setProgramName(String programName) {
        this.programName = programName;
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
        return "PreferProgramTable{" +
                "programId=" + programId +
                ", programName='" + programName + '\'' +
                ", count=" + count +
                ", timeStamp=" + timeStamp +
                '}';
    }

    @Override
    public long save() {
        return super.save();
    }
}

package com.lxqhmlwyh.qingtingfm.databaseutil;

import com.orm.SugarRecord;

public class PreferProgram extends SugarRecord {

    private int channelId;
    private String programName;
    private int count;
    private long timeStamp;

    public int getChannelId() {
        return channelId;
    }

    public void setChannelId(int channelId) {
        this.channelId = channelId;
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
    public long save() {
        return super.save();
    }
}

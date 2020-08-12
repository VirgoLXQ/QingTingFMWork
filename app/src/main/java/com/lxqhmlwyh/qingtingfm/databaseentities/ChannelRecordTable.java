package com.lxqhmlwyh.qingtingfm.databaseentities;

import com.orm.SugarRecord;

/**
 * 李兴权
 * 2020年8月11日
 * 记录最喜欢的电台
 */
public class ChannelRecordTable extends SugarRecord {
    private String channel;
    private int channelId;
    private int count;
    private long timeStamp;

    public String getChannel() {
        return channel;
    }

    public void setChannel(String channel) {
        this.channel = channel;
    }

    public int getChannelId() {
        return channelId;
    }

    public void setChannelId(int channelId) {
        this.channelId = channelId;
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

    @Override
    public String toString() {
        return "ChannelRecordTable{" +
                "channel='" + channel + '\'' +
                ", channelId='" + channelId + '\'' +
                ", count=" + count +
                ", timeStamp=" + timeStamp +
                '}';
    }
}

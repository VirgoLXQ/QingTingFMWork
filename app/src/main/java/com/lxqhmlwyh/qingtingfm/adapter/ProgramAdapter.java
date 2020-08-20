package com.lxqhmlwyh.qingtingfm.adapter;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.lxqhmlwyh.qingtingfm.R;
import com.lxqhmlwyh.qingtingfm.activity.PlayActivity;
import com.lxqhmlwyh.qingtingfm.activity.PlayListActivity;
import com.lxqhmlwyh.qingtingfm.databaseentities.PreferProgramTable;
import com.lxqhmlwyh.qingtingfm.entities.Broadcasters;
import com.lxqhmlwyh.qingtingfm.entities.PlayingList;
import com.lxqhmlwyh.qingtingfm.entities.ProgramItemEntity;
import com.lxqhmlwyh.qingtingfm.service.PlayService;
import com.lxqhmlwyh.qingtingfm.utils.DataBaseUtil;
import com.lxqhmlwyh.qingtingfm.utils.MyPlayer;
import com.lxqhmlwyh.qingtingfm.utils.MyTime;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

public class ProgramAdapter extends RecyclerView.Adapter<ProgramAdapter.ProgramItem> {

    private List<ProgramItemEntity> programs;
    private Context context;
    private int livingItem;

    public ProgramAdapter(Context context,List<ProgramItemEntity> programs){
        this.programs=programs;
        this.context=context;
    }

    @NonNull
    @Override
    public ProgramItem onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.program_item,null);
        ProgramItem item=new ProgramItem(view);
        return item;
    }

    @Override
    public void onBindViewHolder(@NonNull ProgramItem holder, final int position) {
        final ProgramItemEntity entity=programs.get(position);
        //holder.countView.setText("2345");
        List<Broadcasters>broadcasters=entity.getBroadcasters();
        String host="";
        //遍历json数据里broadcasters数组的对象
        for (Broadcasters hostObj:broadcasters){
            //将这个节目里所有主播的名字连接起来
            host=host+"  "+hostObj.getUsername();
        }

        holder.hostView.setText(host);
        holder.stateImg.setImageResource(R.mipmap.sound_wait);
        holder.titleView.setText(entity.getTitle());
        holder.durationView.setText("["+entity.getStart_time()+"-"+entity.getEnd_time()+"]");

        //寻找正在播放的节目
        int temp=((PlayListActivity)context).programId;
        String startTime=((PlayListActivity)context).startTime;
        //判断这个节目的id和开始时间与正在播放的节目的相关信息是否一致
        if (entity.getProgram_id()==temp&&entity.getStart_time().equals(startTime)){
            //已找到正在播放的节目
            Log.e("ProgramAdapter","context.programId="+temp+"; entity="+entity.getProgram_id());
            holder.countView.setText(((PlayListActivity)context).count+"");
            holder.countImg.setVisibility(View.VISIBLE);
            //显示听众人数
            holder.countView.setVisibility(View.VISIBLE);
            //给列表项添加醒目的样式
            holder.relativeLayout.setBackground(context.getDrawable(R.drawable.program_item_current_living));
            livingItem=position;
        }else{//因为出现了奇怪的问题，所以添加了else分支
            holder.countImg.setVisibility(View.GONE);
            holder.countView.setVisibility(View.GONE);
            holder.relativeLayout.setBackground(context.getDrawable(R.drawable.program_item_normal_style));
        }

        final String finalHost = host;
        //给列表项添加点击事件
        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //现判断已选择的节目是否正在直播，如果节目正在播放或者已经结束，那么就可以进行下一步
                if (position>livingItem){
                    Toast.makeText(context, "节目还没开始", Toast.LENGTH_SHORT).show();
                    return;
                }

                Intent intent=new Intent(context, PlayActivity.class);
                intent.putExtra("channelName",((PlayListActivity)context).channelName);
                intent.putExtra("cover",((PlayListActivity)context).cover);
                intent.putExtra("title",entity.getTitle());
                intent.putExtra("broadcaster", finalHost);
                intent.putExtra("start_time",entity.getStart_time());
                intent.putExtra("end_time",entity.getEnd_time());
                intent.putExtra("duration",entity.getDuration());

                updateProgramTable(entity.getProgram_id(),entity.getTitle());

                //定义播放列表
                List<PlayingList> playingList=new ArrayList<>();
                for(ProgramItemEntity itemEntity:programs){
                    PlayingList playingItem=new PlayingList();
                    String host="";
                    for (Broadcasters hostObj:itemEntity.getBroadcasters()){
                        host=host+"  "+hostObj.getUsername();
                    }
                    playingItem.setBroadcasters(host);
                    //playingItem.setChannelId(((PlayListActivity)context).channelId);
                    playingItem.setStartTime(itemEntity.getStart_time());
                    playingItem.setPlayUrl(itemEntity.getStart_time());
                    playingItem.setEndTime(itemEntity.getEnd_time());
                    String playUrl=MyTime.changeToPlayUrl(((PlayListActivity)context).channelId,
                            itemEntity.getStart_time(),itemEntity.getEnd_time());
                    playingItem.setPlayUrl(playUrl);
                    playingItem.setProgramName(itemEntity.getTitle());
                    playingList.add(playingItem);
                }

                clickPlayer(position,playingList);

                //播放节目前的配置
                //MyPlayer.PLAY_LIST=playingList;
                //MyPlayer.currentIndex=position;
                intent.putExtra("need",true);
                //intent.putExtra("isReset",true);
                /*PlayService.setPlayingList(playingList);
                Intent serIntent=new Intent(context, PlayService.class);
                serIntent.putExtra("startIndex",position);*/
                //启动播放服务
                //context.startService(serIntent);
                //跳转到播放界面
                context.startActivity(intent);
            }
        });
    }

    public void clickPlayer(int position,List<PlayingList> list){
        MyPlayer myPlayer=MyPlayer.getMyPlayer();
        if (myPlayer==null){
            myPlayer=MyPlayer.getInstance();
        }else{
            myPlayer.resetPlayer();
        }
        myPlayer.setMusicList(list);
        myPlayer.setListIndex(position);
    }

    /**
     * 更新数据，当点击列表项后，调用这个方法
     */
    private void updateProgramTable(int programId,String programName){
        long nowTimeStamp=System.currentTimeMillis();
        Calendar nowCalendar=Calendar.getInstance();
        nowCalendar.setTime(new Date(nowTimeStamp));
        Iterator records=PreferProgramTable.findAll(PreferProgramTable.class);
        while(records.hasNext()){
            PreferProgramTable thisRecord=(PreferProgramTable) records.next();
            //Log.e("updateProgramTable",thisRecord.toString());
            long thisTimeStamp=thisRecord.getTimeStamp();
            Calendar thisCalendar=Calendar.getInstance();
            thisCalendar.setTime(new Date(thisTimeStamp));
            //先判断这一行记录是否是今天产生的
            if (DataBaseUtil.isToday(nowCalendar,thisCalendar)){
                int thisProgramId=thisRecord.getProgramId();
                String thisProgramName=thisRecord.getProgramName();
                //再判断这个节目的id和节目名是否和点击的节目一致
               if (programId==thisProgramId&&programName.equals(thisProgramName)){
                   //找到目标记录后，根据这条记录的id更新数据
                   long thisId=thisRecord.getId();
                   int updateCount=thisRecord.getCount();
                   PreferProgramTable.executeQuery("update Prefer_Program_Table set count=? where id=?",
                           updateCount+1+"",thisId+"");
                   //Log.e("updateProgramTable","更新了今天这个节目的点击次数");
                    return;
                }
            }
        }

        //如果找不到当天的记录，那么就新增一条
        PreferProgramTable newRecord=new PreferProgramTable();
        newRecord.setProgramId(programId);
        newRecord.setCount(1);
        newRecord.setProgramName(programName);
        newRecord.setTimeStamp(nowTimeStamp);
        newRecord.save();
        Log.e("updateProgramTable","新增一条记录--"+newRecord.toString());
    }

    @Override
    public int getItemCount() {
        return programs.size();
    }

    class ProgramItem extends RecyclerView.ViewHolder{

        ImageView stateImg;//节目标题图标
        TextView titleView;
        TextView hostView;//电台主播的名字
        TextView countView;
        TextView durationView;
        CardView cardView;
        ImageView countImg;
        RelativeLayout relativeLayout;

        public ProgramItem(@NonNull View itemView) {
            super(itemView);
            stateImg= itemView.findViewById(R.id.program_state_wait);
            titleView=itemView.findViewById(R.id.program_title);
            hostView=itemView.findViewById(R.id.program_host);
            countView=itemView.findViewById(R.id.program_count);
            durationView=itemView.findViewById(R.id.program_duration);
            cardView=itemView.findViewById(R.id.play_list_item);
            countImg=itemView.findViewById(R.id.program_count_img);
            relativeLayout=itemView.findViewById(R.id.program_inner_relative);
        }
    }
}

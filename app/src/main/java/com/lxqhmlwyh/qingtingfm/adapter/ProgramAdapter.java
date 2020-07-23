package com.lxqhmlwyh.qingtingfm.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.lxqhmlwyh.qingtingfm.R;
import com.lxqhmlwyh.qingtingfm.activity.PlayActivity;
import com.lxqhmlwyh.qingtingfm.activity.PlayListActivity;
import com.lxqhmlwyh.qingtingfm.pojo.Broadcasters;
import com.lxqhmlwyh.qingtingfm.pojo.PlayingList;
import com.lxqhmlwyh.qingtingfm.pojo.ProgramItemEntity;
import com.lxqhmlwyh.qingtingfm.service.PlayService;
import com.lxqhmlwyh.qingtingfm.utils.MyPlayer;
import com.lxqhmlwyh.qingtingfm.utils.MyTime;

import java.util.ArrayList;
import java.util.List;

public class ProgramAdapter extends RecyclerView.Adapter<ProgramAdapter.ProgramItem> {

    private List<ProgramItemEntity> programs;
    private Context context;

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
        holder.countView.setText("2345");
        List<Broadcasters>broadcasters=entity.getBroadcasters();
        String host="";
        for (Broadcasters hostObj:broadcasters){
            host=host+"  "+hostObj.getUsername();
        }
        holder.hostView.setText(host);
        holder.stateImg.setImageResource(R.mipmap.sound_wait);
        holder.titleView.setText(entity.getTitle());
        holder.durationView.setText("["+entity.getStart_time()+"-"+entity.getEnd_time()+"]");

        final String finalHost = host;
        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(context, PlayActivity.class);
                intent.putExtra("channelName",((PlayListActivity)context).channelName);
                intent.putExtra("cover",((PlayListActivity)context).cover);
                intent.putExtra("title",entity.getTitle());
                intent.putExtra("broadcaster", finalHost);
                intent.putExtra("start_time",entity.getStart_time());
                intent.putExtra("end_time",entity.getEnd_time());
                intent.putExtra("duration",entity.getDuration());

                List<PlayingList> playingList=new ArrayList<>();
                for(ProgramItemEntity itemEntity:programs){
                    PlayingList playingItem=new PlayingList();
                    String host="";
                    for (Broadcasters hostObj:itemEntity.getBroadcasters()){
                        host=host+"  "+hostObj.getUsername();
                    }
                    playingItem.setBroadcasters(host);
                    playingItem.setChannelId(((PlayListActivity)context).channelId);
                    playingItem.setPlayUrl(itemEntity.getStart_time());
                    playingItem.setEndTime(itemEntity.getEnd_time());
                    String playUrl=MyTime.changeToPlayUrl(((PlayListActivity)context).channelId,
                            itemEntity.getStart_time(),itemEntity.getEnd_time());
                    playingItem.setPlayUrl(playUrl);
                    playingItem.setProgramName(itemEntity.getTitle());
                    playingList.add(playingItem);
                }
                PlayService.setPlayingList(playingList);
                context.startService(new Intent(context, PlayService.class));
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return programs.size();
    }

    class ProgramItem extends RecyclerView.ViewHolder{

        ImageView stateImg;
        TextView titleView;
        TextView hostView;
        TextView countView;
        TextView durationView;
        CardView cardView;

        public ProgramItem(@NonNull View itemView) {
            super(itemView);
            stateImg= itemView.findViewById(R.id.program_state_wait);
            titleView=itemView.findViewById(R.id.program_title);
            hostView=itemView.findViewById(R.id.program_host);
            countView=itemView.findViewById(R.id.program_count);
            durationView=itemView.findViewById(R.id.program_duration);
            cardView=itemView.findViewById(R.id.play_list_item);
        }
    }
}

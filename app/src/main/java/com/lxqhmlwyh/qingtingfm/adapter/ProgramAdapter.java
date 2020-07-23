package com.lxqhmlwyh.qingtingfm.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.lxqhmlwyh.qingtingfm.R;
import com.lxqhmlwyh.qingtingfm.pojo.Broadcasters;
import com.lxqhmlwyh.qingtingfm.pojo.ProgramItemEntity;

import java.util.List;

public class ProgramAdapter extends RecyclerView.Adapter<ProgramAdapter.ProgramItem> {

    private List<ProgramItemEntity> programs;

    public ProgramAdapter(List<ProgramItemEntity> programs){
        this.programs=programs;
    }

    @NonNull
    @Override
    public ProgramItem onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.program_item,null);
        ProgramItem item=new ProgramItem(view);
        return item;
    }

    @Override
    public void onBindViewHolder(@NonNull ProgramItem holder, int position) {
        ProgramItemEntity entity=programs.get(position);
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
        public ProgramItem(@NonNull View itemView) {
            super(itemView);
            stateImg= itemView.findViewById(R.id.program_state_wait);
            titleView=itemView.findViewById(R.id.program_title);
            hostView=itemView.findViewById(R.id.program_host);
            countView=itemView.findViewById(R.id.program_count);
            durationView=itemView.findViewById(R.id.program_duration);
        }
    }
}

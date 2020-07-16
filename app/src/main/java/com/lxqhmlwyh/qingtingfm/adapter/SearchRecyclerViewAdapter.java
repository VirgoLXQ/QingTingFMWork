package com.lxqhmlwyh.qingtingfm.adapter;

import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.lxqhmlwyh.qingtingfm.R;
import com.lxqhmlwyh.qingtingfm.pojo.FMCardView;

import java.util.List;

public class SearchRecyclerViewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<FMCardView> data;

    public SearchRecyclerViewAdapter(List<FMCardView> data){
        this.data=data;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType==0){
            View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.cardview_item,null);
            CardViewHolder holder=new CardViewHolder(view);
            return holder;
        }else{
            View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.item_loading_more,null);
            FooterHolder holder=new FooterHolder(view);
            return holder;
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof CardViewHolder){
            FMCardView fmCardView=data.get(position);
            ((CardViewHolder) holder).audienceTextView.setText(fmCardView.getAudience());
            ((CardViewHolder) holder).titleTextView.setText(fmCardView.getTitle());
            ((CardViewHolder) holder).coverImg.setImageURI(Uri.parse(fmCardView.getCover()));
            ((CardViewHolder) holder).favorImg.setImageResource(R.mipmap.wujiaoxing);
        }
    }

    public void upDate(List<FMCardView> data){
        this.data=data;
        notifyDataSetChanged();
    }

    @Override
    public int getItemViewType(int position) {
        //return super.getItemViewType(position);
        if (position==data.size()){
            return 1;
        }else{
            return 0;
        }
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public class CardViewHolder extends RecyclerView.ViewHolder{

        ImageView coverImg;
        TextView titleTextView;
        ImageView favorImg;
        TextView audienceTextView;

        public CardViewHolder(@NonNull View itemView) {
            super(itemView);
            coverImg=itemView.findViewById(R.id.card_view_item_cover);
            titleTextView=itemView.findViewById(R.id.card_view_item_title);
            favorImg=itemView.findViewById(R.id.card_view_item_favorite);
            audienceTextView=itemView.findViewById(R.id.card_view_item_audience);
        }
    }

    public class FooterHolder extends RecyclerView.ViewHolder{

        ProgressBar progressBar;
        TextView loadingText;
        public FooterHolder(@NonNull View itemView) {
            super(itemView);
            progressBar=itemView.findViewById(R.id.item_loading_bar);
            loadingText=itemView.findViewById(R.id.item_loading_tv);
        }
    }
}

package com.lxqhmlwyh.qingtingfm.adapter;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.lxqhmlwyh.qingtingfm.R;
import com.lxqhmlwyh.qingtingfm.activity.PlayActivity;
import com.lxqhmlwyh.qingtingfm.pojo.FMCardView;
import com.lxqhmlwyh.qingtingfm.service.GetFMItemJsonService;
import com.lxqhmlwyh.qingtingfm.service.PlayService;

import org.json.JSONArray;

import java.util.List;

public class SearchRecyclerViewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<FMCardView> data;
    private Context context;

    public SearchRecyclerViewAdapter(Context context,List<FMCardView> data){
        this.data=data;
        this.context=context;
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

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onBindViewHolder(@NonNull final RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof CardViewHolder){
            final FMCardView fmCardView=data.get(position);
            ((CardViewHolder) holder).audienceTextView.setText(fmCardView.getAudience_count()+"");
            ((CardViewHolder) holder).titleTextView.setText(fmCardView.getTitle());
            Glide.with(context).load(fmCardView.getCover())
                    .into(((CardViewHolder) holder).coverImg);
            ((CardViewHolder) holder).favorImg.setImageResource(R.mipmap.heart_grey);
            ((CardViewHolder) holder).cardView.setTooltipText(fmCardView.getDescription());
            ((CardViewHolder) holder).cardView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    //Toast.makeText(context, fmCardView.getDescription(), Toast.LENGTH_SHORT).show();
                    Intent toPlay=new Intent(context,PlayActivity.class);
                    toPlay.putExtra("channelName",fmCardView.getTitle());
                    toPlay.putExtra("cover",fmCardView.getCover());
                    context.startActivity(toPlay);
                    context.startService(new Intent(context, PlayService.class));
                }
            });
            ((CardViewHolder) holder).favorImg.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Toast.makeText(context, "你收藏了这个电台", Toast.LENGTH_SHORT).show();
                    ((CardViewHolder) holder).favorImg.setImageResource(R.mipmap.heart_red);
                }
            });
        }
    }

    public void upData(List<FMCardView> data){
        this.data=data;
        notifyDataSetChanged();
    }

    public void upData(){
        JSONArray array=GetFMItemJsonService.getLastGetJson();
        Gson gson=new Gson();
        List<FMCardView> list=
                gson.fromJson(array.toString(), new TypeToken<List<FMCardView>>(){}.getType());
        this.data= list;
        notifyDataSetChanged();
    }

    @Override
    public int getItemViewType(int position) {
        if (position==data.size()){
            Log.e("getItemViewType()","true and position is "+position);
            return 1;
        }else{
            Log.e("getItemViewType()","false and position is "+position);
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
        CardView cardView;

        public CardViewHolder(@NonNull View itemView) {
            super(itemView);
            coverImg=itemView.findViewById(R.id.card_view_item_cover);
            titleTextView=itemView.findViewById(R.id.card_view_item_title);
            favorImg=itemView.findViewById(R.id.card_view_item_favorite);
            audienceTextView=itemView.findViewById(R.id.card_view_item_audience);
            cardView=itemView.findViewById(R.id.card_view);
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

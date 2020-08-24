package com.lxqhmlwyh.qingtingfm.adapter;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.lxqhmlwyh.qingtingfm.R;
import com.lxqhmlwyh.qingtingfm.databaseentities.FavouriteTable;
import com.orm.SugarRecord;

import java.util.List;

public class FavouriteAdapter extends RecyclerView.Adapter<FavouriteAdapter.FavouriteItem> {

    private Context context;
    private List<FavouriteTable> tables;//数据
    public FavouriteAdapter(Context context,List<FavouriteTable> tables){
        this.tables=tables;
        this.context=context;
    }

    //创建列表
    @NonNull
    @Override
    public FavouriteItem onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view=View.inflate(parent.getContext(),R.layout.item_favourite,null);
        return new FavouriteItem(view);
    }

    //列表显示内容
    @Override
    public void onBindViewHolder(@NonNull FavouriteItem holder, int position) {
        FavouriteTable table=tables.get(position);
        holder.name.setText(table.getTitle());
        holder.heart.setOnClickListener((view)->{
            holder.heart.setImageResource(R.mipmap.heart_grey);
            Toast.makeText(context, "你取消了这个收藏", Toast.LENGTH_SHORT).show();
            int channelId= table.getChannel_id();
            String title= table.getTitle();
            SugarRecord.executeQuery("delete from FAVOURITE_TABLE where channelId=? " +
                    "and title=?",channelId+"",title);
            Log.e("updateFavourite","删除了一条数据——"+title);
        });
        Glide.with(context).load(table.getImgUrl()).error(R.mipmap.default_album)
                .into(holder.img);
    }

    @Override
    public int getItemCount() {
        return tables.size();
    }

    //列表格子对象
    class FavouriteItem extends RecyclerView.ViewHolder{

        ImageView img;
        TextView name;
        ImageView heart;
        public FavouriteItem(@NonNull View itemView) {
            super(itemView);
            img=itemView.findViewById(R.id.favourite_view_item_cover);
            name=itemView.findViewById(R.id.favourite_title);
            heart=itemView.findViewById(R.id.favourite_heart);
        }
    }
}

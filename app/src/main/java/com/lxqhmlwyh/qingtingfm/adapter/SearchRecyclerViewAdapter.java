package com.lxqhmlwyh.qingtingfm.adapter;

import android.content.Context;
import android.content.Intent;
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
import com.lxqhmlwyh.qingtingfm.activity.PlayListActivity;
import com.lxqhmlwyh.qingtingfm.databaseentities.CategoriesRecordTable;
import com.lxqhmlwyh.qingtingfm.databaseentities.ChannelRecordTable;
import com.lxqhmlwyh.qingtingfm.entities.Category;
import com.lxqhmlwyh.qingtingfm.entities.FMCardViewJson;
import com.lxqhmlwyh.qingtingfm.service.GetFMItemJsonService;
import com.lxqhmlwyh.qingtingfm.utils.DataBaseUtil;
import com.orm.SugarRecord;

import org.json.JSONArray;

import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

public class SearchRecyclerViewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<FMCardViewJson> data;
    private Context context;

    public SearchRecyclerViewAdapter(Context context,List<FMCardViewJson> data){
        this.data=data;
        this.context=context;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType==0){
            View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.cardview_item,null);
            return new CardViewHolder(view);
        }else{
            View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.item_loading_more,null);
            return new FooterHolder(view);
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onBindViewHolder(@NonNull final RecyclerView.ViewHolder holder, final int position) {
        if (holder instanceof CardViewHolder){
            final FMCardViewJson fmCardView=data.get(position);
            ((CardViewHolder) holder).audienceTextView.setText(fmCardView.getAudience_count()+"");
            ((CardViewHolder) holder).titleTextView.setText(fmCardView.getTitle());
            Glide.with(context).load(fmCardView.getCover())
                    .into(((CardViewHolder) holder).coverImg);
            ((CardViewHolder) holder).favorImg.setImageResource(R.mipmap.heart_grey);
            //((CardViewHolder) holder).cardView.setTooltipText(fmCardView.getDescription());

            //CardView的点击事件
            ((CardViewHolder) holder).cardView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    //Toast.makeText(context, fmCardView.getDescription(), Toast.LENGTH_SHORT).show();

                    //更新数据
                    updateChannelRecord(fmCardView.getTitle(),fmCardView.getContent_id());
                    handleCategories(fmCardView.getCategories());

                    Intent toPlayList=new Intent(context, PlayListActivity.class);
                    toPlayList.putExtra("cover",fmCardView.getCover());
                    toPlayList.putExtra("channelName",fmCardView.getTitle());
                    toPlayList.putExtra("startTime",fmCardView.getNowplaying().getStart_time());
                    toPlayList.putExtra("programId",fmCardView.getNowplaying().getId());
                    toPlayList.putExtra("previous",fmCardView.getRegion().getTitle());
                    toPlayList.putExtra("channel",fmCardView.getTitle());
                    toPlayList.putExtra("channel_id",fmCardView.getContent_id());
                    toPlayList.putExtra("count",fmCardView.getAudience_count());
                    //跳转到节目列表界面
                    context.startActivity(toPlayList);
                    //context.startService(new Intent(context, PlayService.class));
                }
            });

            //收藏按钮的点击事件
            ((CardViewHolder) holder).favorImg.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Toast.makeText(context, "你收藏了这个电台", Toast.LENGTH_SHORT).show();
                    ((CardViewHolder) holder).favorImg.setImageResource(R.mipmap.heart_red);
                }
            });
        }
    }

    /*public void upData(List<FMCardView> data){
        this.data=data;
        notifyDataSetChanged();
    }*/

    /**
     * 遍历Categories
     * @param categories
     */
    private void handleCategories(List<Category> categories){
        for (Category category:categories){
            updateCategoriesRecord(category.getId(),category.getTitle());
        }
    }

    /**
     *更新电台类型倾向数据
     */
    private void updateCategoriesRecord(int categoryId,String categoryTitle){
        Iterator tables= CategoriesRecordTable.findAll(CategoriesRecordTable.class);
        long nowTimeStamp=System.currentTimeMillis();//获取当前的时间戳
        Calendar calendar=Calendar.getInstance();
        calendar.setTime(new Date(nowTimeStamp));
        while(tables.hasNext()){
            CategoriesRecordTable tableObj= (CategoriesRecordTable) tables.next();
            long thisTimeStamp=tableObj.getTimeStamp();//获取数据库的时间戳
            Calendar thisCalendar=Calendar.getInstance();
            thisCalendar.setTime(new Date(thisTimeStamp));
            if (DataBaseUtil.isToday(calendar,thisCalendar)){//如果是同一天
                if (categoryId==tableObj.getCategoryId()&&categoryTitle.equals(tableObj.getCategoryTitle())){
                    Log.e("updateCategoriesRecord","更新今天访问"+categoryTitle+"次数");
                    int count=tableObj.getCount()+1;
                    long id=tableObj.getId();
                    SugarRecord.executeQuery("update CATEGORIES_RECORD_TABLE set count=? where id=?",
                            count+"",id+"");
                    return;
                }
            }
        }

        CategoriesRecordTable table=new CategoriesRecordTable();
        table.setCategoryId(categoryId);
        table.setCategoryTitle(categoryTitle);
        table.setCount(1);
        table.setTimeStamp(nowTimeStamp);
        table.save();
        Log.e("updateCategoriesRecord","新增了一条记录--"+table.toString());
    }

    /**
     * 更新最喜欢的电台数据
     * @param channelTitle
     * @param channelId
     */
    private void updateChannelRecord(String channelTitle,int channelId){
        Iterator tables= ChannelRecordTable.findAll(ChannelRecordTable.class);
        long nowTimeStamp=System.currentTimeMillis();//获取当前的时间戳
        Calendar calendar=Calendar.getInstance();
        calendar.setTime(new Date(nowTimeStamp));

        while(tables.hasNext()){
            ChannelRecordTable tableObj= (ChannelRecordTable) tables.next();
            //Log.e("updateVisit",tableObj.toString());
            long thisTimeStamp=tableObj.getTimeStamp();//获取数据库的时间戳
            Calendar thisCalendar=Calendar.getInstance();
            thisCalendar.setTime(new Date(thisTimeStamp));
            if (DataBaseUtil.isToday(calendar,thisCalendar)){//如果是同一天
                if (channelId==tableObj.getChannelId()&&channelTitle.equals(tableObj.getChannel())){//判断是否是同一个电台
                    //Log.e("updateRecord","更新今天访问"+channelTitle+"次数");
                    int count=tableObj.getCount()+1;
                    long id=tableObj.getId();
                    SugarRecord.executeQuery("update CHANNEL_RECORD_TABLE set count=? where id=?",count+"",id+"");
                    return;
                }
            }
        }

        //如果没有找到想要的数据，那就增加一条
        ChannelRecordTable table=new ChannelRecordTable();
        table.setChannel(channelTitle);
        table.setCount(1);
        table.setChannelId(channelId);
        table.setTimeStamp(nowTimeStamp);
        table.save();
        Log.e("updateRecord","新增了一条记录--"+table.toString());
    }

    /**
     * 更新适配器里的data属性
     */
    public void upData(){
        JSONArray array=GetFMItemJsonService.getLastGetJson();
        Gson gson=new Gson();
        List<FMCardViewJson> list=
                gson.fromJson(array.toString(), new TypeToken<List<FMCardViewJson>>(){}.getType());
        //更新适配器里的data属性
        this.data= list;
        notifyDataSetChanged();
    }

    @Override
    public int getItemViewType(int position) {
        if (position==data.size()){
            //Log.e("getItemViewType()","true and position is "+position);
            return 1;
        }else{
            //Log.e("getItemViewType()","false and position is "+position);
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

    /**
     * 加载ViewHolder
     */
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

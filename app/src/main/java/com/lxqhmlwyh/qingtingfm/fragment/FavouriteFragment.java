package com.lxqhmlwyh.qingtingfm.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toolbar;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.lxqhmlwyh.qingtingfm.R;
import com.lxqhmlwyh.qingtingfm.adapter.FavouriteAdapter;
import com.lxqhmlwyh.qingtingfm.databaseentities.FavouriteTable;
import com.orm.SugarRecord;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;


/**
 * 显示我的收藏
 */
public class FavouriteFragment extends Fragment {
    private View view;
    private RecyclerView listView;//列表控件
    private SwipeRefreshLayout refreshLayout;//刷新控件
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view=inflater.inflate(R.layout.fragment_favourite,null);

        Toolbar toolbar=view.findViewById(R.id.favor_toolbar);
        toolbar.setTitle("我的收藏");
        refreshLayout=view.findViewById(R.id.refresh_layout);
        listView=view.findViewById(R.id.favor_recycler_view);
        listView.setLayoutManager(new GridLayoutManager(getActivity(),3));
        toolbar.setTitleTextColor(getResources().getColor(R.color.font_white));
        refreshLayout.setColorSchemeResources(R.color.material_red);
        refreshLayout.setOnRefreshListener(()->{
            showFavourite();
            refreshLayout.setRefreshing(false);
        });
        showFavourite();

        return view;
    }

    //一键刷新列表
    private void showFavourite(){
        //获取数据
        Iterator<FavouriteTable> iterator= SugarRecord.findAll(FavouriteTable.class);
        List<FavouriteTable> tables=new ArrayList<>();
        while (iterator.hasNext()){
            tables.add(iterator.next());
        }
        FavouriteAdapter adapter=new FavouriteAdapter(getActivity(),tables);
        listView.setAdapter(adapter);
    }
}

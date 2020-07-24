package com.lxqhmlwyh.qingtingfm.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toolbar;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.lxqhmlwyh.qingtingfm.R;

public class FavouriteFragment extends Fragment {
    private View view;
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view=inflater.inflate(R.layout.fragment_favourite,null);
        initView();
        return view;
    }

    private void initView(){
        Toolbar toolbar=view.findViewById(R.id.favor_toolbar);
        toolbar.setTitle("我的收藏");
        toolbar.setTitleTextColor(getResources().getColor(R.color.font_white));
    }
}

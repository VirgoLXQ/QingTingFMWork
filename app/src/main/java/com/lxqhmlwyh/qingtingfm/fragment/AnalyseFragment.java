package com.lxqhmlwyh.qingtingfm.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import com.lxqhmlwyh.qingtingfm.R;
import com.lxqhmlwyh.qingtingfm.adapter.AnalysePagerAdapter;

import java.util.ArrayList;
import java.util.List;

public class AnalyseFragment extends Fragment {

    private View view;
    private ViewPager viewPager;
    private List<Fragment> fragments;
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view=inflater.inflate(R.layout.fragment_analyse,null);
        fragments=new ArrayList<>();
        FragmentAnalyse1 analyse1=new FragmentAnalyse1();
        FragmentAnalyse2 analyse2=new FragmentAnalyse2();
        FragmentAnalyse3 analyse3=new FragmentAnalyse3();
        FragmentAnalyse4 analyse4=new FragmentAnalyse4();
        FragmentAnalyse5 analyse5=new FragmentAnalyse5();

        fragments.add(analyse1);
        fragments.add(analyse2);
        fragments.add(analyse3);
        fragments.add(analyse4);
        fragments.add(analyse5);
        viewPager=view.findViewById(R.id.viewpager);
        AnalysePagerAdapter adapter=new AnalysePagerAdapter(getActivity().getSupportFragmentManager(),1,fragments);
        viewPager.setAdapter(adapter);
        //viewPager.setOffscreenPageLimit(5);
        return view;
    }

}

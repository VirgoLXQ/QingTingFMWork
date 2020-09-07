package com.lxqhmlwyh.qingtingfm.fragment;

import android.graphics.Color;
import android.os.Bundle;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.lxqhmlwyh.qingtingfm.R;
import com.lxqhmlwyh.qingtingfm.databaseentities.ProvinceKeyTable;
import com.orm.SugarRecord;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * 各地区访问比例
 */
public class FragmentAnalyse3 extends Fragment {

    private Map<String,Integer> gather;
    private PieChart chart;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.analyse_3, null);

        chart=view.findViewById(R.id.analyse_3_chart);
        summary();
        showChart();
        return view;
    }

    //整理数据
    private void summary(){
        Iterator<ProvinceKeyTable> iterator=
        SugarRecord.findAll(ProvinceKeyTable.class);
        List<ProvinceKeyTable> tables=new ArrayList<>();
        //获取数据
        while (iterator.hasNext()){
            tables.add(iterator.next());
        }
        List<String> provinceKey=new ArrayList<>();
        //去除重复得内容
        for (ProvinceKeyTable table:tables){
            String newKey=table.getProvince();
            boolean found=false;
            for (String key:provinceKey){
                if (key.equals(newKey)){
                    found=true;
                    break;
                }
            }
            if (!found){
                provinceKey.add(newKey);
            }
        }

        gather=new HashMap<>();
        //计算访问这个省份得次数
        for (String key:provinceKey){
            //Log.e("key",key);
            int count=0;
            for (ProvinceKeyTable table:tables){
                if (table.getProvince().equals(key)){
                    count+=table.getCount();
                }
            }
            gather.put(key,count);
        }

        /*for (String key:provinceKey){
            Log.e(key,gather.get(key)+"");
        }*/
    }

    //画饼图
    private void showChart(){
        List<PieEntry> entries=new ArrayList<>();//创建数据集合
        Iterator<String> iterator= gather.keySet().iterator();
        int len=1;
        while (iterator.hasNext()){
            //提取前8个省份的数据
            if (len<=8){
                String key=iterator.next();
                entries.add(new PieEntry(gather.get(key),key));
                len++;
            }else{
                break;
            }
        }
        PieDataSet dataSet=new PieDataSet(entries,"各地区访问比例");
        dataSet.setValueTextSize(20);//设置值字体大小
        dataSet.setColors(Color.GREEN,Color.RED,Color.LTGRAY,Color.CYAN,Color.BLUE,//设置颜色
                Color.YELLOW,Color.MAGENTA,Color.DKGRAY);
        PieData pieData=new PieData(dataSet);
        chart.setData(pieData);//为饼图添加数据
        chart.setDrawHoleEnabled(false);//不画同心圆
    }

}

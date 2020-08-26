package com.lxqhmlwyh.qingtingfm.fragment;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.github.mikephil.charting.charts.HorizontalBarChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.lxqhmlwyh.qingtingfm.R;
import com.lxqhmlwyh.qingtingfm.databaseentities.PreferProgramTable;
import com.orm.SugarRecord;

import java.util.ArrayList;
import java.util.List;

public class FragmentAnalyse2 extends Fragment {
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.analyse_2, null);

        HorizontalBarChart chart=view.findViewById(R.id.analyse_2_chart);

        List<PreferProgramTable> tables=
        SugarRecord.findWithQuery(PreferProgramTable.class,
                "select * from PREFER_PROGRAM_TABLE ORDER BY count desc limit 10");

        String[] name=new String[10];
        List<BarEntry> barEntries=new ArrayList<>();
        int i=0;
        for (PreferProgramTable table:tables){
            name[i]=table.getProgramName();
            barEntries.add(new BarEntry(i,table.getCount()));
            //Log.e("table",table.toString());
            i++;
        }

        BarDataSet dataSet=new BarDataSet(barEntries,"最喜欢得节目前10名");
        BarData data=new BarData(dataSet);
        chart.setData(data);
        XAxis xAxis= chart.getXAxis();
        xAxis.setGranularity(1);
        xAxis.setLabelCount(10);
        xAxis.setTextSize(15);
        xAxis.setValueFormatter((v, axisBase) -> name[(int)v]);
        return view;
    }
}

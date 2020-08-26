package com.lxqhmlwyh.qingtingfm.fragment;

import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.lxqhmlwyh.qingtingfm.R;
import com.lxqhmlwyh.qingtingfm.databaseentities.APPVisitTable;
import com.orm.SugarRecord;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;

/**
 * APP访问次数
 */
public class FragmentAnalyse1 extends Fragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.analyse_1, null);
        LineChart chartView = view.findViewById(R.id.analyse_1_chart);

        Iterator<APPVisitTable> iterator= SugarRecord.findAll(APPVisitTable.class);
        List<APPVisitTable> tables=new ArrayList<>();
        while(iterator.hasNext()){
            tables.add(iterator.next());
        }
        List<Entry> entries=new ArrayList<>();
        List<String> timeLine=new ArrayList<>();
        int i=0;
        for (APPVisitTable table:tables){
            String MD=
            new SimpleDateFormat("M月dd日", Locale.CHINA).format(new Date(table.getTimeStamp()));
            timeLine.add(MD);
            //Log.e(MD,table.getCount()+"次");
            entries.add(new Entry(i,table.getCount()));
            i++;
        }

        LineDataSet dataSet=new LineDataSet(entries,"app历史访问次数");
        dataSet.setLineWidth(2f);
        dataSet.setValueTextSize(10);
        dataSet.setColor(Color.RED);
        LineData data=new LineData(dataSet);
        chartView.setData(data);

        XAxis xAxis=chartView.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setLabelRotationAngle(45);
        xAxis.setValueFormatter((v, axisBase) -> timeLine.get((int) v));

        return view;
    }
}

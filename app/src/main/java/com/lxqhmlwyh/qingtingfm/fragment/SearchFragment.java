package com.lxqhmlwyh.qingtingfm.fragment;



import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.lxqhmlwyh.qingtingfm.R;
import com.lxqhmlwyh.qingtingfm.service.InitDataService;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.PipedOutputStream;
import java.util.ArrayList;
import java.util.List;

public class SearchFragment extends Fragment {

    private View view;
    private SwipeRefreshLayout swipeRefreshLayout;
    private PopupWindow popupWindow;
    private View chooseDialog;
    private ListView provinceList;
    private TextView tvProvince;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment_search,null);
        this.view=view;
        initView();
        initDialog();
        return view;
    }

    private void initView(){
        swipeRefreshLayout=view.findViewById(R.id.swipe);
        tvProvince=view.findViewById(R.id.tv_province);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                swipeRefreshLayout.setRefreshing(false);
            }
        });
        view.findViewById(R.id.choose_province).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showChooseProvinceWindow();
            }
        });
    }

    private void initDialog(){
        popupWindow=new PopupWindow();
        chooseDialog=View.inflate(getActivity(),R.layout.choose_province_dialog,null);
        popupWindow.setContentView(chooseDialog);
        provinceList=chooseDialog.findViewById(R.id.dialog_list_view);
        final List<String> provinces=new ArrayList<>();
        JSONArray provinceData= InitDataService.getDistrict();
        for (int i=0;i<=provinceData.length();i++){
            try {
                JSONObject tempStr=provinceData.getJSONObject(i);
                provinces.add(tempStr.getString("title"));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        final ArrayAdapter adapter=new ArrayAdapter(getActivity(),android.R.layout.simple_list_item_1,provinces);
        provinceList.setAdapter(adapter);
        provinceList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                tvProvince.setText(provinces.get(i));
                popupWindow.dismiss();
                backgroundAlpha(1);
            }
        });
    }
    private void showChooseProvinceWindow(){
        if (popupWindow.isShowing())return;
        backgroundAlpha(0.5f);
        popupWindow.setWidth(view.getLayoutParams().width);
        popupWindow.setHeight(500);
        popupWindow.showAtLocation(view,Gravity.BOTTOM,0,55);
        chooseDialog.findViewById(R.id.close_choose_dialog).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                popupWindow.dismiss();
                backgroundAlpha(1);
            }
        });
    }

    private void backgroundAlpha(float f) {
        WindowManager.LayoutParams lp =getActivity().getWindow().getAttributes();
        lp.alpha = f;
        getActivity().getWindow().setAttributes(lp);
    }
}

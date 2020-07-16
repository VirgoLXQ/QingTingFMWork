package com.lxqhmlwyh.qingtingfm.fragment;


import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.lxqhmlwyh.qingtingfm.R;
import com.lxqhmlwyh.qingtingfm.service.InitDataService;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class SearchFragment extends Fragment {

    private View view;
    private SwipeRefreshLayout swipeRefreshLayout;
    private PopupWindow popupWindow;
    private View chooseDialog;
    private ListView provinceList;
    private TextView tvProvince;
    private RelativeLayout maskLayout;
    private RecyclerView fmRecyclerView;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_search, null);
        this.view = view;
        initView();
        initDialog();
        return view;
    }

    private void initView() {
        swipeRefreshLayout = view.findViewById(R.id.swipe);
        tvProvince = view.findViewById(R.id.tv_province);
        fmRecyclerView=view.findViewById(R.id.fm_recycler_view);
        maskLayout = view.findViewById(R.id.fragment_search_mask);
        maskLayout.setOnClickListener(onClickListener);
        swipeRefreshLayout.setEnabled(false);
        /*swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                swipeRefreshLayout.setRefreshing(false);
            }
        });*/
        view.findViewById(R.id.choose_province).setOnClickListener(onClickListener);
    }

    /**
     * 初始化dialog
     */
    private void initDialog() {
        popupWindow = new PopupWindow();
        chooseDialog = View.inflate(getActivity(), R.layout.choose_province_dialog, null);
        popupWindow.setContentView(chooseDialog);
        //popupWindow.setFocusable(true);
        provinceList = chooseDialog.findViewById(R.id.dialog_list_view);
        final List<String> provinces = new ArrayList<>();
        JSONArray provinceData = InitDataService.getDistrict();
        for (int i = 0; i <= provinceData.length(); i++) {
            try {
                JSONObject tempStr = provinceData.getJSONObject(i);
                provinces.add(tempStr.getString("title"));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        final ArrayAdapter adapter = new ArrayAdapter(getActivity(), android.R.layout.simple_list_item_1, provinces);
        provinceList.setAdapter(adapter);
        provinceList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                tvProvince.setText(provinces.get(i));
                dismissDialog();
            }
        });
    }

    /**
     * 显示dialog
     */
    private void showChooseProvinceWindow() {
        if (popupWindow.isShowing()) return;
        /*((InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE))
                .hideSoftInputFromWindow(view.getApplicationWindowToken(), 0);*/
        //backgroundAlpha(0.5f);
        maskLayout.setVisibility(View.VISIBLE);
        maskLayout.startAnimation(AnimationUtils.loadAnimation(getActivity(),R.anim.anim_choose_dialog_mask_after));
        popupWindow.setWidth(view.getLayoutParams().width);
        popupWindow.setHeight(view.getHeight() / 2);
        //popupWindow.setAnimationStyle(R.style.pop_animation);
        //popupWindow.showAtLocation(maskLayout, Gravity.BOTTOM, 0, 0);
        popupWindow.showAsDropDown(view);
        chooseDialog.findViewById(R.id.close_choose_dialog).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismissDialog();
            }
        });

    }

    private View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View btnView) {
            switch (btnView.getId()) {
                case R.id.fragment_search_mask:
                    dismissDialog();
                    break;
                case R.id.choose_province:
                    showChooseProvinceWindow();
                    break;
            }
        }
    };

    /**
     * 遮罩层
     */
    /*private void backgroundAlpha(float f) {
        maskLayout.setAlpha(f);
        maskLayout.setVisibility(View.VISIBLE);
    }*/

    /**
     * 关闭dialog
     */
    public void dismissDialog() {
        popupWindow.dismiss();
        //backgroundAlpha(1);
        maskLayout.startAnimation(AnimationUtils.loadAnimation(getActivity(),R.anim.anim_choose_dialog_mask_before));
        maskLayout.setVisibility(View.GONE);
    }
}

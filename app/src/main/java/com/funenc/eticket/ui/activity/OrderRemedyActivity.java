package com.funenc.eticket.ui.activity;

import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.funenc.eticket.R;
import com.funenc.eticket.fragment.FragmentMessageList;
import com.funenc.eticket.fragment.FragmentStationList;
import com.funenc.eticket.model.OrderListResponse;
import com.funenc.eticket.storage.AppStore;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

import static android.graphics.Color.WHITE;

/**
 * Created by tingken.com on 2018-10-30.
 */
public class OrderRemedyActivity extends FragmentActivity {

    @InjectView(R.id.tabLayout)
    TabLayout tabLayout;
    @InjectView(R.id.indicator)
    View indicator;
    @InjectView(R.id.viewpager)
    ViewPager viewPager;
    private Fragment[] fragments;

    private static final Map<String, String> LINE_NO_TO_NAME = new HashMap<>();

    static {
        LINE_NO_TO_NAME.put("01", "一号线");
        LINE_NO_TO_NAME.put("02", "二号线");
        LINE_NO_TO_NAME.put("03", "三号线");
        LINE_NO_TO_NAME.put("04", "五号线");
        LINE_NO_TO_NAME.put("05", "六号线");
        LINE_NO_TO_NAME.put("07", "七号线");
        LINE_NO_TO_NAME.put("08", "八号线");
        LINE_NO_TO_NAME.put("09", "九号线");
        LINE_NO_TO_NAME.put("10", "十号线");
        LINE_NO_TO_NAME.put("11", "十一号线");
        LINE_NO_TO_NAME.put("12", "十二号线");
        LINE_NO_TO_NAME.put("13", "十三号线");
    }

    private List<String> lineNoList;
    private OrderListResponse.Order order;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_remedy);
        ButterKnife.inject(this);

        order = (OrderListResponse.Order) getIntent().getExtras().getSerializable(AppStore.ORDER);
        Log.d("OrderRemedyActivity", "order=" + (order == null ? "" : order.toString()));
        // init fragments
        lineNoList = new ArrayList<>(AppStore.getStationLineInfo().keySet());
        Collections.sort(lineNoList, new Comparator<String>() {
            @Override
            public int compare(String o1, String o2) {
                if (o1 == null) {
                    return 0;
                }
                return o1.compareTo(o2);
            }
        });
        fragments = new Fragment[lineNoList.size()];
        for (int i = 0; i < lineNoList.size(); ++i) {
            Fragment fragment = new FragmentStationList();
            Bundle args = new Bundle();
            args.putString(FragmentStationList.LINE_NO, lineNoList.get(i));
            args.putSerializable(AppStore.ORDER, order);
            fragment.setArguments(args);
            fragments[i] = fragment;
        }
        viewPager.setAdapter(new FragmentPagerAdapter(getSupportFragmentManager()) {
            @Override
            public int getCount() {
                return fragments.length;
            }

            @Override
            public Fragment getItem(int position) {
                return fragments[position];
            }

            @Override
            public CharSequence getPageTitle(int position) {
                return LINE_NO_TO_NAME.get(lineNoList.get(position));
            }
        });
        tabLayout.setupWithViewPager(viewPager);
        tabLayout.removeAllTabs();
        initTabView();
//        TextView tabTextView = (TextView)(((LinearLayout) ((LinearLayout) tabLayout.getChildAt(0)).getChildAt(0)).getChildAt(1));
//        tabTextView.setTextSize(20);
//        int[] location = new int[2];
//        tabTextView.getLocationInWindow(location);
//        indicator.setX(location[0]);
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
//                TextView tabTextView = (TextView)(((LinearLayout) ((LinearLayout) tabLayout.getChildAt(0)).getChildAt(tab.getPosition())).getChildAt(1));
//                tabTextView.setTextSize(20); // no effect
                TextView tabTextView = (TextView) tab.getCustomView().findViewById(R.id.tv_tab_name);
                setTabItemState(tabTextView, true);
                int[] location = new int[2];
                tabTextView.getLocationInWindow(location);
                indicator.animate().setDuration(getResources().getInteger(android.R.integer.config_shortAnimTime)).x(location[0] + (tabTextView.getWidth() - indicator.getWidth()) / 2);
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
//                TextView tabTextView = (TextView)(((LinearLayout) ((LinearLayout) tabLayout.getChildAt(0)).getChildAt(tab.getPosition())).getChildAt(1));
//                tabTextView.setTextSize(16); // no effect
                TextView tabTextView = (TextView) tab.getCustomView().findViewById(R.id.tv_tab_name);
                setTabItemState(tabTextView, false);
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
    }

    /**
     * 创建自定义Tab
     */
    private TabLayout.Tab createTab(String title) {
        //依次获取标签
        TabLayout.Tab tab = tabLayout.newTab();
        //为每个标签设置布局
        tab.setCustomView(R.layout.custom_tab_item_with_note);
        TextView tabTextView = (TextView) tab.getCustomView().findViewById(R.id.tv_tab_name);
        tabTextView.setTextColor(WHITE);
        //为标签填充数据
        tabTextView.setText(title);
        TextView tvCount = (TextView) tab.getCustomView().findViewById(R.id.tv_count);
        tvCount.setVisibility(View.GONE);
        return tab;
    }

    /**
     * 设置Tab的样式
     */
    private void initTabView() {
        int i = 0;
        for (String lineNo : lineNoList) {
            //依次获取标签
            TabLayout.Tab tab = createTab(LINE_NO_TO_NAME.get(lineNo));
            tabLayout.addTab(tab);
            TextView tabTextView = (TextView) tab.getCustomView().findViewById(R.id.tv_tab_name);
            //默认选择第一项
            if (i++ == 0) {
                tab.select();
                setTabItemState(tabTextView, true);
            } else {
                setTabItemState(tabTextView, false);
            }
        }
    }

    private static final void setTabItemState(TextView tabTextView, boolean selected) {
        if (selected) {
            tabTextView.setSelected(true);
            tabTextView.setTextSize(20);
        } else {
            tabTextView.setSelected(false);
            tabTextView.setTextSize(16);
        }
    }

    @OnClick(R.id.back)
    void onBack(View view) {
        finish();
    }

    @Override
    protected void onStart() {
        super.onStart();
        final TextView tabTextView = (TextView) tabLayout.getTabAt(0).getCustomView().findViewById(R.id.tv_tab_name);
        tabTextView.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {

                int[] location = new int[2];
                tabTextView.getLocationInWindow(location);
                indicator.setX(location[0] + (tabTextView.getWidth() - indicator.getWidth()) / 2);
            }
        });
    }
}

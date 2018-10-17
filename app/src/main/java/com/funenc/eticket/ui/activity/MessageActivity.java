package com.funenc.eticket.ui.activity;

import android.os.Handler;
import android.os.Message;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.Window;
import android.widget.TextView;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.funenc.eticket.R;
import com.funenc.eticket.api.OperationService;
import com.funenc.eticket.fragment.FragmentMessageList;
import com.funenc.eticket.model.MessageCountResponse;
import com.funenc.eticket.okhttp.ApiFactory;
import com.funenc.eticket.storage.AppStore;

import java.io.IOException;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

import static android.graphics.Color.WHITE;

public class MessageActivity extends FragmentActivity {

    private String[] TAB_NAMES = {"消息", "通知"};
    private TabLayout tabLayout;
    @InjectView(R.id.indicator)
    View indicator;
    @InjectView(R.id.viewpager)
    ViewPager viewPager;
    @InjectView(R.id.notice)
    TextView tvNotice;
    private Fragment[] fragments;

    int[] unreadCounts = new int[2];
    private Handler updateHandler;

    public MessageActivity() {
        updateHandler = new Handler(){
            @Override
            public void handleMessage(Message msg) {
                boolean allRead = true;
                for(int i=0; i<2; ++i){
                    TextView tvCount = (TextView) tabLayout.getTabAt(i).getCustomView().findViewById(R.id.tv_count);
                    if(unreadCounts[i] > 0){
                        tvCount.setText(String.valueOf(unreadCounts[i]));
                        tvCount.setVisibility(View.VISIBLE);
                        allRead = false;
                    }else {
                        tvCount.setVisibility(View.GONE);
                    }
                }
                if(allRead){
                    tvNotice.setText(R.string.allRead);
                }else {
                    tvNotice.setText("");
                }
            }
        };
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
//        getSupportActionBar().hide();
        setContentView(R.layout.activity_message);
        ButterKnife.inject(this);
        tabLayout = findViewById(R.id.tablayout);

        // init fragments
        fragments = new Fragment[2];
        fragments[0] = new FragmentMessageList();
        Bundle args0 = new Bundle();
        args0.putInt(FragmentMessageList.IS_MESSAGE, 1);
        fragments[0].setArguments(args0);
        fragments[1] = new FragmentMessageList();
        Bundle args1 = new Bundle();
        args1.putInt(FragmentMessageList.IS_MESSAGE, 0);
        fragments[1].setArguments(args1);
        viewPager.setAdapter(new FragmentPagerAdapter(getSupportFragmentManager()) {
            @Override
            public int getCount() {
                return fragments.length;
            }

            @Override
            public Fragment getItem(int position) {
                return fragments[position];
            }
        });
        tabLayout.setupWithViewPager(viewPager);
        tabLayout.removeAllTabs();
        initTabView();

        // handle actions after tab select
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                TextView tabTextView = (TextView) tab.getCustomView().findViewById(R.id.tv_tab_name);
                setTabItemState(tabTextView, true);
                int[] location = new int[2];
                tabTextView.getLocationInWindow(location);
                indicator.animate().setDuration(getResources().getInteger(android.R.integer.config_shortAnimTime)).x(location[0]);
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
                TextView tabTextView = (TextView) tab.getCustomView().findViewById(R.id.tv_tab_name);
                setTabItemState(tabTextView, false);
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

//        tabLayout.post(new Runnable() {
//            @Override
//            public void run() {
//                try {
//                    //拿到tabLayout的mTabStrip属性
//                    Field mTabStripField = tabLayout.getClass().getDeclaredField("mTabStrip");
//                    mTabStripField.setAccessible(true);
//
//                    LinearLayout mTabStrip = (LinearLayout) mTabStripField.get(tabLayout);
//
//                    int dp10 = dip2Px(10);
//
//                    for (int i = 0; i < mTabStrip.getChildCount(); i++) {
//                        View tabView = mTabStrip.getChildAt(i);
//
//                        //拿到tabView的mTextView属性
//                        Field mTextViewField = tabView.getClass().getDeclaredField("mTextView");
//                        mTextViewField.setAccessible(true);
//
//                        TextView mTextView = (TextView) mTextViewField.get(tabView);
//
//                        tabView.setPadding(0, 0, 0, 0);
//
//                        //因为我想要的效果是   字多宽线就多宽，所以测量mTextView的宽度
//                        int width = 0;
//                        width = mTextView.getWidth();
//                        if (width == 0) {
//                            mTextView.measure(0, 0);
//                            width = mTextView.getMeasuredWidth();
//                        }
//
//                        //设置tab左右间距为10dp  注意这里不能使用Padding 因为源码中线的宽度是根据 tabView的宽度来设置的
//                        LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) tabView.getLayoutParams();
//                        params.width = dip2Px(32) ;
////                                    params.leftMargin = dp10;
////                                    params.rightMargin = dp10;
//                        tabView.setLayoutParams(params);
//
////                                    tabView.invalidate();
//                    }
//
//                } catch (NoSuchFieldException e) {
//                    e.printStackTrace();
//                } catch (IllegalAccessException e) {
//                    e.printStackTrace();
//                }
//            }
//        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.d("MessageActivity", tabLayout !=null?"tabLayout is not null":"tabLayout is null");
        Log.d("MessageActivity", findViewById(R.id.tab1)==null?"tab1 is null":"tab1 is not null");
        Log.d("MessageActivity", findViewById(R.id.tab2)==null?"tab2 is null":"tab2 is not null");
//        new QBadgeView(this).bindTarget(tabLayout.getChildAt(0)).setBadgeNumber(5);
//        new QBadgeView(this).bindTarget(findViewById(R.id.tab2)).setBadgeNumber(8);
//        indicator.post(new Runnable() {
//            @Override
//            public void run() {
//                tabLayout.getTabAt(0).select();
//                //
//            }
//        });
        final TextView tabTextView = (TextView) tabLayout.getTabAt(0).getCustomView().findViewById(R.id.tv_tab_name);
        tabTextView.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {

                int[] location = new int[2];
                tabTextView.getLocationInWindow(location);
                indicator.setX(location[0]);
            }
        });

        ApiFactory apiFactory = new ApiFactory();
        apiFactory.createService(OperationService.BASE_ADDR, OperationService.class, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.e("FragmentMessageList", "getInformationList error", e);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String body = response.body().string();
                Log.i("MessageList response", body);
                if (response.isSuccessful()) {
                    ObjectMapper mapper = new ObjectMapper();
                    MessageCountResponse result = mapper.readValue(body, MessageCountResponse.class);
                    unreadCounts[0] = result.getContent().getMessageCount();
                    unreadCounts[1] = result.getContent().getNotifCount();
                    updateHandler.sendEmptyMessage(0);
                }
            }
        }).getInformationUnreadCount(AppStore.getToken(this));
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
        return tab;
    }

    /**
     * 设置Tab的样式
     */
    private void initTabView() {
        for (int i = 0; i < TAB_NAMES.length; i++) {
            //依次获取标签
            TabLayout.Tab tab = createTab(TAB_NAMES[i]);
            tabLayout.addTab(tab);
            TextView tabTextView = (TextView) tab.getCustomView().findViewById(R.id.tv_tab_name);
            //默认选择第一项
            if (i == 0) {
                tab.select();
                setTabItemState(tabTextView, true);
            }else{
                setTabItemState(tabTextView, false);
            }
        }
    }

    private static final void setTabItemState(TextView tabTextView, boolean selected){
        if(selected){
            tabTextView.setSelected(true);
            tabTextView.setTextSize(20);
        }else{
            tabTextView.setSelected(false);
            tabTextView.setTextSize(16);
        }
    }

    @OnClick(R.id.back)
    void onBack(View view){
        finish();
    }
}

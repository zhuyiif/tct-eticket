package com.example.eticket.fragment;

import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.TextView;

import com.example.eticket.ListViewAdapter;
import com.example.eticket.Person;
import com.example.eticket.R;
import com.example.eticket.ViewPagerAdapter;
import com.example.eticket.model.HeadlineCateItem;
import com.example.eticket.okhttp.HttpUtils;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.tmall.ultraviewpager.UltraViewPager;
import com.tmall.ultraviewpager.UltraViewPagerAdapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;


public class Fragment1 extends Fragment {

    //This is our tablayout
    private TabLayout tabLayout;

    //This is our viewPager
    private ViewPager viewPager;

    ViewPagerAdapter adapter;

    Fragment6 testFragment6;
    Fragment7 testFragment7;
    Fragment8 testFragment8;
    Fragment9 testFragment9;
    Fragment10 testFragment10;

    private  ArrayList<HeadlineCateItem> headlineCateItems = new ArrayList<>();

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return LayoutInflater.from(getActivity()).inflate(R.layout.fragment1, container, false);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        adapter = new ViewPagerAdapter(getChildFragmentManager());
        testFragment6=new Fragment6();
        adapter.addFragment(testFragment6,"6");

        testFragment7=new Fragment7();
        adapter.addFragment(testFragment7,"7");

        testFragment8=new Fragment8();
        adapter.addFragment(testFragment8,"8");

        testFragment9 = new Fragment9();
        adapter.addFragment(testFragment9,"9");

        testFragment10 = new Fragment10();
        adapter.addFragment(testFragment10,"10");



    }

    @Override
    public void onStart() {
        super.onStart();
        HttpUtils httpUtils = new HttpUtils();

        final UltraViewPager ultraViewPager = (UltraViewPager) getActivity().findViewById(R.id.ultra_viewpager);
        ultraViewPager.setScrollMode(UltraViewPager.ScrollMode.HORIZONTAL);
//initialize UltraPagerAdapter，and add child view to UltraViewPager
        final UltraPagerAdapter adapter = new UltraPagerAdapter(false);
        ultraViewPager.setAdapter(adapter);

//initialize built-in indicator
        ultraViewPager.initIndicator();
//set style of indicators
        ultraViewPager.getIndicator()
                .setOrientation(UltraViewPager.Orientation.HORIZONTAL)
                .setFocusColor(Color.GREEN)
                .setNormalColor(Color.WHITE)
                .setRadius((int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 5, getResources().getDisplayMetrics()));
//set the alignment
        ultraViewPager.getIndicator().setGravity(Gravity.CENTER_HORIZONTAL | Gravity.BOTTOM);
//construct built-in indicator, and add it to  UltraViewPager
        ultraViewPager.getIndicator().build();

//set an infinite loop
       // ultraViewPager.setInfiniteLoop(true);
//enable auto-scroll mode
       // ultraViewPager.setAutoScroll(2000);


        //Initializing viewPager
        viewPager = (ViewPager) getActivity().findViewById(R.id.viewpager);
        viewPager.setOffscreenPageLimit(3);
        setupViewPager(viewPager);

        //Initializing the tablayout
        tabLayout = (TabLayout) getActivity().findViewById(R.id.tablayout);


        try {
            httpUtils.getHeadlineCate(new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {

                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    String responseString = response.body().string();
                    Log.d("get headline", responseString);

                    JSONObject respObj = null;
                    try {
                        respObj = new JSONObject(responseString);
                        JSONObject contentObj = respObj.getJSONObject("content");
                        final JSONArray jArray = contentObj.getJSONArray("list");

                        Gson gson = new Gson();
                        Type headCateType = new TypeToken<List<HeadlineCateItem>>() {
                        }.getType();

                        headlineCateItems =  gson.fromJson(jArray.toString(), headCateType);


                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                tabLayout.removeAllTabs();

                                for (int i = 0; i < headlineCateItems.size(); i++) {
                                    tabLayout.addTab(tabLayout.newTab().setText(headlineCateItems.get(i).getTitle()));
                                }


                                initTabView();

                                tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
                                    @Override
                                    public void onTabSelected(TabLayout.Tab tab) {
                                        TextView tabTextView = (TextView) tab.getCustomView().findViewById(R.id.tv_tab_name);
                                        setTabItemState(tabTextView, true);

                                        ScrollView scrollView =  getActivity().findViewById(R.id.scrollView);
                                        int currentY = scrollView.getScrollY();

                                        viewPager.setCurrentItem(tab.getPosition(),false);
                                        scrollView.smoothScrollTo(0,currentY);

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

                            }
                        });


                    } catch (JSONException e) {
                        e.printStackTrace();
                    }


                }
            });
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }



        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                tabLayout.getTabAt(position).select();

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });


        ScrollView scrollView =  getActivity().findViewById(R.id.scrollView);

        scrollView.smoothScrollTo(0,0);




        try {
            httpUtils.getBanner(new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {

                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    String responseString = response.body().string();
                    Log.d("frag1", responseString);
                    JSONObject respObj = null;
                    try {
                        respObj = new JSONObject(responseString);
                        JSONObject contentObj = respObj.getJSONObject("content");
                        final JSONArray jArray = contentObj.getJSONArray("list");


                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {

                                ArrayList<String> items = new ArrayList<>();

                                for (int i=0; i < jArray.length(); i++)
                                {
                                    try {
                                        JSONObject oneObject = jArray.getJSONObject(i);
                                        // Pulling items from the array
                                        String oneObjectsItem = oneObject.getString("cover");
                                        items.add(oneObjectsItem);

                                    } catch (JSONException e) {
                                        // Oops
                                    }
                                }

                                adapter.addItems(items);

                                ultraViewPager.refresh();
                                ultraViewPager.getWrapAdapter().notifyDataSetChanged();

                            }
                        });



                    } catch (JSONException e) {
                        e.printStackTrace();
                    }


                }
            });
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }


    }

    private List<Person> readListFromFile(){
        try{
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(getActivity().getAssets()
                    .open("persons.txt")));
            StringBuilder stringBuilder = new StringBuilder();
            String line;
            while((line = bufferedReader.readLine()) != null){
                stringBuilder.append(line);
            }
            Gson gson = new Gson();
            return gson.fromJson(stringBuilder.toString(), new TypeToken<List<Person>>(){}
                    .getType());
        }catch (IOException exception){
            exception.printStackTrace();
            return new ArrayList<>();
        }
    }

    private void setupViewPager(ViewPager viewPager)
    {

        viewPager.setAdapter(adapter);

    }

    /**
     * 设置Tab的样式
     */
    private void initTabView() {
        for (int i = 0; i < tabLayout.getTabCount(); i++) {
            //依次获取标签
            TabLayout.Tab tab = tabLayout.getTabAt(i);
            //为每个标签设置布局
            tab.setCustomView(R.layout.custom_tab_item);
            TextView tabTextView = (TextView) tab.getCustomView().findViewById(R.id.tv_tab_name);
            //为标签填充数据
            tabTextView.setText(tab.getText());
            //默认选择第一项
            if (i == 0) {
                setTabItemState(tabTextView, true);
            }else{
                setTabItemState(tabTextView, false);
            }
        }
    }

    private static final void setTabItemState(TextView tabTextView, boolean selected){
        if(selected){
            tabTextView.setSelected(true);
            tabTextView.setTextSize(15);
            tabTextView.setTypeface(tabTextView.getTypeface(), Typeface.BOLD);
        }else{
            tabTextView.setSelected(false);
            tabTextView.setTextSize(11);
            tabTextView.setTypeface(tabTextView.getTypeface(), Typeface.NORMAL);
        }
    }
}

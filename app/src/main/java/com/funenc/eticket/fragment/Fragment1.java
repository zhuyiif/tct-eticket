package com.funenc.eticket.fragment;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.funenc.eticket.Person;
import com.funenc.eticket.R;
import com.funenc.eticket.api.OperationService;
import com.funenc.eticket.model.FelicityListResponse;
import com.funenc.eticket.model.HeadlineCateItem;
import com.funenc.eticket.okhttp.ApiFactory;
import com.funenc.eticket.okhttp.HttpUtils;
import com.funenc.eticket.storage.AppStore;
import com.funenc.eticket.ui.activity.WebViewActivity;
import com.funenc.eticket.ui.adapter.FelicityListAdapter;
import com.funenc.eticket.ui.component.ObservableScrollView;
import com.funenc.eticket.util.StringUtils;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.tmall.ultraviewpager.UltraViewPager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.SortedSet;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;


public class Fragment1 extends Fragment {

    //This is our tablayout
    private TabLayout tabLayout;

    //This is our viewPager
    private ViewPager viewPager;

    HeadlinePagerAdapter headlineAdapter;

    private  ArrayList<HeadlineCateItem> headlineCateItems = new ArrayList<>();
    private SortedSet<Integer> categoryIdSet;
    private Handler headlineCategoryUpdateHandler;

    @InjectView(R.id.scrollView)
    ObservableScrollView scrollView;
    @InjectView(R.id.felicityListView)
    GridView felicityListView;
    private List<FelicityListResponse.Felicity> felicityList;
    private FelicityListAdapter felicityListAdapter;
    private FelicityListResponse felicityListResponse;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = LayoutInflater.from(getActivity()).inflate(R.layout.fragment1, container, false);
        ButterKnife.inject(this, view);

        final RelativeLayout tabRootLayout = view.findViewById(R.id.tabrootlayout);
        final LinearLayout tabFloatContainer = view.findViewById(R.id.tabFloatContainer);
        final View tabLayout = view.findViewById(R.id.tablayout);
        scrollView.setScrollViewListener(new ObservableScrollView.ScrollViewListener() {
            @Override
            public void onScrollChanged(ScrollView v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
                if(scrollY > tabRootLayout.getY()){
                    if(!tabFloatContainer.equals(tabLayout.getParent())) {
                        tabRootLayout.removeView(tabLayout);
                        tabFloatContainer.setVisibility(View.VISIBLE);
                        tabFloatContainer.addView(tabLayout);
                    }
                }else{
                    if(!tabLayout.getParent().equals(tabRootLayout)) {
                        tabFloatContainer.removeView(tabLayout);
                        tabRootLayout.addView(tabLayout);
                        tabFloatContainer.setVisibility(View.GONE);
                    }
                }
            }
        });
        felicityList = new ArrayList<>();
        FelicityListResponse.Felicity felicity = new FelicityListResponse.Felicity();
        felicityList.add(felicity);
        felicityList.add(felicity);
        felicityListAdapter = new FelicityListAdapter(getContext(), felicityList);
        felicityListView.setAdapter(felicityListAdapter);
        felicityListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String url = felicityList.get(position).getUrl();
                if(!StringUtils.isBlank(url)) {
                    if(!url.matches("http(s)?://.*")){
                        url = OperationService.BASE_ADDR + url;
                    }
                    Intent intent = new Intent().setClass(getContext(), WebViewActivity.class);
                    intent.putExtra(WebViewActivity.URL, url);
                    startActivity(intent);
                }
            }
        });
        layoutFelicityListView(felicityList);
        final Handler felicityUpdateHandler = new Handler(){
            @Override
            public void handleMessage(Message msg) {
                felicityList.clear();
                felicityList.addAll(felicityListResponse.getContent().getList());
                felicityListAdapter.notifyDataSetChanged();
            }
        };
        ApiFactory apiFactory = new ApiFactory();
        apiFactory.createService(OperationService.BASE_ADDR, OperationService.class, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.e("WEB API", "when calling getFelicityList", e);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String body = response.body().string();
                Log.d("getFelicityList", body);
                if (response.isSuccessful()) {
                    ObjectMapper mapper = new ObjectMapper();
                    try {
                        felicityListResponse = mapper.readValue(body, FelicityListResponse.class);
                        if(felicityListResponse.getCode() == 0){
                            felicityUpdateHandler.sendEmptyMessage(felicityListResponse.getContent().getList().size());
                        }
                    } catch (IOException e) {
                        Log.e("getFelicityList", "failure", e);
                    }
                }
            }
        }).getFelicityList();
        return view;
    }

    private void layoutFelicityListView(List<FelicityListResponse.Felicity> felicityList) {
        felicityListView.setNumColumns(felicityList.size());
        int columnWidth = Float.valueOf(TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP, 180, getContext().getResources().getDisplayMetrics())).intValue();
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                columnWidth*felicityList.size(), LinearLayout.LayoutParams.WRAP_CONTENT);
        felicityListView.setLayoutParams(params); //
        felicityListView.setColumnWidth(columnWidth);
        felicityListView.setHorizontalSpacing(0);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d("ViewPagerAdapter", "init");

        headlineAdapter = new HeadlinePagerAdapter(getChildFragmentManager());

    }

    @Override
    public void onStart() {
        super.onStart();
        HttpUtils httpUtils = new HttpUtils();

        headlineCategoryUpdateHandler = new Handler(){
            @Override
            public void handleMessage(Message msg) {
//                for (int i = 0; i < headlineCateItems.size(); i++) {
//                    HeadlineCateItem headlineCateItem = headlineCateItems.get(i);
//                    Fragment fragment = new FragmentHeadlineList();
//                    Bundle args = new Bundle();
//                    args.putInt(FragmentHeadlineList.CATEGORY_ID_KEY, headlineCateItem.getId());
//                    fragment.setArguments(args);
//                }
                headlineAdapter.notifyDataSetChanged();
                super.handleMessage(msg);
            }
        };

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
                                    tabLayout.addTab(createTab(headlineCateItems.get(i).getTitle()));
                                }


//                                initTabView();
                                headlineCategoryUpdateHandler.sendEmptyMessage(headlineCateItems.size());

                                tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
                                    @Override
                                    public void onTabSelected(TabLayout.Tab tab) {
                                        TextView tabTextView = (TextView) tab.getCustomView().findViewById(R.id.tv_tab_name);
                                        setTabItemState(tabTextView, true);

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

        viewPager.setAdapter(headlineAdapter);

    }

    /**
     * 创建自定义Tab
     */
    private TabLayout.Tab createTab(String title) {
        //依次获取标签
        TabLayout.Tab tab = tabLayout.newTab();
        //为每个标签设置布局
        tab.setCustomView(R.layout.custom_tab_item);
        TextView tabTextView = (TextView) tab.getCustomView().findViewById(R.id.tv_tab_name);
        //为标签填充数据
        tabTextView.setText(title);
        return tab;
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

    private static String makeFragmentName(int viewId, long id) {
        return "android:switcher:" + viewId + ":" + id;
    }

    private List<Boolean> fragmentsUpdateFlag = new ArrayList<Boolean>();
    /**
     * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
     * one of the sections/tabs/pages.
     */
    public class HeadlinePagerAdapter extends FragmentPagerAdapter {
        private List<Fragment> fragmentList;

        public HeadlinePagerAdapter(FragmentManager fm) {
            super(fm);
            fragmentList = new ArrayList<Fragment>(headlineCateItems.size());
        }

        @Override
        public Fragment getItem(int position) {
            Fragment fragment = null;
            Bundle args = new Bundle();

            if (headlineCateItems.size() > position) {
                fragment = new FragmentHeadlineList();
                args.putInt(FragmentHeadlineList.CATEGORY_ID_KEY, headlineCateItems.get(position).getId());
                fragment.setArguments(args);
            }else{
                fragment = new Fragment();
            }
            if (position >= fragmentList.size()) {
                fragmentList.add(fragment);
            } else {
                fragmentList.remove(position);
                fragmentList.add(position, fragment);
            }
            if (fragmentsUpdateFlag.size() > position) {
                fragmentsUpdateFlag.set(position, false);
            }
            // getItem is called to instantiate the fragment for the given page.
            // Return a PlaceholderFragment (defined as a static inner class below).
            return fragment;
        }

        @Override
        public int getItemPosition(Object object){
            for(int pos =0;pos<fragmentList.size();++pos){
                if(object.equals(fragmentList.get(pos))){
                    if(fragmentsUpdateFlag.size()>pos && fragmentsUpdateFlag.get(pos)){
                        return POSITION_NONE;
                    }
                }
            }
            return POSITION_UNCHANGED;
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
//得到缓存的fragment
            Fragment fragment = (Fragment) super.instantiateItem(container,
                    position);
//得到tag，这点很重要
            String fragmentTag = fragment.getTag();


            if (fragmentsUpdateFlag.size() > 0 && fragmentsUpdateFlag.get(position % fragmentsUpdateFlag.size())) {
//如果这个fragment需要更新

                FragmentTransaction ft = getChildFragmentManager().beginTransaction();
//移除旧的fragment
                ft.remove(fragment);
//换成新的fragment
                fragment = getItem(position);
//添加新fragment时必须用前面获得的tag，这点很重要
                ft.add(container.getId(), fragment, fragmentTag);
                ft.attach(fragment);
                ft.commit();

//复位更新标志
                fragmentsUpdateFlag.set(position % fragmentsUpdateFlag.size(), false);
            }

            return fragment;
        }

        @Override
        public int getCount() {
            return headlineCateItems.size();
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return headlineCateItems.get(position).getTitle();
        }
    }

    @OnClick(R.id.microInteract)
    public void openMicroInteraction(View bt) {
        Intent intent = new Intent().setClass(getContext(), WebViewActivity.class);
        intent.putExtra(WebViewActivity.URL, AppStore.MICRO_INTERACTION_URL + AppStore.getToken(getContext()));
        startActivity(intent);
    }

    @OnClick(R.id.subwaySearch)
    public void searchSubway(View bt) {
        Intent intent = new Intent().setClass(getContext(), WebViewActivity.class);
        intent.putExtra(WebViewActivity.URL, AppStore.SUBWAY_SEARCH_URL);
        startActivity(intent);
    }
}

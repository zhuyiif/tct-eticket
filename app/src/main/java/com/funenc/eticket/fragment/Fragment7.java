package com.funenc.eticket.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ScrollView;

import com.funenc.eticket.ListViewAdapter;
import com.funenc.eticket.R;
import com.funenc.eticket.Utility;

public class Fragment7 extends FragmentTabBase {
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return LayoutInflater.from(getActivity()).inflate(R.layout.fragment7, container, false);
    }


    @Override
    public void onStart() {
        super.onStart();

        listView = getActivity().findViewById(R.id.listView7);
        listView.setAdapter(new ListViewAdapter(getContext(), readListFromFile()));

        View multiTabLayout =  getActivity().findViewById(R.id.tabrootlayout);

        Utility.setListViewHeightBasedOnChildren(listView, multiTabLayout);


    }
}
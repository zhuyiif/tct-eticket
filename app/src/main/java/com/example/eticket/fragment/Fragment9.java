package com.example.eticket.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.eticket.ListViewAdapter;
import com.example.eticket.R;
import com.example.eticket.Utility;

public class Fragment9 extends FragmentTabBase {
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return LayoutInflater.from(getActivity()).inflate(R.layout.fragment9, container, false);
    }

    @Override
    public void onStart() {
        super.onStart();

        listView = getActivity().findViewById(R.id.listView9);
        listView.setAdapter(new ListViewAdapter(getContext(), readListFromFile()));

        View multiTabLayout =  getActivity().findViewById(R.id.tabrootlayout);

        Utility.setListViewHeightBasedOnChildren(listView, multiTabLayout);


    }
}

package com.funenc.eticket.ui.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.funenc.eticket.R;
import com.funenc.eticket.model.FelicityListResponse;
import com.funenc.eticket.util.StringUtils;
import com.squareup.picasso.Picasso;

import java.util.List;

public class FelicityListAdapter extends BaseAdapter {
    private Context context;
    private List<FelicityListResponse.Felicity> felicityList;

    public FelicityListAdapter(Context context, List<FelicityListResponse.Felicity> felicityList) {
        this.context = context;
        this.felicityList = felicityList;
    }

    @Override
    public int getCount() {
        return felicityList.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return felicityList.get(position).getId();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        convertView = LayoutInflater.from(context).inflate(R.layout.layout_felicity_in_main, null);
        ImageView cover = convertView.findViewById(R.id.cover);
        TextView title = convertView.findViewById(R.id.title);
        TextView intro = convertView.findViewById(R.id.intro);

        FelicityListResponse.Felicity felicity = felicityList.get(position);
        if(!StringUtils.isBlank(felicity.getCover())) {
            Picasso.get().load(felicity.getCover()).into(cover);
        }
        if(!StringUtils.isBlank(felicity.getTitle())) {
            title.setText(felicity.getTitle());
        }
        if(!StringUtils.isBlank(felicity.getIntro())) {
            intro.setText(felicity.getIntro());
        }
        return convertView;
    }
}

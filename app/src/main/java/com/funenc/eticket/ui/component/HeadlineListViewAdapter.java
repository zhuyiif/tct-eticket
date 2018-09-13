package com.funenc.eticket.ui.component;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.funenc.eticket.Person;
import com.funenc.eticket.R;
import com.funenc.eticket.model.HeadlineListResponse;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by tingken.com on 2018-08-19.
 */

public class HeadlineListViewAdapter extends BaseAdapter {

    private Context context;
    private List<HeadlineListResponse.Headline> headlineList;

    public HeadlineListViewAdapter(Context context, List<HeadlineListResponse.Headline> headlineList){
        this.context = context;
        this.headlineList = headlineList;
    }

    public HeadlineListViewAdapter(Context context) {
        this.context = context;
        this.headlineList = new ArrayList<HeadlineListResponse.Headline>();
    }

    @Override
    public int getCount() {
        return headlineList.size();
    }

    @Override
    public Object getItem(int position) {
        return headlineList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        if(convertView == null){
            convertView = LayoutInflater.from(context).inflate(R.layout.row_listview, null);

            viewHolder = new ViewHolder();

            viewHolder.imageViewProfilePic = convertView.findViewById(R.id.imageViewProfilePic);
            viewHolder.textViewName = convertView.findViewById(R.id.textViewName);

            convertView.setTag(viewHolder);
        }else{
            viewHolder = (ViewHolder)convertView.getTag();
        }
        HeadlineListResponse.Headline headline = headlineList.get(position);

        viewHolder.textViewName.setText(headline.getTitle());
        Picasso.get().load(headline.getCover()).into(viewHolder.imageViewProfilePic);

        return convertView;
    }

    private Drawable getImageDrawable(String imageName){
        int id  = context.getResources().getIdentifier(imageName, "drawable",
                context.getPackageName());
        return context.getResources().getDrawable(id);
    }

    class ViewHolder{
        ImageView imageViewProfilePic;
        TextView textViewName;
        TextView textViewDescription;
    }

    public void appendHeadlineList(List<HeadlineListResponse.Headline> headlineList){
        if(headlineList != null && !headlineList.isEmpty()){
            this.headlineList.addAll(headlineList);
        }
    }

    public void clearData(){
        headlineList.clear();
    }
}
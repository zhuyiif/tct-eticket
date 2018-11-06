package com.funenc.eticket.ui.adapter;

import android.content.Context;
import android.content.DialogInterface;
import android.graphics.drawable.Drawable;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.funenc.eticket.R;
import com.funenc.eticket.model.MessageListResponse;
import com.funenc.eticket.model.StationListResponse;
import com.squareup.picasso.Picasso;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by tingken.com on 2018-11-2.
 */

public class StationListViewAdapter extends BaseAdapter {

    private Context context;
    private List<StationListResponse.Station> stationList;

    public StationListViewAdapter(Context context, List<StationListResponse.Station> stationList){
        this.context = context;
        this.stationList = stationList;
    }

    public StationListViewAdapter(Context context) {
        this.context = context;
        this.stationList = new ArrayList<StationListResponse.Station>();
    }

    @Override
    public int getCount() {
        return stationList.size();
    }

    @Override
    public Object getItem(int position) {
        return stationList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        if(convertView == null){
            viewHolder = new ViewHolder();

            convertView = LayoutInflater.from(context).inflate(R.layout.layout_station_list_item, null);
            viewHolder.name = convertView.findViewById(R.id.name);

            convertView.setTag(viewHolder);
        }else{
            viewHolder = (ViewHolder)convertView.getTag();
        }
        final StationListResponse.Station station = stationList.get(position);

        viewHolder.name.setText(station.getcStationName());

//        convertView.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                View view=LayoutInflater.from(context).inflate(R.layout.layout_confirm_out_station, null);
//                AlertDialog.Builder builder = new AlertDialog.Builder(context,3);
//                builder.setView(view);
//                ((TextView)view.findViewById(R.id.alertTitle)).setText(station.getcStationName());
////                builder.setMessage("请确认您的出站口");
//                DialogInterface.OnClickListener dialogOnclicListener = new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialog, int which) {
//                        //
//                    }
//                };
////                builder.setNegativeButton("取消", dialogOnclicListener);
////                builder.setNeutralButton("确认支付", dialogOnclicListener);
//                final AlertDialog alertDialog = builder.create();
//                alertDialog.show();
//                view.findViewById(R.id.button1).setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//                        alertDialog.dismiss();
//                    }
//                });
//                view.findViewById(R.id.button2).setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//                        View view=LayoutInflater.from(context).inflate(R.layout.layout_confirm_money, null);
//                        AlertDialog.Builder builder = new AlertDialog.Builder(context,3);
//                        builder.setView(view);
//                        final AlertDialog alertDialog = builder.create();
//                        alertDialog.show();
//                    }
//                });
//            }
//        });
        return convertView;
    }

    class ViewHolder{
        TextView name;
    }

    public void appendStationList(List<StationListResponse.Station> stationList){
        if(stationList != null && !stationList.isEmpty()){
            this.stationList.addAll(stationList);
        }
    }

    public void clearData(){
        stationList.clear();
    }
}

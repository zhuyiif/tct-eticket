package com.funenc.eticket.ui.adapter;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.funenc.eticket.R;
import com.funenc.eticket.model.MessageListResponse;
import com.squareup.picasso.Picasso;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by tingken.com on 2018-10-9.
 */

public class MessageListViewAdapter extends BaseAdapter {

    private Context context;
    private List<MessageListResponse.Message> messageList;
    private boolean ofMessage;

    public MessageListViewAdapter(Context context, List<MessageListResponse.Message> messageList, boolean ofMessage){
        this.context = context;
        this.messageList = messageList;
        this.ofMessage = ofMessage;
    }

    public MessageListViewAdapter(Context context, boolean ofMessage) {
        this.context = context;
        this.messageList = new ArrayList<MessageListResponse.Message>();
        this.ofMessage = ofMessage;
    }

    @Override
    public int getCount() {
        return messageList.size();
    }

    @Override
    public Object getItem(int position) {
        return messageList.get(position);
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

            if(ofMessage){
                convertView = LayoutInflater.from(context).inflate(R.layout.layout_message_list_item, null);
                viewHolder.avatar = convertView.findViewById(R.id.avatar);
                viewHolder.cover = convertView.findViewById(R.id.cover);
            }else {
                convertView = LayoutInflater.from(context).inflate(R.layout.layout_notice_list_item, null);
            }

            viewHolder.title = convertView.findViewById(R.id.title);
            viewHolder.content = convertView.findViewById(R.id.content);
            viewHolder.date = convertView.findViewById(R.id.date);

            convertView.setTag(viewHolder);
        }else{
            viewHolder = (ViewHolder)convertView.getTag();
        }
        MessageListResponse.Message message = messageList.get(position);

        viewHolder.title.setText(message.getTitle());
        viewHolder.content.setText(message.getContent());

        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        viewHolder.date.setText(dateFormat.format(message.getCreatedAt()));
        if(ofMessage){
            Picasso.get().load(R.drawable.image6).into(viewHolder.avatar);
            Picasso.get().load(R.drawable.right_ad).into(viewHolder.cover);
        }

        return convertView;
    }

    private Drawable getImageDrawable(String imageName){
        int id  = context.getResources().getIdentifier(imageName, "drawable",
                context.getPackageName());
        return context.getResources().getDrawable(id);
    }

    class ViewHolder{
        TextView title;
        TextView content;
        TextView date;
        ImageView avatar;
        ImageView cover;
    }

    public void appendMessageList(List<MessageListResponse.Message> messageList){
        if(messageList != null && !messageList.isEmpty()){
            this.messageList.addAll(messageList);
        }
    }

    public void clearData(){
        messageList.clear();
    }
}

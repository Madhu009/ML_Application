package com.example.madhu.ml_application.ChatBot;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.madhu.ml_application.R;
import com.example.madhu.ml_application.Shopping.Product;
import com.example.madhu.ml_application.Shopping.ShoppingCartHelper;

import java.util.List;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;


public class ChatAdapter  extends BaseAdapter {

    private List<Conversation> conversations;
    private LayoutInflater mInflater;


    public ChatAdapter(List<Conversation> list, LayoutInflater inflater) {
        conversations = list;
        mInflater = inflater;

    }

    @Override
    public int getCount() {
        return conversations.size();
    }

    @Override
    public Object getItem(int position) {
        return conversations.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final ViewItem item;
        Conversation conversation = conversations.get(position);
        if (!conversation.isImgSet())
        {
            if (conversation.isSent())
                convertView = mInflater.inflate(R.layout.chat_item_rcv, null);
            else
                convertView = mInflater.inflate(R.layout.chat_item_sent, null);


            item = new ViewItem();

            item.time = (TextView) convertView
                    .findViewById(R.id.time);

            item.message = (TextView) convertView
                    .findViewById(R.id.msg);

            item.st = (TextView) convertView
                    .findViewById(R.id.st);

            convertView.setTag(item);

        /*if (convertView == null) {
            convertView = mInflater.inflate(R.layout.item, null);
            item = new ViewItem();

            item.productImageView = (ImageView) convertView
                    .findViewById(R.id.ImageViewItem);

            item.productTitle = (TextView) convertView
                    .findViewById(R.id.TextViewItem);

            item.productQuantity = (TextView) convertView
                    .findViewById(R.id.textViewQuantity);

            convertView.setTag(item);
        } else {
            item = (ViewItem) convertView.getTag();
        }
*/


            item.time.setText(conversation.getDate().toString());
            item.message.setText(conversation.getMsg());
            item.st.setText(conversation.getStatusSending());
            return convertView;

        }
        else if(conversation.isSent())
            convertView = mInflater.inflate(R.layout.chat_item_rcv_image, null);
        else
            convertView = mInflater.inflate(R.layout.chat_item_sent_image, null);

        item = new ViewItem();
        item.img = (ImageView) convertView
                .findViewById(R.id.imageViewtrue);

        convertView.setTag(item);
        item.img.setImageBitmap(conversation.getBitmapImage());
        item.st.setText(conversation.getStatusSending());

        return convertView;
    }


    private class ViewItem {
        TextView st;
        TextView time;
        TextView message;
        ImageView img;
    }

}


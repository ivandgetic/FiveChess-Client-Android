package org.ivandgetic.fivechess.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import org.ivandgetic.fivechess.MainActivity;
import org.ivandgetic.fivechess.MyService;
import org.ivandgetic.fivechess.R;
import org.ivandgetic.fivechess.models.Message;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ivandgetic on 2016/3/18 0018.
 */
public class MessageAdapter extends BaseAdapter {
    public static List<Message> messageList = new ArrayList<Message>();
    public Context context;

    public MessageAdapter(Context context) {
        this.context = context;
    }

    @Override
    public int getCount() {
        return messageList.size();
    }

    @Override
    public Message getItem(int position) {
        return messageList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Message messageItem = getItem(position);
        if (messageItem.getName().equals(MyService.sharedPreferences.getString("name", ""))) {
            convertView = LayoutInflater.from(context).inflate(R.layout.message_right, null);
        } else {
            convertView = LayoutInflater.from(context).inflate(R.layout.message_left, null);
        }
        TextView messageTextView = (TextView) convertView.findViewById(R.id.textView);
        messageTextView.setText(messageItem.getName() + ": " + messageItem.getMessage());
        return convertView;
    }
}

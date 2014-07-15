package org.ivandgetic.fivechess;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by ivandgetic on 14/7/10.
 */
public class UserAdapter extends BaseAdapter {
    public static List<User> userList = new ArrayList<User>();
    public Context context;

    public UserAdapter(Context context) {
        this.context = context;
    }

    public Context getContext() {
        return context;
    }

    @Override
    public int getCount() {
        return userList.size();
    }

    @Override
    public User getItem(int i) {
        return userList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        final User userItem = getItem(i);
        view = LayoutInflater.from(getContext()).inflate(R.layout.list_user, null);
        TextView name = (TextView) view.findViewById(R.id.textView_name);
        TextView state = (TextView) view.findViewById(R.id.textView_state);
        TextView remoteIP = (TextView) view.findViewById(R.id.textView_remoteIP);
        final Button invite= (Button) view.findViewById(R.id.button_invite);
        name.setText(userItem.getName());
        //state.setText("State:" + userItem.getState());
        state.setText("");
        remoteIP.setText(context.getString(R.string.textview_from)+" " + userItem.getRemoteIP().substring(1));
        if (userItem.getName().equals(MyActivity.preferences.getString("name", ""))){
            invite.setEnabled(false);
        }else if (userItem.getState().equals("online")){
            invite.setEnabled(true);
        }else{
            invite.setEnabled(false);
        }
        invite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    MyService.getMyService().out.writeUTF("operate:state:"+MyActivity.preferences.getString("name","")+":"+"invite");
                    MyService.getMyService().out.writeUTF("operate:"+"invite:"+MyActivity.preferences.getString("name", "")+":"+userItem.getName());
                    GameConfig.setPartner(userItem.getName());
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
        return view;
    }
}

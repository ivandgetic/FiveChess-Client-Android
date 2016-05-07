package org.ivandgetic.fivechess;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ListView;

import org.ivandgetic.fivechess.adapters.MessageAdapter;
import org.ivandgetic.fivechess.models.GameConfig;

/**
 * Created by ivandgetic on 2016/5/7 0007.
 */
public class ChatFragment extends Fragment {

    public static ListView listView;
    public static MessageAdapter messageAdapter;
    private Context mContext;
    public static EditText compose_edit;

    public ChatFragment(Context context) {
        mContext = context;
    }

    public static ChatFragment newInstance(Context context) {
        ChatFragment fragment = new ChatFragment(context);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_chat, container, false);
        messageAdapter = new MessageAdapter(mContext);
        listView = (ListView) rootView.findViewById(R.id.listview_chat);
        listView.setAdapter(messageAdapter);
        compose_edit = (EditText) rootView.findViewById(R.id.compose_edit);
        return rootView;
    }

    @Override
    public void onResume() {
        GameConfig.inChat = true;
        super.onResume();
    }

    @Override
    public void onPause() {
        GameConfig.inChat = false;
        super.onPause();
    }
}
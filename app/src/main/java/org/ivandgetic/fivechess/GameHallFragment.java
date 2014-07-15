package org.ivandgetic.fivechess;

import android.app.Fragment;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.Toast;

import java.io.IOException;

/**
 * Created by ivandgetic on 14/7/9.
 */
public class GameHallFragment extends Fragment {
    public static SwipeRefreshLayout swipeRefreshLayout;
    public static ListView listView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_game_hall, container, false);
        swipeRefreshLayout = (SwipeRefreshLayout) rootView.findViewById(R.id.swipeRefreshLayout);
        swipeRefreshLayout.setColorScheme(android.R.color.holo_green_light,
                android.R.color.holo_red_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_blue_light);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                try {
                    MyService.getMyService().out.writeUTF("operate:getUserList:null");
                } catch (IOException e) {
                    e.printStackTrace();
                }catch (NullPointerException e){
                    Toast.makeText(MyService.getMyService(),"No connected",Toast.LENGTH_SHORT).show();
                    swipeRefreshLayout.setRefreshing(false);
                }
              }
        });
        listView = (ListView) rootView.findViewById(R.id.listview);
        listView.setAdapter(MyActivity.userAdapter);
        return rootView;
    }

    @Override
    public void onResume() {
        super.onResume();
        getActivity().getActionBar().setTitle(getString(R.string.app_name));
    }
}

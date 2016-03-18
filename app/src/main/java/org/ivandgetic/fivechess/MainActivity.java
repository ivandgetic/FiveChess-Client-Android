package org.ivandgetic.fivechess;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Point;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import org.ivandgetic.fivechess.settings.SettingsActivity;

import java.io.IOException;

/**
 * Created by ivandgetic on 14/7/9.
 */
public class MainActivity extends AppCompatActivity {
    public static SharedPreferences preferences;
    public static UserAdapter userAdapter;
    public static MainActivity mainActivity = null;
    public static SwipeRefreshLayout swipeRefreshLayout;
    public static ListView listView;
    public static MainActivity getMainActivity() {
        return mainActivity;
    }

    public MainActivity() {
        mainActivity = this;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Display display = getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);//获取屏幕宽度的像素
        GameConfig.setWIDTH(size.x);//设置宽度
        preferences = PreferenceManager.getDefaultSharedPreferences(this);
        if (!(preferences.getString("name", "").length() > 0)) {//如果用户名为空
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("Sign in");
            LayoutInflater inflater = this.getLayoutInflater();
            final View layout = inflater.inflate(R.layout.dialog_signin, null);
            builder.setView(layout);
            builder.setCancelable(false);
            builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    preferences.edit().putString("name",  ((EditText) layout.findViewById(R.id.username)).getText().toString()).apply();
                    startService(new Intent(MainActivity.this, MyService.class));
                    initView();
                }
            });
            builder.show();
        } else {
            startService(new Intent(MainActivity.this, MyService.class));
            initView();
        }
    }

    private void initView(){
        userAdapter = new UserAdapter(this);
        swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipeRefreshLayout);
//        swipeRefreshLayout.setColorScheme(android.R.color.holo_green_light,
//                android.R.color.holo_red_light,
//                android.R.color.holo_orange_light,
//                android.R.color.holo_blue_light);
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
        listView = (ListView) findViewById(R.id.listview);
        listView.setAdapter(userAdapter);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            startActivity(new Intent(this, SettingsActivity.class));
            return true;
        } else if (id == R.id.action_exit) {
            stopService(new Intent(MainActivity.this, MyService.class));
            System.exit(0);
            return true;
        } else if (id == R.id.action_reconnect) {
            UserAdapter.userList.clear();
            try {
                if (MyService.getMyService().out != null) {
                    MyService.getMyService().out.close();
                }
                if (MyService.getMyService().in != null) {
                    MyService.getMyService().in.close();
                }
                if (MyService.getMyService().socket != null) {
                    MyService.getMyService().socket.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            stopService(new Intent(MainActivity.this, MyService.class));
            startService(new Intent(MainActivity.this, MyService.class));
        }
        return super.onOptionsItemSelected(item);
    }
}

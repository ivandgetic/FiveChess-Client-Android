package org.ivandgetic.fivechess;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Point;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.Display;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;
import android.widget.Toast;

import org.ivandgetic.fivechess.adapters.UserAdapter;
import org.ivandgetic.fivechess.models.GameConfig;
import org.ivandgetic.fivechess.settings.SettingsActivity;

import java.io.IOException;

/**
 * Created by ivandgetic on 14/7/9.
 */
public class MainActivity extends AppCompatActivity {
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
        initView();
        if (MyService.socket == null) {
            stopService(new Intent(this, MyService.class));
            startService(new Intent(this, MyService.class));
        }
    }

    private void initView() {
        userAdapter = new UserAdapter(this);
        swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipeRefreshLayout);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                try {
                    MyService.dataOutputStream.writeUTF("operate:getUserList:null");
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (NullPointerException e) {
                    Toast.makeText(MyService.getMyService(), "No connected", Toast.LENGTH_SHORT).show();
                    swipeRefreshLayout.setRefreshing(false);
                }
            }
        });
        listView = (ListView) findViewById(R.id.listview);
        listView.setAdapter(userAdapter);
        Toast.makeText(this, "可下拉刷新获取玩家列表", Toast.LENGTH_SHORT).show();
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
            stopService(new Intent(this, MyService.class));
            System.exit(0);
            return true;
        } else if (id == R.id.action_logout) {
            UserAdapter.userList.clear();
            try {
                if (MyService.dataOutputStream != null) {
                    MyService.dataOutputStream.close();
                }
                if (MyService.dataInputStream != null) {
                    MyService.dataInputStream.close();
                }
                if (MyService.socket != null) {
                    MyService.socket.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            stopService(new Intent(this, MyService.class));
            startService(new Intent(this, MyService.class));
            startActivity(new Intent(this, LoginActivity.class));
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

}

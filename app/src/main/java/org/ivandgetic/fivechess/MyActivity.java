package org.ivandgetic.fivechess;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Point;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ListView;

import java.io.IOException;

/**
 * Created by ivandgetic on 14/7/9.
 */
public class MyActivity extends Activity {
    public static SharedPreferences preferences;
    public static UserAdapter userAdapter;
    public static MyActivity myActivity = null;
    public static MyActivity getMyActivity() {
        return myActivity;
    }

    public MyActivity() {
        myActivity = this;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my);
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
                    SharedPreferences.Editor e = preferences.edit();
                    String name = ((EditText) layout.findViewById(R.id.username)).getText().toString();
                    e.putString("name", name);
                    e.commit();
                    startService(new Intent(MyActivity.this, MyService.class));
                    getFragmentManager().beginTransaction().add(android.R.id.content, new GameHallFragment()).commit();
                }
            });
            builder.show();
        } else {
            startService(new Intent(MyActivity.this, MyService.class));
            getFragmentManager().beginTransaction().add(android.R.id.content, new GameHallFragment()).commit();
        }
        userAdapter = new UserAdapter(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.my, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            getFragmentManager().beginTransaction().replace(android.R.id.content, new SettingsFragment()).addToBackStack(null).commit();
            return true;
        } else if (id == R.id.action_exit) {
            stopService(new Intent(MyActivity.this, MyService.class));
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
            stopService(new Intent(MyActivity.this, MyService.class));
            startService(new Intent(MyActivity.this, MyService.class));
        }
        return super.onOptionsItemSelected(item);
    }
}

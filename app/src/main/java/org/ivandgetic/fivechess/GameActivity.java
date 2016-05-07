package org.ivandgetic.fivechess;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.support.v7.widget.Toolbar;

import org.ivandgetic.fivechess.adapters.MessageAdapter;
import org.ivandgetic.fivechess.adapters.SectionsPagerAdapter;
import org.ivandgetic.fivechess.models.GameConfig;
import org.ivandgetic.fivechess.models.Message;

import java.io.IOException;

/**
 * Created by ivandgetic on 2016/3/18 0018.
 */
public class GameActivity extends AppCompatActivity {
    public static String player1, player2;
    public static String colorMy;
    public static String colorPartner;

    public static SharedPreferences sharedPreferences;

    private SectionsPagerAdapter mSectionsPagerAdapter;
    private ViewPager mViewPager;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("对方用户名：" + GameConfig.getPartner());
        setSupportActionBar(toolbar);
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager(), this);

        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapter);
        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(mViewPager);
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        //init player

        player1 = MyService.sharedPreferences.getString("name", "");
        player2 = GameConfig.getPartner();
        //

        if (GameView.myColor == 1) {
            colorMy = getString(R.string.textview_black);
            colorPartner = getString(R.string.textview_white);
        } else if (GameView.myColor == 2) {
            colorMy = getString(R.string.textview_white);
            colorPartner = getString(R.string.textview_black);
        }

        GameView.chess = new int[GameConfig.getPOINT_NUM()][GameConfig.getPOINT_NUM()];
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        try {
            MyService.dataOutputStream.writeUTF("play:game:leave:" + sharedPreferences.getString("name", "") + ":" + GameConfig.partner);
            MyService.dataOutputStream.writeUTF("operate:state:" + sharedPreferences.getString("name", "") + ":" + "在线");
            GameConfig.partner = null;
            GameView.myColor = 0;
            GameView.turn = false;
            GameView.line = null;
            GameView.gameEnd = false;
            MessageAdapter.messageList.clear();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.game, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_clear) {
            MessageAdapter.messageList.clear();
            ChatFragment.messageAdapter.notifyDataSetChanged();
        } else if (id == R.id.action_exit) {
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
            System.exit(0);
        }
        return super.onOptionsItemSelected(item);
    }

    private String message;

    public void send(View view) {
        message = ChatFragment.compose_edit.getText().toString();
        if (message.length() == 0) {
            return;
        }
        try {
            MyService.dataOutputStream.writeUTF("Message:" + GameActivity.sharedPreferences.getString("name", "") + ":" + GameConfig.getPartner() + ":" + message);
            addMessage(GameActivity.sharedPreferences.getString("name", ""), message);
        } catch (NullPointerException e) {
            Toast.makeText(this, R.string.no_connect, Toast.LENGTH_SHORT).show();
        } catch (IOException e) {
            e.printStackTrace();
        }
        ChatFragment.compose_edit.setText("");
    }

    public static void addMessage(String stringName, String stringMessage) {
        MessageAdapter.messageList.add(new Message(stringName, stringMessage));
        ChatFragment.messageAdapter.notifyDataSetChanged();
        ChatFragment.listView.setSelection(ChatFragment.messageAdapter.getCount());
    }
}

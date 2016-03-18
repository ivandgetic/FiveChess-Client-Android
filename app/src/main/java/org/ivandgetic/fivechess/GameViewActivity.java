package org.ivandgetic.fivechess;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.ViewGroup;
import android.widget.TextView;

import java.io.IOException;

/**
 * Created by ivandgetic on 2016/3/18 0018.
 */
public class GameViewActivity extends AppCompatActivity {
    private GameView gameview;
    private TextView textViewPlayer1, textViewPlayer2;
    private String player1, player2;
    private String colorMy;
    private String colorPartner;
    public static TextView textViewPlayer1Turn, textViewPlayer2Turn;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_view);
        //init player
        player1 = MainActivity.preferences.getString("name", "");
        player2 = GameConfig.getPartner();
        //
        gameview = (GameView) findViewById(R.id.gameview);
        ViewGroup.LayoutParams layoutParams = gameview.getLayoutParams();
        layoutParams.height = GameConfig.getWIDTH();
        gameview.setLayoutParams(layoutParams);
        if (GameView.myColor == 1) {
            colorMy = getString(R.string.textview_black);
            colorPartner = getString(R.string.textview_white);
        } else if (GameView.myColor == 2) {
            colorMy = getString(R.string.textview_white);
            colorPartner = getString(R.string.textview_black);
        }
        textViewPlayer1 = (TextView) findViewById(R.id.textView_play1);
        textViewPlayer2 = (TextView) findViewById(R.id.textView_play2);
        textViewPlayer1.setText(getString(R.string.textview_player1) + colorMy + player1);
        textViewPlayer2.setText(getString(R.string.textview_player2) + colorPartner + player2);
        textViewPlayer1Turn = (TextView) findViewById(R.id.textView_play1_turn);
        textViewPlayer2Turn = (TextView) findViewById(R.id.textView_play2_turn);
        if (!GameView.gameEnd) {
            textViewPlayer1Turn.post(new Runnable() {
                @Override
                public void run() {
                    if (GameView.turn) {
                        textViewPlayer1Turn.setText(getString(R.string.textview_myturn));
                    } else {
                        textViewPlayer1Turn.setText("");
                    }
                }
            });
            textViewPlayer2Turn.post(new Runnable() {
                @Override
                public void run() {
                    if (GameView.turn) {
                        textViewPlayer2Turn.setText("");
                    } else {
                        textViewPlayer2Turn.setText(getString(R.string.textview_partnerturn));

                    }
                }
            });
        }
        GameView.chess = new int[GameConfig.getPOINT_NUM()][GameConfig.getPOINT_NUM()];
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        try {
            MyService.getMyService().out.writeUTF("play:game:leave:" + MainActivity.preferences.getString("name", "") + ":" + GameConfig.partner);
            MyService.getMyService().out.writeUTF("operate:state:" + MainActivity.preferences.getString("name", "") + ":" + "online");
            GameConfig.partner = null;
            GameView.myColor = 0;
            GameView.turn = false;
            GameView.line = null;
            GameView.gameEnd = false;
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

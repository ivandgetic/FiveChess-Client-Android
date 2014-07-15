package org.ivandgetic.fivechess;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.io.IOException;

/**
 * Created by ivandgetic on 14/7/9.
 */
public class GameViewFragment extends Fragment {
    GameView gameview;
    TextView textViewPlayer1, textViewPlayer2;
    static TextView textViewPlayer1Turn, textViewPlayer2Turn;
    String player1, player2;
    String colorMy;
    String colorPartner;

    public GameViewFragment(String playerFrom, String playerTo) {
        player1 = playerFrom;
        player2 = playerTo;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_game_view, container, false);
        gameview = (GameView) rootView.findViewById(R.id.gameview);
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
        textViewPlayer1 = (TextView) rootView.findViewById(R.id.textView_play1);
        textViewPlayer2 = (TextView) rootView.findViewById(R.id.textView_play2);
        textViewPlayer1.setText(getString(R.string.textview_player1) + colorMy + player1);
        textViewPlayer2.setText(getString(R.string.textview_player2) + colorPartner + player2);
        textViewPlayer1Turn = (TextView) rootView.findViewById(R.id.textView_play1_turn);
        textViewPlayer2Turn = (TextView) rootView.findViewById(R.id.textView_play2_turn);
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
        return rootView;
    }

    @Override
    public void onResume() {
        super.onResume();
        getActivity().getActionBar().setTitle(getString(R.string.app_name));
    }

    @Override
    public void onDetach() {
        super.onDetach();
        try {
            MyService.getMyService().out.writeUTF("play:game:leave:" + MyActivity.preferences.getString("name", "") + ":" + GameConfig.partner);
            MyService.getMyService().out.writeUTF("operate:state:" + MyActivity.preferences.getString("name", "") + ":" + "online");
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

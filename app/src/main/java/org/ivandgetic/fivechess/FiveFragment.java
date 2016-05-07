package org.ivandgetic.fivechess;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import org.ivandgetic.fivechess.models.GameConfig;

/**
 * Created by ivandgetic on 2016/5/7 0007.
 */
public class FiveFragment extends Fragment {

    private GameView gameview;
    private Context mContext;
    public static TextView textViewPlayer1Turn, textViewPlayer2Turn;
    private TextView textViewPlayer1, textViewPlayer2;

    public FiveFragment(Context context) {
        mContext = context;
    }

    public static FiveFragment newInstance(Context context) {
        FiveFragment fragment = new FiveFragment(context);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_five, container, false);
        gameview = (GameView) rootView.findViewById(R.id.gameview);
        ViewGroup.LayoutParams layoutParams = gameview.getLayoutParams();
        layoutParams.height = GameConfig.getWIDTH();
        gameview.setLayoutParams(layoutParams);
        textViewPlayer1 = (TextView) rootView.findViewById(R.id.textView_play1);
        textViewPlayer2 = (TextView) rootView.findViewById(R.id.textView_play2);
        textViewPlayer1.setText(getString(R.string.textview_player1) + GameActivity.colorMy + GameActivity.player1);
        textViewPlayer2.setText(getString(R.string.textview_player2) + GameActivity.colorPartner + GameActivity.player2);
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
        return rootView;
    }
}
package org.ivandgetic.fivechess;

import android.app.AlertDialog;
import android.app.Service;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.IBinder;
import android.preference.PreferenceManager;
import android.widget.Toast;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;

/**
 * Created by ivandgetic on 14/7/9.
 */
public class MyService extends Service {
    private static final int SOCKET_PORT = 50000;
    public static String SOCKET_ADDRESS = "192.168.1.210";//初始化
    public static MyService myService;
    public Socket socket;
    public GameViewFragment gameViewFragment;
    DataInputStream in;
    DataOutputStream out;
    SharedPreferences preferences;
    String player1, player2;


    public MyService() {
        myService = this;
    }

    public static MyService getMyService() {
        return myService;
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public void onCreate() {
        super.onCreate();
        preferences = PreferenceManager.getDefaultSharedPreferences(this);
        SOCKET_ADDRESS = preferences.getString("server_address", "");
        Thread work = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    socket = new Socket(SOCKET_ADDRESS, SOCKET_PORT);
                    in = new DataInputStream(socket.getInputStream());
                    out = new DataOutputStream(socket.getOutputStream());
                    out.writeUTF("information:" + preferences.getString("name", ""));
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                while (true) {
                                    final String line = in.readUTF();
                                    GameHallFragment.listView.post(new Runnable() {
                                        @Override
                                        public void run() {
                                            final String[] separate = line.split(":", 7);
                                            if (separate[0].equals("information")) {
                                                GameHallFragment.swipeRefreshLayout.setRefreshing(false);
                                                UserAdapter.userList.add(new User(separate[1], separate[2], separate[3]));
                                                MyActivity.userAdapter.notifyDataSetChanged();
                                            }
                                            if (separate[0].equals("operate")) {
                                                if (separate[1].equals("clear")) {
                                                    UserAdapter.userList.clear();
                                                }
                                                if (separate[1].equals("invitefrom")) {
                                                    GameConfig.setPartner(separate[2]);
                                                    new AlertDialog.Builder(MyActivity.getMyActivity()).setTitle(getString(R.string.invite_from) + separate[2]).setPositiveButton(getString(R.string.button_agree), new DialogInterface.OnClickListener() {
                                                        @Override
                                                        public void onClick(DialogInterface dialogInterface, int i) {
                                                            try {
                                                                out.writeUTF("operate:agree:" + separate[2] + ":" + MyActivity.preferences.getString("name", ""));
                                                            } catch (IOException e) {
                                                                e.printStackTrace();
                                                            }
                                                        }
                                                    }).setNegativeButton(getString(R.string.button_disagree), new DialogInterface.OnClickListener() {
                                                        @Override
                                                        public void onClick(DialogInterface dialogInterface, int i) {
                                                            try {
                                                                out.writeUTF("operate:disagree:" + separate[2] + ":" + MyActivity.preferences.getString("name", ""));
                                                            } catch (IOException e) {
                                                                e.printStackTrace();
                                                            }
                                                        }
                                                    }).setCancelable(false).show();
                                                }
                                                if (separate[1].equals("agree")) {
                                                    gameViewFragment = new GameViewFragment(MyActivity.preferences.getString("name", ""), GameConfig.getPartner());
                                                    GameView.myColor = Integer.parseInt(separate[4]);
                                                    if (separate[5].equals("true")) {
                                                        GameView.turn = true;
                                                    }
                                                    MyActivity.getMyActivity().getFragmentManager().beginTransaction().add(android.R.id.content, gameViewFragment).addToBackStack(null).commit();

                                                }
                                                if (separate[1].equals("disagree")) {
                                                    Toast.makeText(MyActivity.getMyActivity(), separate[3] + getString(R.string.toast_disagree_invite), Toast.LENGTH_SHORT).show();
                                                }
                                            }
                                            if (separate[0].equals("play")) {
                                                if (separate[1].equals("place")) {
                                                    GameView.chess[Integer.parseInt(separate[2])][Integer.parseInt(separate[3])] = Integer.parseInt(separate[4]);
                                                    GameView.lastx = separate[2];
                                                    GameView.lasty = separate[3];
                                                    GameView.getGameView().draw();
                                                    GameView.check();
                                                }
                                                if (separate[1].equals("turn")) {
                                                    if (separate[2].equals("true")) {
                                                        GameView.turn = true;
                                                        if (!GameView.gameEnd) {
                                                            if (GameViewFragment.textViewPlayer1Turn != null) {
                                                                GameViewFragment.textViewPlayer1Turn.post(new Runnable() {
                                                                    @Override
                                                                    public void run() {
                                                                        GameViewFragment.textViewPlayer1Turn.setText(getString(R.string.textview_myturn));
                                                                    }
                                                                });
                                                            }
                                                            if (GameViewFragment.textViewPlayer2Turn != null) {
                                                                GameViewFragment.textViewPlayer2Turn.post(new Runnable() {
                                                                    @Override
                                                                    public void run() {
                                                                        GameViewFragment.textViewPlayer2Turn.setText("");
                                                                    }
                                                                });
                                                            }
                                                        }
                                                    }
                                                }
                                                if (separate[1].equals("game")) {
                                                    if (separate[2].equals("leave")) {
                                                        Toast.makeText(MyActivity.getMyActivity(), separate[3] + " " + getString(R.string.toast_has_left_game), Toast.LENGTH_LONG).show();
                                                        if (!GameView.gameEnd) {
                                                            if (GameViewFragment.textViewPlayer1Turn != null) {
                                                                GameViewFragment.textViewPlayer1Turn.post(new Runnable() {
                                                                    @Override
                                                                    public void run() {
                                                                        GameViewFragment.textViewPlayer1Turn.setText("");
                                                                    }
                                                                });
                                                            }
                                                            if (GameViewFragment.textViewPlayer2Turn != null) {
                                                                GameViewFragment.textViewPlayer2Turn.post(new Runnable() {
                                                                    @Override
                                                                    public void run() {
                                                                        GameViewFragment.textViewPlayer2Turn.setText(getString(R.string.toast_has_left_game));
                                                                    }
                                                                });
                                                            }
                                                        }
                                                    }
                                                }
                                            }
                                        }
                                    });
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    }).start();
                } catch (UnknownHostException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
        work.start();
    }

}

package org.ivandgetic.fivechess;

import android.app.AlertDialog;
import android.app.Service;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.IBinder;
import android.preference.PreferenceManager;
import android.widget.Toast;

import org.ivandgetic.fivechess.adapters.SectionsPagerAdapter;
import org.ivandgetic.fivechess.adapters.UserAdapter;
import org.ivandgetic.fivechess.models.GameConfig;
import org.ivandgetic.fivechess.models.User;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.EOFException;
import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;

/**
 * Created by ivandgetic on 14/7/9.
 */
public class MyService extends Service {
    private static final int SOCKET_PORT = 50000;
    public static String SOCKET_ADDRESS = "192.168.31.200";//初始化
    public static MyService myService;
    public static Socket socket;
    public static DataInputStream dataInputStream;
    public static DataOutputStream dataOutputStream;
    public static SharedPreferences sharedPreferences;


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
    public int onStartCommand(Intent intent, int flags, final int startId) {
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        SOCKET_ADDRESS = sharedPreferences.getString("server_address", "");
        Thread work = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    socket = new Socket(SOCKET_ADDRESS, SOCKET_PORT);
                    dataInputStream = new DataInputStream(socket.getInputStream());
                    dataOutputStream = new DataOutputStream(socket.getOutputStream());
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                while (true) {
                                    final String line = dataInputStream.readUTF();
                                    System.out.println("收到的消息:" + line);
                                    if (line.startsWith("Login")) {
                                        LoginActivity.attemptLoginButton.post(new Runnable() {
                                            @Override
                                            public void run() {
                                                if (line.contains("right")) {
                                                    System.out.println("登录成功");
                                                    Toast.makeText(MyService.this, "登录成功", Toast.LENGTH_SHORT).show();
                                                    startActivity(new Intent(MyService.this, MainActivity.class).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
                                                    LoginActivity.getLoginActivity().finish();
                                                } else if (line.contains("wrong")) {
                                                    System.out.println("账号或密码错误");
                                                    Toast.makeText(MyService.this, "账号或密码错误", Toast.LENGTH_SHORT).show();
                                                }
                                            }
                                        });
                                    }
                                    if (line.startsWith("Register")) {
                                        LoginActivity.attemptLoginButton.post(new Runnable() {
                                            @Override
                                            public void run() {
                                                if (line.contains("right")) {
                                                    System.out.println("注册成功");
                                                    Toast.makeText(MyService.this, "注册成功，即将登录", Toast.LENGTH_SHORT).show();
                                                    startActivity(new Intent(MyService.this, MainActivity.class).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
                                                    LoginActivity.getLoginActivity().finish();
                                                } else if (line.contains("wrong")) {
                                                    System.out.println("用户名已存在");
                                                    Toast.makeText(MyService.this, "用户名已存在", Toast.LENGTH_SHORT).show();
                                                }
                                            }
                                        });
                                    }
                                    if (MainActivity.listView != null) {
                                        MainActivity.listView.post(new Runnable() {
                                            @Override
                                            public void run() {
                                                final String[] separate = line.split(":", 7);
                                                if (separate[0].equals("information")) {
                                                    MainActivity.swipeRefreshLayout.setRefreshing(false);
                                                    if (!separate[1].equals(sharedPreferences.getString("name", ""))) {
                                                        UserAdapter.userList.add(new User(separate[1], separate[2], separate[3]));
                                                    }
                                                    MainActivity.userAdapter.notifyDataSetChanged();
                                                } else if (separate[0].equals("operate")) {
                                                    if (separate[1].equals("clear")) {
                                                        UserAdapter.userList.clear();
                                                    } else if (separate[1].equals("invitefrom")) {
                                                        GameConfig.setPartner(separate[2]);
                                                        new AlertDialog.Builder(MainActivity.getMainActivity()).setTitle(getString(R.string.invite_from) + separate[2]).setPositiveButton(getString(R.string.button_agree), new DialogInterface.OnClickListener() {
                                                            @Override
                                                            public void onClick(DialogInterface dialogInterface, int i) {
                                                                try {
                                                                    dataOutputStream.writeUTF("operate:agree:" + separate[2] + ":" + MyService.sharedPreferences.getString("name", ""));
                                                                } catch (IOException e) {
                                                                    e.printStackTrace();
                                                                }
                                                            }
                                                        }).setNegativeButton(getString(R.string.button_disagree), new DialogInterface.OnClickListener() {
                                                            @Override
                                                            public void onClick(DialogInterface dialogInterface, int i) {
                                                                try {
                                                                    dataOutputStream.writeUTF("operate:disagree:" + separate[2] + ":" + MyService.sharedPreferences.getString("name", ""));
                                                                } catch (IOException e) {
                                                                    e.printStackTrace();
                                                                }
                                                            }
                                                        }).setCancelable(false).show();
                                                    } else if (separate[1].equals("agree")) {
                                                        //设置对手名字
                                                        if (sharedPreferences.getString("name", "").equals(separate[3])){
                                                            GameConfig.setPartner(separate[2]);
                                                        }else {
                                                            GameConfig.setPartner(separate[3]);
                                                        }
                                                        GameView.myColor = Integer.parseInt(separate[4]);
                                                        if (separate[5].equals("true")) {
                                                            GameView.turn = true;
                                                        }
                                                        Intent intent = new Intent(MainActivity.getMainActivity(), GameActivity.class);
                                                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                                        startActivity(intent);
                                                    } else if (separate[1].equals("disagree")) {
                                                        Toast.makeText(MainActivity.getMainActivity(), separate[3] + getString(R.string.toast_disagree_invite), Toast.LENGTH_SHORT).show();
                                                    }
                                                } else if (separate[0].equals("play")) {
                                                    if (separate[1].equals("place")) {
                                                        GameView.chess[Integer.parseInt(separate[2])][Integer.parseInt(separate[3])] = Integer.parseInt(separate[4]);
                                                        GameView.lastx = separate[2];
                                                        GameView.lasty = separate[3];
                                                        GameView.check();
                                                        GameView.getGameView().draw();
                                                    } else if (separate[1].equals("turn")) {
                                                        if (separate[2].equals("true")) {
                                                            GameView.turn = true;
                                                            if (!GameView.gameEnd) {
                                                                if (FiveFragment.textViewPlayer1Turn != null) {
                                                                    FiveFragment.textViewPlayer1Turn.post(new Runnable() {
                                                                        @Override
                                                                        public void run() {
                                                                            if (GameConfig.inChat) {
                                                                                Toast.makeText(MyService.this, "对手已下棋", Toast.LENGTH_SHORT).show();
                                                                            }
                                                                            FiveFragment.textViewPlayer1Turn.setText(getString(R.string.textview_myturn));
                                                                        }
                                                                    });
                                                                }
                                                                if (FiveFragment.textViewPlayer2Turn != null) {
                                                                    FiveFragment.textViewPlayer2Turn.post(new Runnable() {
                                                                        @Override
                                                                        public void run() {
                                                                            FiveFragment.textViewPlayer2Turn.setText("");
                                                                        }
                                                                    });
                                                                }
                                                            }
                                                        }
                                                    } else if (separate[1].equals("game")) {
                                                        if (separate[2].equals("leave")) {
                                                            Toast.makeText(MainActivity.getMainActivity(), separate[3] + " " + getString(R.string.toast_has_left_game), Toast.LENGTH_LONG).show();
                                                            GameView.turn = false;
                                                            if (!GameView.gameEnd) {
                                                                if (FiveFragment.textViewPlayer1Turn != null) {
                                                                    FiveFragment.textViewPlayer1Turn.post(new Runnable() {
                                                                        @Override
                                                                        public void run() {
                                                                            FiveFragment.textViewPlayer1Turn.setText("");
                                                                        }
                                                                    });
                                                                }
                                                                if (FiveFragment.textViewPlayer2Turn != null) {
                                                                    FiveFragment.textViewPlayer2Turn.post(new Runnable() {
                                                                        @Override
                                                                        public void run() {
                                                                            FiveFragment.textViewPlayer2Turn.setText(getString(R.string.toast_has_left_game));
                                                                        }
                                                                    });
                                                                }
                                                            }
                                                        }
                                                    }
                                                } else if (separate[0].equals("Message")) {
                                                    if (!separate[1].equals(GameActivity.sharedPreferences.getString("name", ""))) {
                                                        GameActivity.addMessage(separate[1], separate[3]);
                                                    }
                                                }
                                            }
                                        });
                                    }
                                }

                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    }).start();
                } catch (UnknownHostException e) {
                    e.printStackTrace();
                } catch (EOFException e) {
                    System.out.println("服务器崩溃啦~~~~~~~~~~~~~~~~~~~~");
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
        work.start();
        return super.onStartCommand(intent, flags, startId);
    }

}

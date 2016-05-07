package org.ivandgetic.fivechess;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.widget.Toast;

import org.ivandgetic.fivechess.models.GameConfig;

import java.io.IOException;

/**
 * Created by ivandgetic on 14/7/9.
 */
public class GameView extends SurfaceView implements SurfaceHolder.Callback {
    public static int CHESS_BLACK = 1;
    public static int CHESS_WHITE = 2;
    public static int[][] chess = new int[GameConfig.getPOINT_NUM()][GameConfig.getPOINT_NUM()];
    public static GameView gameView = null;
    public static boolean turn = false;
    public static boolean gameEnd = false;
    public static int myColor = 1;
    static String line = null;
    static String lastx = null;
    static String lasty = null;
    Paint paint = null;
    int startX = 0;
    int startY = 0;
    int GRID_WIDTH = GameConfig.getWIDTH() / GameConfig.getPOINT_NUM();

    public static GameView getGameView() {
        return gameView;
    }

    public GameView(Context context) {
        super(context);
        InitGameView();
    }

    public GameView(Context context, AttributeSet attrs) {
        super(context, attrs);
        InitGameView();
    }

    public GameView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        InitGameView();
    }

    public void InitGameView() {
        getHolder().addCallback(this);
        gameView = this;
        paint = new Paint();
        paint.setAntiAlias(true);
        startX = GRID_WIDTH / 2;
        startY = GRID_WIDTH / 2;
        lastx = null;
        lasty = null;
    }

    public static void check() {
        try {
            for (int i = 0; i < GameConfig.GRID_NUM; i++) {
                for (int j = 0; j < GameConfig.GRID_NUM; j++) {
                    if (chess[i][j] != 0) {
                        if (chess[i][j] == chess[i + 1][j] && chess[i + 1][j] == chess[i + 2][j] && chess[i + 2][j] == chess[i + 3][j] && chess[i + 3][j] == chess[i + 4][j]) {//横
                            end(i, j, i + 4, j);
                        }
                        if (chess[i][j] == chess[i][j + 1] && chess[i][j + 1] == chess[i][j + 2] && chess[i][j + 2] == chess[i][j + 3] && chess[i][j + 3] == chess[i][j + 4]) {//竖
                            end(i, j, i, j + 4);
                        }
                        if (chess[i][j] == chess[i + 1][j + 1] && chess[i + 1][j + 1] == chess[i + 2][j + 2] && chess[i + 2][j + 2] == chess[i + 3][j + 3] && chess[i + 3][j + 3] == chess[i + 4][j + 4]) {//右斜
                            end(i, j, i + 4, j + 4);
                        }
                        if (chess[i][j] == chess[i - 1][j + 1] && chess[i - 1][j + 1] == chess[i - 2][j + 2] && chess[i - 2][j + 2] == chess[i - 3][j + 3] && chess[i - 3][j + 3] == chess[i - 4][j + 4]) {//左斜
                            end(i, j, i - 4, j + 4);
                        }

                    }
                }
            }
        } catch (ArrayIndexOutOfBoundsException e) {
        }
    }

    public static void end(int i1, int j1, int i2, int j2) {
        if (chess[i1][j1] == myColor) {
            Toast.makeText(MainActivity.getMainActivity(), MainActivity.getMainActivity().getString(R.string.toast_youwin), Toast.LENGTH_LONG).show();
            FiveFragment.textViewPlayer1Turn.setText("");
            FiveFragment.textViewPlayer2Turn.setText("");
            gameEnd = true;
        } else {
            Toast.makeText(MainActivity.getMainActivity(), MainActivity.getMainActivity().getString(R.string.toast_youlost), Toast.LENGTH_LONG).show();
            gameEnd = true;
            FiveFragment.textViewPlayer1Turn.setText("");
            FiveFragment.textViewPlayer2Turn.setText("");
        }
        line = i1 + ":" + j1 + ":" + i2 + ":" + j2;
    }


    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

    }

    public void draw() {
        Canvas canvas = getHolder().lockCanvas();
        canvas.drawColor(0xffe69310);
        paint.setColor(Color.BLACK);//设置画笔为黑色
        paint.setStrokeWidth(2.5f);//设置画笔宽度
        for (int i = 0; i < GameConfig.getPOINT_NUM(); i++) {
            canvas.drawLine(startX, startY + i * GRID_WIDTH, startX + GameConfig.getGRID_NUM()
                    * GRID_WIDTH, startY + i * GRID_WIDTH, paint);//画横线
            canvas.drawLine(startX + i * GRID_WIDTH, startY, startX + i
                    * GRID_WIDTH, startY + GameConfig.getGRID_NUM() * GRID_WIDTH, paint);//画竖线
        }
        for (int i = 0; i < GameConfig.getPOINT_NUM(); i++) {
            for (int j = 0; j < GameConfig.getPOINT_NUM(); j++) {
                if (chess[i][j] == CHESS_BLACK) {
                    paint.setColor(Color.BLACK);
                    canvas.drawCircle(startX + i * GRID_WIDTH, startY + j * GRID_WIDTH, GRID_WIDTH / 2 - GRID_WIDTH * 0.05f, paint);
                }
                if (chess[i][j] == CHESS_WHITE) {
                    paint.setColor(Color.WHITE);
                    canvas.drawCircle(startX + i * GRID_WIDTH, startY + j * GRID_WIDTH, GRID_WIDTH / 2 - GRID_WIDTH * 0.05f, paint);
                }
            }
        }
        if (line != null) {
            String[] separateLine = line.split(":", 4);
            paint.setColor(0xffff0000);
            canvas.drawLine(startX + Integer.parseInt(separateLine[0]) * GRID_WIDTH, startY + Integer.parseInt(separateLine[1]) * GRID_WIDTH, startX + Integer.parseInt(separateLine[2]) * GRID_WIDTH, startY + Integer.parseInt(separateLine[3]) * GRID_WIDTH, paint);
        }
        if (!gameEnd) {
            if (lastx != null && lasty != null) {
                paint.setColor(0xffff0000);
                canvas.drawLine(startX + Integer.parseInt(lastx) * GRID_WIDTH - GRID_WIDTH * 0.2f, startY + Integer.parseInt(lasty) * GRID_WIDTH, startX + Integer.parseInt(lastx) * GRID_WIDTH + GRID_WIDTH * 0.2f, startY + Integer.parseInt(lasty) * GRID_WIDTH, paint);
                canvas.drawLine(startX + Integer.parseInt(lastx) * GRID_WIDTH, startY + Integer.parseInt(lasty) * GRID_WIDTH - GRID_WIDTH * 0.2f, startX + Integer.parseInt(lastx) * GRID_WIDTH, startY + Integer.parseInt(lasty) * GRID_WIDTH + GRID_WIDTH * 0.2f, paint);
            }
        }
        getHolder().unlockCanvasAndPost(canvas);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        float touchX = event.getX();
        float touchY = event.getY();
        if (((((touchX - startX) % GRID_WIDTH) < GRID_WIDTH * 0.4) || (((touchX - startX) % GRID_WIDTH) > GRID_WIDTH * 0.6))
                && ((((touchY - startY) % GRID_WIDTH) < GRID_WIDTH * 0.4) || (((touchY - startY) % GRID_WIDTH) > GRID_WIDTH * 0.6))) {
            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    int indexX = Math.round((touchX - startX) / GRID_WIDTH);
                    int indexY = Math.round((touchY - startY) / GRID_WIDTH);
                    if (turn && chess[indexX][indexY] == 0 && !gameEnd) {
                        chess[indexX][indexY] = myColor;
                        try {
                            MyService.getMyService().dataOutputStream.writeUTF("play:place:" + GameConfig.getPartner() + ":" + indexX + ":" + indexY + ":" + myColor);
                            lastx = String.valueOf(indexX);
                            lasty = String.valueOf(indexY);
                            turn = false;
                            check();
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
                                            FiveFragment.textViewPlayer2Turn.setText(getResources().getString(R.string.textview_partnerturn));
                                        }
                                    });
                                }
                            }
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                    break;
            }
        }
        draw();
        return true;
    }

    @Override
    public void surfaceCreated(SurfaceHolder surfaceHolder) {
        draw();
    }

    @Override
    public void surfaceChanged(SurfaceHolder surfaceHolder, int format, int width, int height) {
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder surfaceHolder) {
    }
}

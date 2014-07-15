package org.ivandgetic.fivechess;

/**
 * Created by ivandgetic on 14/7/9.
 */
public class GameConfig {
    public static int WIDTH;
    public static int POINT_NUM=13;
    public static int GRID_NUM=POINT_NUM-1;
    public static String partner;

    public static String getPartner() {
        return partner;
    }

    public static void setPartner(String partner) {
        GameConfig.partner = partner;
    }

    public static int getWIDTH() {
        return WIDTH;
    }

    public static int getPOINT_NUM() {
        return POINT_NUM;
    }

    public static void setWIDTH(int WIDTH) {
        GameConfig.WIDTH = WIDTH;
    }

    public static int getGRID_NUM() {
        return GRID_NUM;
    }

}

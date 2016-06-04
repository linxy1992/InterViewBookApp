package com.dailin.utils;

import android.app.Activity;

import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2016/4/16.
 */
public class Global {

    public static List<Map<String,Object>> values;
    public static MyOpenHelper myOpenHelper;

    public static int SCREEN_WIDTH;
    public static int SCREEN_HEIGHT;

    public static void init(Activity activity){
        myOpenHelper = new MyOpenHelper(activity);

        SCREEN_HEIGHT = activity.getWindowManager().getDefaultDisplay().getHeight();
        SCREEN_WIDTH = activity.getWindowManager().getDefaultDisplay().getWidth();
    }
}

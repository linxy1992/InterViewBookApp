package com.dailin.utils;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.dailin.interviewbookapp.MainActivity;
import com.dailin.interviewbookapp.QuestionActivity;
import com.dailin.interviewbookapp.R;
import com.dailin.interviewbookapp.SearchActivity;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2016/4/17.
 */
public class BaseActivity extends AppCompatActivity {

    private TextView headTitle;
    private Button[] btns = new Button[3];
    private Class[] targetActivities = new Class[]{MainActivity.class,QuestionActivity.class,SearchActivity.class};
    private int[] buttonImags = new int[]{R.drawable.bottombtn01b,R.drawable.bottombtn02b,R.drawable.bottombtn03b};
    private int[] buttonImagsSelected = new int[]{R.drawable.bottombtn01a,R.drawable.bottombtn02a,R.drawable.bottombtn03a};
    int i = 0;

    private Button settingBtn;

    //浮动窗口
    private PopupWindow popupWindow;
    private TextView version;
    private TextView about;
    private TextView exit;

    private  static List<Activity> allActivities = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        allActivities.add(this);
    }

    @Override
    protected void onDestroy() {
        allActivities.remove(this);
        super.onDestroy();
    }

    protected void init(String title,int index){
        //将传进的title显示到TextView上
        headTitle = (TextView) findViewById(R.id.head_title);
        headTitle.setText(title);

        btns[0] = (Button) findViewById(R.id.bottombtn01);
        btns[1] = (Button) findViewById(R.id.bottombtn02);
        btns[2] = (Button) findViewById(R.id.bottombtn03);

        for(i = 0;i<btns.length;i++){
            final int temp = i;
            if (index==i){
                btns[i].setBackgroundResource(buttonImagsSelected[i]);
            }else{
                btns[i].setBackgroundResource(buttonImags[i]);
                btns[i].setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(BaseActivity.this,targetActivities[temp]);
                        startActivity(intent);
                    }
                });
            }
        }

        //设置按钮
        settingBtn = (Button) findViewById(R.id.setting_btn);
        settingBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popupWindow = new PopupWindow(Global.SCREEN_WIDTH / 4, Global.SCREEN_HEIGHT / 6);
                View view = LayoutInflater.from(BaseActivity.this).inflate(R.layout.window_list, null);
                popupWindow.setContentView(view);

                //如果点击设置按钮时，浮动窗口正在展开，那么隐藏，否则显示
                if (popupWindow.isShowing()) {
                    popupWindow.dismiss();
                } else {
                    //设置在按钮的下方展示
                    popupWindow.showAsDropDown(settingBtn);
                }

                //找到控件
                version = (TextView) view.findViewById(R.id.win_version);//注意是view.findViewById()！！！
                about = (TextView) view.findViewById(R.id.win_about);
                exit = (TextView) view.findViewById(R.id.win_exit);

                //Log.d("ds","--------------------------------------"+version);
                //每个选项设置监听
                version.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        popupWindow.dismiss();
                        Toast.makeText(BaseActivity.this, "当前版本为1.0", Toast.LENGTH_SHORT).show();
                    }
                });

                about.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        popupWindow.dismiss();
                        final AlertDialog.Builder builder = new AlertDialog.Builder(BaseActivity.this);
                        builder.setTitle("提示信息");
                        builder.setMessage("北京航空航天大学");
                        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                            }
                        });
                        builder.create().show();//将此builder显示出来
                    }
                });

                exit.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        popupWindow.dismiss();
                        for (Activity activity : allActivities) {
                            activity.finish();
                        }
                    }
                });

            }
        });
    }


}
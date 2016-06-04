package com.dailin.interviewbookapp;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.dailin.utils.Global;
import com.dailin.utils.MyOpenHelper;
import com.dailin.utils.QuestionDao;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;

public class IndexActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Global.init(this);//MyOpenHelper myOpenHelper = new MyOpenHelper(this);
        setContentView(R.layout.activity_index);

        Thread t = new Thread(){
            @Override
            public void run() {
                try {
                    //从assets文件中获取question.txt的InputStream
                    InputStream in = getAssets().open("question.txt");

                    SharedPreferences sharedPreferences = getSharedPreferences("question_flag",0);
                    //设置一个标记，来判断是否是第一次访问、数据库中是否已经存在数据
                    boolean savedFlag = sharedPreferences.getBoolean("flag",false);
                    if(savedFlag){
                        sleep((2000));
                    }else{
                        //如果已经有数据，则将之前的数据先删除，以防加载失败时的数据出现
                        QuestionDao.deleteAllData();
                        loadInputStream(in);
                        SharedPreferences.Editor editor = sharedPreferences.edit();
                        editor.putBoolean("flag",true);
                        editor.commit();
                    }
                }
                 catch (IOException e) {
                    e.printStackTrace();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                Intent intent = new Intent(IndexActivity.this,MainActivity.class);
                startActivity(intent);
                finish();//在activity动作完成的时候，或者Activity需要关闭的时候，调用finish()
            }
        };
        t.start();
    }

    //读取文件流，并将数据放到集合中返回
    public void loadInputStream(InputStream inputStream) throws IOException {
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream,"GBK"));
        String line = null;
        //存放问题
        StringBuffer question = new StringBuffer();
        //存放答案
        StringBuffer answer = new StringBuffer();
        //定义标记判断读取的类型
        boolean questionFlag = false;
        while ((line=bufferedReader.readLine())!=null){
            if(line.equals("Start_Question_Flag")){
                //读取问题
                questionFlag = true;
            }else if(line.equals("Start_Answer_Flag")){
                //读取答案
                questionFlag = false;
            }else if(line.equals("End")){
                //读取结束，整合问题和答案到集合中去
                Map<String,Object> map = new HashMap<>();
                map.put("question",question.toString());
                map.put("answer",answer.toString());
                //数据保存
                QuestionDao.insertData(map);

                //清空question和answer
                question = new StringBuffer();
                answer = new StringBuffer();
            }else {
                //读取数据时保存
                if (questionFlag){
                    question.append(line);
                }else{
                    answer.append(line);
                }
            }
        }
    }
}

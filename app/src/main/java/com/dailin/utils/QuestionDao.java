package com.dailin.utils;

import android.database.Cursor;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2016/4/18.
 */
public class QuestionDao {

    //插入数据
    public static void insertData(Map<String,Object> map){
        String sql="insert into ques_ans(question,answer) values(?,?)";
        Global.myOpenHelper.getWritableDatabase().execSQL(sql, new Object[]{map.get("question"), map.get("answer")});
    }

    //查询数据
    public static List<Map<String,Object>> loadData(){
        List<Map<String,Object>> values = new ArrayList<>();
        String sql = "select * from ques_ans";
        Cursor cursor = Global.myOpenHelper.getReadableDatabase().rawQuery(sql,null);
        if(cursor!=null && cursor.getCount()>0){
            cursor.moveToFirst();//从第一个开始
            while (!cursor.isAfterLast()){
                Map<String,Object> map = new HashMap<>();
                map.put("question",cursor.getString(0)+" "+cursor.getString(1));
                map.put("answer",cursor.getString(2));
                map.put("showFlag",false);
                values.add(map);//数据加入到list中
                cursor.moveToNext();
            }
            cursor.close();
        }

        return values;
    }

    //分页查询数据
    public static List<Map<String,Object>> loadDataPage(int pagenum,int pagesize,String keyword){
        List<Map<String,Object>> values = new ArrayList<>();
        String sql = "select * from ques_ans where question like ? limit ?,?";
        Cursor cursor = Global.myOpenHelper.getReadableDatabase().rawQuery(sql,new String[]{"%"+keyword+"%",pagesize*pagenum-pagesize+"",pagesize+""});
        if(cursor!=null && cursor.getCount()>0){
            cursor.moveToFirst();//从第一个开始
            while (!cursor.isAfterLast()){
                Map<String,Object> map = new HashMap<>();
                map.put("question",cursor.getString(0)+" "+cursor.getString(1));
                map.put("answer",cursor.getString(2));
                map.put("showFlag",false);
                values.add(map);//数据加入到list中
                cursor.moveToNext();
            }
            cursor.close();
        }
        return values;
    }

    //查询全部记录数
    public static int getCount(String keyword){
        int recordCount = 0;
        String sql="select count(*) from ques_ans where question like ?";
        Cursor cursor = Global.myOpenHelper.getReadableDatabase().rawQuery(sql,new String[]{"%"+keyword+"%"});
        cursor.moveToFirst();
        recordCount = cursor.getInt(0);
        cursor.close();
        return recordCount;
    }


    //删除
    public static void deleteAllData(){
        String sql = "delete from ques_ans";
        Global.myOpenHelper.getWritableDatabase().execSQL(sql);
    }

}

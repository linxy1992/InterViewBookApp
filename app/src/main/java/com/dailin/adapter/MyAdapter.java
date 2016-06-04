package com.dailin.adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.dailin.interviewbookapp.R;

import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2016/4/27.
 */
public class MyAdapter extends BaseAdapter {

    private Context context;
    private List<Map<String,Object>> list;

    public MyAdapter(Context context,List<Map<String,Object>> list) {
        this.context = context;
        this.list = list;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if(convertView == null){
            convertView = LayoutInflater.from(context).inflate(R.layout.question_list,null);
        }else{
            LinearLayout linear = (LinearLayout) convertView;
            if(linear.getChildCount() == 2){
                linear.removeViewAt(1);
            }
        }

        Map<String,Object> map = list.get(position);
        TextView question = (TextView) convertView.findViewById(R.id.list_question);
        question.setText(map.get("question").toString());

        boolean showFlag = (boolean) map.get("showFlag");
        if(showFlag){
            LinearLayout linear = (LinearLayout) convertView;
            TextView anserView = new TextView(context);
            anserView.setText(map.get("answer").toString());
            anserView.setTextSize(15);
            anserView.setTextColor(Color.RED);
            linear.addView(anserView);
        }

        return convertView;
    }
}

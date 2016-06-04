package com.dailin.interviewbookapp;

import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.dailin.adapter.MyAdapter;
import com.dailin.utils.BaseActivity;
import com.dailin.utils.Global;
import com.dailin.utils.QuestionDao;

import java.util.List;
import java.util.Map;

public class QuestionActivity extends BaseActivity {

    //显示数据使用
    private ListView listView;
    private List<Map<String,Object>> values;
    private MyAdapter myAdapter;

    //分页使用
    private int pagenum = 1;
    private int pagesize = Global.SCREEN_HEIGHT/15;//！！！！！设置大小，否则会报算法（/0）的异常
    private int recordCount;
    private int pageCount;
    private String keyword;

    //list的onScroll事件使用
    private int first;
    private int visible;
    private int total;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.page_question);

        super.init("全部问题", 1);

        listView = (ListView) findViewById(R.id.list);

        //查询有没有keyword
        keyword = getIntent().getStringExtra("keyword");
        if(keyword == null){
            keyword = "";
        }


        //values = Global.values;
        values = QuestionDao.loadDataPage(pagenum, pagesize,keyword);
        //查询总记录数
        recordCount = QuestionDao.getCount(keyword);
        //总页数
        pageCount = (recordCount -1)/pagesize +1;

        //加入尾部footview
        final TextView footView = new TextView(this);
        footView.setText("正在加载，请稍后……");
        footView.setTextSize(20);
        footView.setTextColor(Color.GRAY);
        listView.addFooterView(footView);

        /*第一种
        adapter = new SimpleAdapter(this,values,R.layout.question_list,
                new String[]{"question","answer"},new int[]{R.id.list_question,R.id.list_answer});
         */
        //第二种。（要删掉R.id.list_answer）
       /* adapter = new SimpleAdapter(this,values,R.layout.question_list,
                 new String[]{"question"},new int[]{R.id.list_question});*/
        myAdapter = new MyAdapter(this,values);
        listView.setAdapter(myAdapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                LinearLayout linearLayout = (LinearLayout) view;
                //将答案通过LinearLayout直接显示出来<第一种>
                //linearLayout.getChildAt(1).setVisibility(View.VISIBLE);
                //将答案通过点击显示出在新的一行上<第二种> ->要删掉question_list里面的答案的TextView
                Map<String, Object> map = values.get(position);
                //如果没有答案行就加载
                if (linearLayout.getChildCount() == 1) {
                    TextView anserView = new TextView(QuestionActivity.this);
                    anserView.setText(map.get("answer").toString());
                    anserView.setTextSize(15);
                    anserView.setTextColor(Color.RED);
                    linearLayout.addView(anserView);
                    map.put("showFlag",true);
                    //如果已经有了答案行，则就删除
                } else {
                    linearLayout.removeViewAt(1);
                    map.put("showFlag",false);
                }
            }
        });

        listView.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
                //SCROLL_STATE_FLING; ->触屏后松开
                //SCROLL_STATE_IDLE; ->触屏后不松开
                //SCROLL_STATE_TOUCH_SCROLL; ->触屏
                if (scrollState==SCROLL_STATE_IDLE && first+visible==total && total!=0){
                    if(pagenum<pageCount) {
                        pagenum++;
                        //将分页数据加入到原有的数据上
                        values.addAll(QuestionDao.loadDataPage(pagenum, pagesize,keyword));
                        //通知改变了数据
                        myAdapter.notifyDataSetChanged();
                    }else{
                        //如果到达底部，去除footview
                        if (listView.getFooterViewsCount()>0){
                            listView.removeFooterView(footView);
                        }
                    }
                }
            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                first = firstVisibleItem;
                visible = visibleItemCount;
                total = totalItemCount;
            }
        });

    }
}

package com.dailin.interviewbookapp;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.dailin.adapter.MyAdapter;
import com.dailin.adapter.MyPagerAdapter;
import com.dailin.utils.Global;
import com.dailin.utils.QuestionDao;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    private ViewPager viewPager;
    private List<View> allViews;
    private MyPagerAdapter myPagerAdapter;

    //原BaseActivity的init()
    private TextView headTitle;
    private Button[] btns = new Button[3];
    private String[] allTitles = new String[]{"程序员面试宝典","全部问题","搜索关键字"};
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
        setContentView(R.layout.activity_main);

        this.init("程序员面试宝典", 0);

        viewPager = (ViewPager) findViewById(R.id.viewPager);
        allViews = new ArrayList<>();

        //为allView添加数据
        View mainView = LayoutInflater.from(this).inflate(R.layout.page_main,null);
        View questionView = LayoutInflater.from(this).inflate(R.layout.page_question,null);
        View searchView = LayoutInflater.from(this).inflate(R.layout.page_search,null);
        allViews.add(mainView);
        allViews.add(questionView);
        allViews.add(searchView);

        initQuestion(questionView);
        initSearch(searchView);

        //设置加载页面个数
        viewPager.setOffscreenPageLimit(3);
        //实例myPagerAdapter
        myPagerAdapter = new MyPagerAdapter(allViews);
        //设置adapter
        viewPager.setAdapter(myPagerAdapter);

        viewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                headTitle.setText(allTitles[position]);
                for(int i=0;i<btns.length;i++){
                    if(i == position){
                        btns[i].setBackgroundResource(buttonImagsSelected[i]);
                    }else{
                        btns[i].setBackgroundResource(buttonImags[i]);
                    }
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

    }
//-----------------------------------------------------------------------------------
    public void init(String title,int index){

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
            }
            btns[i].setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    viewPager.setCurrentItem(temp,true);
                }
            });
        }

        //设置按钮
        settingBtn = (Button) findViewById(R.id.setting_btn);
        settingBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popupWindow = new PopupWindow(Global.SCREEN_WIDTH / 4, Global.SCREEN_HEIGHT / 6);
                View view = LayoutInflater.from(MainActivity.this).inflate(R.layout.window_list, null);
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
                        Toast.makeText(MainActivity.this, "当前版本为1.0", Toast.LENGTH_SHORT).show();
                    }
                });

                about.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        popupWindow.dismiss();
                        final AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
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
//-----------------------------------------------------------------------------------
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
    public void initQuestion(View questionView){
        //这里是questionView.findViewById() ！！！
        listView = (ListView) questionView.findViewById(R.id.list);

        //查询有没有keyword
        if(keyword == null){
            keyword = "";
        }

        values = QuestionDao.loadDataPage(pagenum, pagesize, keyword);
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

        myAdapter = new MyAdapter(this,values);
        listView.setAdapter(myAdapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                LinearLayout linearLayout = (LinearLayout) view;
                Map<String, Object> map = values.get(position);
                //如果没有答案行就加载
                if (linearLayout.getChildCount() == 1) {
                    TextView anserView = new TextView(MainActivity.this);
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

//-----------------------------------------------------------------------------------
    private Button searchBtn;
    private EditText searchContent;
    public void initSearch(View searchQuestion){
        //这里得是searchQuestion.findViewById()！！！
        searchBtn = (Button) searchQuestion.findViewById(R.id.search_btn);
        searchContent = (EditText) searchQuestion.findViewById(R.id.search_content);

        searchBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pagenum =1;
                values.clear();
                keyword = searchContent.getText().toString();
                //将分页数据加入到原有的数据上
                values.addAll(QuestionDao.loadDataPage(pagenum,pagesize,keyword));
                myAdapter.notifyDataSetChanged();
                viewPager.setCurrentItem(1,true);
            }
        });
    }

}

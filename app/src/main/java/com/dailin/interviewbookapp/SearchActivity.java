package com.dailin.interviewbookapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.dailin.utils.BaseActivity;

public class SearchActivity extends BaseActivity {

    private Button searchBtn;
    private EditText searchContent;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.page_search);

        super.init("搜索问题", 2);

        searchBtn = (Button) findViewById(R.id.search_btn);
        searchContent = (EditText) findViewById(R.id.search_content);

        searchBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SearchActivity.this,QuestionActivity.class);
                intent.putExtra("keyword",searchContent.getText().toString());
                startActivity(intent);
            }
        });

    }
}

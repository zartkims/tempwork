package com.example.caipengli.helloworld;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import java.util.List;

public class SecActivity extends AppCompatActivity {
    private ScrollerSelectView mSelectView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sec);
        mSelectView = (ScrollerSelectView) this.findViewById(R.id.select_view);
        List<ContentItem> datas = StaticData.getContentItem(StaticData.getContentStrings(), getApplicationContext(), ScrollerSelectView.CONTENT_TEXT_SIZE_DP, 0, 0,
                getResources().getDisplayMetrics().widthPixels, 2, 2,
                5, 3,
                10, 10);
        mSelectView.setDataSource(datas);
    }
}

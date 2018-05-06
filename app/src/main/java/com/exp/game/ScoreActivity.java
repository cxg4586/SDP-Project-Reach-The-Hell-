package com.exp.game;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.ListView;

public class ScoreActivity extends AppCompatActivity {

    private ListView list;
    private ItemScoreAdapter adapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_score);
        list = (ListView) findViewById(R.id.list);
        list.setAdapter(adapter = new ItemScoreAdapter(this));
        adapter.setObjects(App.getDaoInstant().getDBScoreDao().queryBuilder().list());
    }
}

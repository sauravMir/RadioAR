package com.radioar;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

public class LauncherActivity extends ActionBarActivity {

    private ListView lstCustom;
    CustomAdapter customAdapter;
    LauncherActivity activity;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_launcher);
        activity = this;


        String[] text = getResources().getStringArray(R.array.text);

        lstCustom = (ListView) findViewById(R.id.lstCustom);
        customAdapter = new CustomAdapter(activity, text);
        lstCustom.setAdapter(customAdapter);

        lstCustom.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(LauncherActivity.this, MainActivity.class);
                startActivity(intent);

            }
        });

    }


}

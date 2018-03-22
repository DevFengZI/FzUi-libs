package com.fzui.demo;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fzui_floatlayout);
        FzDailog mdialog = new FzDailog(this);
        mdialog.show();
    }
}

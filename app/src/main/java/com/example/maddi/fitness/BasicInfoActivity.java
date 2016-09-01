package com.example.maddi.fitness;

import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

/**
 * Created by sunny on 16-Apr-16.
 */
public class BasicInfoActivity extends AppCompatActivity {

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_basicinfo);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        // setSupportActionBar(toolbar);

        CollapsingToolbarLayout collapsingToolbar= (CollapsingToolbarLayout)findViewById(R.id.collapsing_toolbar);



    }

}

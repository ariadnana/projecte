package com.example.app;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

public class DescripcioActivity extends AppCompatActivity {
    private Toolbar mTopToolbar;

    private TextView nom;
    private TextView desc;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_descripcio);

        mTopToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(mTopToolbar);
        mTopToolbar.setNavigationIcon(R.drawable.ic_arrow);
        mTopToolbar.setNavigationOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                finish();
            }
        });

        Typeface font = Typeface.createFromAsset( getAssets(), "fonts/fa-solid-900.ttf" );
        nom = (TextView)findViewById(R.id.Nom);
        nom.setTypeface(font);
        desc = (TextView)findViewById(R.id.Desc);
        desc.setTypeface(font);
        ProgressBar pgsBar = (ProgressBar)findViewById(R.id.pBar);
        nom.setText(getIntent().getExtras().getString("nom"));
        desc.setText(getString(R.string.icon_info)+" "+getIntent().getExtras().getString("desc"));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_filtre, menu);
        return true;
    }
}

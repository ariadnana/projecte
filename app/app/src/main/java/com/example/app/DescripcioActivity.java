package com.example.app;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class DescripcioActivity extends AppCompatActivity {
    private static final String TAG = "DescripcioActivity";
    private static final String URL_BASE = "http://tfg.xicota.cat/concerts/concert/";

    ConcertComplet concert;

    private TextView nom;
    private TextView desc;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_descripcio);

        nom = (TextView)findViewById(R.id.Nom);
        desc = (TextView)findViewById(R.id.Desc);
        ProgressBar pgsBar = (ProgressBar)findViewById(R.id.pBar);
        nom.setText(getIntent().getExtras().getString("nom"));
        desc.setText("Descripció: "+getIntent().getExtras().getString("desc"));
    }
}

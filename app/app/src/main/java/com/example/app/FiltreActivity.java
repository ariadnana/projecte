package com.example.app;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
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

public class FiltreActivity extends AppCompatActivity {
    private static final String TAG = "ConcertAdapter";
    private static final String URL_BASE = "http://tfg.xicota.cat/concerts/filtres";
    private Toolbar mTopToolbar;
    private String[] artistes;
    private String[] poblacions;
    AutoCompleteTextView artistesfiltre;
    AutoCompleteTextView poblacionsfiltre;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_filtre);

        fetchFiltres();

        mTopToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(mTopToolbar);

        Typeface font = Typeface.createFromAsset( getAssets(), "fonts/fa-solid-900.ttf" );
        TextView iconartista = (TextView)findViewById(R.id.iconartista);
        iconartista.setTypeface(font);
        iconartista.setText(getString(R.string.icon_artista)+" Artista:");
        TextView iconpoblacio = (TextView)findViewById(R.id.iconpoblacio);
        iconpoblacio.setTypeface(font);
        iconpoblacio.setText(getString(R.string.icon_place)+" Població:");
        artistesfiltre = findViewById(R.id.artista);
        artistesfiltre.setTypeface(font);
        poblacionsfiltre = findViewById(R.id.poblacio);
        poblacionsfiltre.setTypeface(font);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_filtre, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_home) {
            Intent intent = new Intent(FiltreActivity.this, MainActivity.class);
            FiltreActivity.this.startActivity(intent);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    void fetchFiltres() {
        RequestQueue requestQueue;
        JsonObjectRequest jsArrayRequest;

        requestQueue = Volley.newRequestQueue(this);
        jsArrayRequest = new JsonObjectRequest(
                Request.Method.GET,
                 URL_BASE,
                null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        parseJson(response);
                        ProgressBar pgsBar = (ProgressBar)findViewById(R.id.pBar);
                        pgsBar.setVisibility(View.GONE);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d(TAG, "Error Respuesta en JSON: " + error.getMessage());

                        Toast.makeText(FiltreActivity.this,
                                "Hi ha hagut un error", Toast.LENGTH_LONG).show();
                    }
                }
        );
        requestQueue.add(jsArrayRequest);
    }

    public void parseJson(JSONObject jsonObject){
        JSONArray jsonArray= null;

        try {
            artistes = jsonObject.getJSONArray("artistes").toString().replace("[\"", "").replace("\"]", "").split("\",\"");
            ArrayAdapter<String> artistesadapter = new ArrayAdapter<String>(this,
                    android.R.layout.simple_list_item_1, artistes);
            artistesfiltre.setAdapter(artistesadapter);
            poblacions = jsonObject.getJSONArray("poblacions").toString().replace("[\"", "").replace("\"]", "").split("\",\"");
            ArrayAdapter<String> poblacionsadapter = new ArrayAdapter<String>(this,
                    android.R.layout.simple_list_item_1, poblacions);
            poblacionsfiltre.setAdapter(poblacionsadapter);

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}

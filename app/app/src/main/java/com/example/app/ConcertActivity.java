package com.example.app;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
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

public class ConcertActivity extends AppCompatActivity{
    private static final String TAG = "ConcertActivity";
    private static final String URL_BASE = "http://tfg.xicota.cat/concerts/concert/";

    ConcertComplet concert;

    private int id;
    private ListView llg;
    private TextView nom;
    private TextView data;
    private TextView desc;
    private TextView localitzacio;
    private TextView poblacio;
    private TextView web;
    private TextView preu;
    private Button awesomeButton;

    List<String> artistes;
    private ArrayAdapter<String> artistesAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_concert2);

        fetchConcert();

        artistes = new ArrayList<String>();

        artistesAdapter = new ArrayAdapter<String>(
            this,
            android.R.layout.simple_list_item_1,
            artistes
        );

        llg = (ListView)findViewById(R.id.Artistes);
        LayoutInflater inflater = getLayoutInflater();
        ViewGroup header = (ViewGroup)inflater.inflate(R.layout.concertheader,llg,false);
        llg.addHeaderView(header);

        llg.setAdapter(artistesAdapter);

        nom = (TextView)findViewById(R.id.Nom);
        data = (TextView)findViewById(R.id.Data);
        desc = (TextView)findViewById(R.id.Desc);
        localitzacio = (TextView)findViewById(R.id.Localitzacio);
        web = (TextView)findViewById(R.id.Web);
        preu = (TextView)findViewById(R.id.Preu);
        Typeface font = Typeface.createFromAsset( getAssets(), "fonts/fa-solid-900.ttf" );
        awesomeButton = (Button)findViewById(R.id.awesome_button);
        awesomeButton.setTypeface(font);
        awesomeButton.setText(getString(R.string.icon_plus));
    }

    void refreshConcertInfo() {
        nom.setText(concert.getNom());
        desc.setText("Descripció: "+concert.getDesc().trim());
        if(concert.getDesc()=="null") {
            desc.setText("Descripció: ");
        } else if(desc.getLineCount()==3){
            awesomeButton.setVisibility(View.VISIBLE);
            awesomeButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(ConcertActivity.this, DescripcioActivity.class);
                    intent.putExtra("desc", concert.getDesc().trim());
                    intent.putExtra("nom", concert.getNom());
                    ConcertActivity.this.startActivity(intent);
                }
            });
        }
        preu.setText("Preu: "+concert.getPreu());
        if(concert.getPreu()=="null"){
            preu.setText("Preu: ");
        }
        data.setText("Data: "+concert.getData());
        if(concert.getData()=="null"){
            data.setText("Data: ");
        }
        localitzacio.setText("Lloc: "+concert.getLocalitzacio()+" "+concert.getPoblacio());
        if(concert.getLocalitzacio()=="null"){
            if(concert.getPoblacio()=="null"){
                localitzacio.setText("Lloc: ");
            }
        } else if(concert.getPoblacio()=="null"){
            localitzacio.setText("Lloc: "+concert.getLocalitzacio());
        }
        web.setText("Web: "+concert.getWeb());
        if(concert.getWeb()=="null"){
            web.setText("Web: ");
        }
        artistes = concert.getArtistes();
        artistesAdapter.addAll(artistes);
        artistesAdapter.notifyDataSetChanged();
    }

    void fetchConcert() {
        id = getIntent().getIntExtra("id", 0);
        RequestQueue requestQueue;
        JsonObjectRequest jsArrayRequest;

        requestQueue = Volley.newRequestQueue(this);
        jsArrayRequest = new JsonObjectRequest(
                Request.Method.GET,
                URL_BASE+id ,
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
                        Toast.makeText(ConcertActivity.this,
                                "Hi ha hagut un error", Toast.LENGTH_LONG).show();
                    }
                }
        );
        requestQueue.add(jsArrayRequest);
    }

    public void parseJson(JSONObject c) {
        JSONArray jsonArray = null;
        List<String> artistes = new ArrayList<>();

        try {
            jsonArray = c.getJSONArray("artistes");

            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject objecte = jsonArray.getJSONObject(i);
                artistes.add(objecte.getString("nom"));
            }

            concert = new ConcertComplet(
                    c.getString("nom"),
                    c.getString("data"),
                    c.getString("desc"),
                    c.getString("localitzacio"),
                    c.getString("poblacio"),
                    c.getString("web"),
                    c.getString("preu"),
                    artistes);

            refreshConcertInfo();

        } catch (JSONException e) {
            Log.e("ConcertActivity", "Error de parsing: " + e.getMessage());
        }
    }
}

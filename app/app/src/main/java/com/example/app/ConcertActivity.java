package com.example.app;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_concert);

        fetchConcert();
        TextView nom = (TextView)findViewById(R.id.Nom);
        nom.setText(concert.getNom());
        TextView data = (TextView)findViewById(R.id.Data);
        data.setText(concert.getData());
        TextView desc = (TextView)findViewById(R.id.Desc);
        desc.setText(concert.getDesc());
        TextView localitzacio = (TextView)findViewById(R.id.Localitzacio);
        localitzacio.setText(concert.getLocalitzacio());
        TextView poblacio = (TextView)findViewById(R.id.Poblacio);
        poblacio.setText(concert.getPoblacio());
        TextView web = (TextView)findViewById(R.id.Web);
        web.setText(concert.getWeb());
        TextView preu = (TextView)findViewById(R.id.Preu);
        preu.setText(concert.getPreu());

        ArrayAdapter<String> artistesAdapter = new ArrayAdapter<String>(
                this,
                android.R.layout.simple_list_item_1,
                concert.getArtistes() );

        llg=(ListView)findViewById(R.id.Artistes);
        llg.setAdapter(artistesAdapter);
        ProgressBar pgsBar = (ProgressBar)findViewById(R.id.pBar);
        pgsBar.setVisibility(View.GONE);
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
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d(TAG, "Error Respuesta en JSON: " + error.getMessage());
                        // TODO: posar un toast perquè l'usuari sàpiga que hi ha hagut un error
                    }
                }
        );
        requestQueue.add(jsArrayRequest);
    }

    public void parseJson(JSONObject c) {
        JSONArray jsonArray = null;
        List<String> a = new ArrayList<>();

        try {
            jsonArray = c.getJSONArray("artistes");

            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject objecte = jsonArray.getJSONObject(i);
                a.add(objecte.getString("nom"));
            }

            concert = new ConcertComplet(
                    c.getString("nom"),
                    c.getString("data"),
                    c.getString("desc"),
                    c.getString("localitzacion"),
                    c.getString("poblacio"),
                    c.getString("web"),
                    c.getString("preu"),
                    a);

        } catch (JSONException e) {
            Log.e("ConcertActivity", "Error de parsing: " + e.getMessage());
        }
    }
}

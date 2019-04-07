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

    private int id;
    private TextView nom;
    private TextView desc;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_descripcio);

        fetchConcert();

        nom = (TextView)findViewById(R.id.Nom);
        desc = (TextView)findViewById(R.id.Desc);
    }

    void refreshConcertInfo() {
        nom.setText(concert.getNom());
        desc.setText("Descripció: "+concert.getDesc().trim());
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
                        Toast.makeText(DescripcioActivity.this,
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

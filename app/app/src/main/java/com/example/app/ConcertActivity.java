package com.example.app;

import android.content.Intent;
import android.graphics.Typeface;
import android.net.Uri;
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
    private TextView dia;
    private TextView mes;
    private TextView hora;
    private TextView desc;
    private TextView localitzacio;
    private TextView poblacio;
    private TextView web;
    private TextView preu;
    private Button awesomeButton;
    private Button awesomeButton2;

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
            R.layout.list_item_artistes,
            artistes
        );

        llg = (ListView)findViewById(R.id.Artistes);
        LayoutInflater inflater = getLayoutInflater();
        ViewGroup header = (ViewGroup)inflater.inflate(R.layout.concertheader,llg,false);
        llg.addHeaderView(header);

        llg.setAdapter(artistesAdapter);

        Typeface font = Typeface.createFromAsset( getAssets(), "fonts/fa-solid-900.ttf" );
        nom = (TextView)findViewById(R.id.Nom);
        nom.setTypeface(font);
        dia = (TextView)findViewById(R.id.Dia);
        dia.setTypeface(font);
        mes = (TextView)findViewById(R.id.Mes);
        mes.setTypeface(font);
        hora = (TextView)findViewById(R.id.Hora);
        hora.setTypeface(font);
        desc = (TextView)findViewById(R.id.Desc);
        desc.setTypeface(font);
        localitzacio = (TextView)findViewById(R.id.Localitzacio);
        localitzacio.setTypeface(font);
        web = (TextView)findViewById(R.id.Web);
        web.setTypeface(font);
        preu = (TextView)findViewById(R.id.Preu);
        preu.setTypeface(font);
        awesomeButton = (Button)findViewById(R.id.awesome_button);
        awesomeButton.setTypeface(font);
        awesomeButton.setText(getString(R.string.icon_plus));
        awesomeButton2 = (Button)findViewById(R.id.awesome_button2);
        awesomeButton2.setTypeface(font);
        awesomeButton2.setText(getString(R.string.icon_map));
    }

    void refreshConcertInfo() {
        nom.setText(concert.getNom());
        desc.setText(getString(R.string.icon_info)+" "+concert.getDesc().trim());
        if(concert.getDesc()=="null") {
            desc.setText(getString(R.string.icon_info));
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
        preu.setText(getString(R.string.icon_price)+" "+concert.getPreu());
        if(concert.getPreu()=="null"){
            preu.setText(getString(R.string.icon_price));
        }
        dia.setText(concert.getDia());
        mes.setText(concert.getMes());
        hora.setText(getString(R.string.icon_clock)+" "+concert.getHora());
        localitzacio.setText(getString(R.string.icon_place)+" "+concert.getLocalitzacio()+" "+concert.getPoblacio());
        if(concert.getLocalitzacio()=="null"){
            if(concert.getPoblacio()=="null"){
                localitzacio.setText(getString(R.string.icon_place)+" ");
                awesomeButton2.setVisibility(View.GONE);
            }
        } else if(concert.getPoblacio()=="null"){
            localitzacio.setText(getString(R.string.icon_place)+" "+concert.getLocalitzacio());
            awesomeButton2.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent2 = new Intent(Intent.ACTION_VIEW);
                    intent2.setData(Uri.parse(concert.getMapa()));
                    startActivity(intent2);
                }
            });
        } else {
            awesomeButton2.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent2 = new Intent(Intent.ACTION_VIEW);
                    intent2.setData(Uri.parse(concert.getMapa()));
                    startActivity(intent2);
                }
            });
        }
        web.setText(getString(R.string.icon_web)+" "+concert.getWeb());
        if(concert.getWeb()=="null"){
            web.setText(getString(R.string.icon_web));
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
                    c.getString("dia"),
                    c.getString("mes"),
                    c.getString("hora"),
                    c.getString("desc"),
                    c.getString("localitzacio"),
                    c.getString("poblacio"),
                    c.getString("web"),
                    c.getString("preu"),
                    artistes,
                    c.getString("mapa"));

            refreshConcertInfo();

        } catch (JSONException e) {
            Log.e("ConcertActivity", "Error de parsing: " + e.getMessage());
        }
    }
}

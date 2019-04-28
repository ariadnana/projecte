package com.example.app;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
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
    private Toolbar mTopToolbar;

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
    private TextView titol;
    private Button awesomeButton;
    private Button awesomeButton2;
    private Button awesomeButton3;
    List<String> artistes;
    private ArrayAdapter<String> artistesAdapter;
    private SharedPreferences favspref;
    private String favs;
    private Typeface font;
    private Typeface font2;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_concert2);

        fetchConcert();

        mTopToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(mTopToolbar);
        mTopToolbar.setNavigationIcon(R.drawable.ic_arrow);
        mTopToolbar.setNavigationOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                finish();
            }
        });

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

        font = Typeface.createFromAsset( getAssets(), "fonts/fa-solid-900.ttf" );
        font2 = Typeface.createFromAsset( getAssets(), "fonts/fa-regular-400.ttf" );
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
        awesomeButton3 = (Button)findViewById(R.id.awesome_button3);
        favspref = getSharedPreferences("Preferencies", Context.MODE_PRIVATE);
        favs = favspref.getString("favs", "");
        if(favs.startsWith(String.valueOf(id)+",") || favs.contains(","+String.valueOf(id)+",")){
            awesomeButton3.setTypeface(font);
        } else {
            awesomeButton3.setTypeface(font2);
        }
        awesomeButton3.setText(getString(R.string.icon_fav));
        awesomeButton3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(favs.startsWith(String.valueOf(id)+",")){
                    awesomeButton3.setTypeface(font2);
                    favs = favs.substring((String.valueOf(id).length()+1));
                } else if (favs.contains(","+String.valueOf(id)+",")){
                    awesomeButton3.setTypeface(font2);
                    favs = favs.substring(0, favs.indexOf(","+String.valueOf(id)+","))+favs.substring((favs.indexOf(","+String.valueOf(id)+",")+String.valueOf(id).length()+1));
                } else {
                    awesomeButton3.setTypeface(font);
                    favs = favs+String.valueOf(id)+",";
                }
                SharedPreferences.Editor editor = favspref.edit();
                editor.putString("favs", favs);
                editor.commit();
            }
        });
        titol = (TextView)findViewById(R.id.titolartistes);
        titol.setTypeface(font);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_filtre, menu);
        return true;
    }

    void refreshConcertInfo() {
        nom.setText(concert.getNom());
        desc.setText(getString(R.string.icon_info)+" "+concert.getDesc().trim());
        if(concert.getDesc()=="null") {
            desc.setVisibility(View.GONE);
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
            preu.setVisibility(View.GONE);
        }
        dia.setText(concert.getDia());
        mes.setText(concert.getMes());
        hora.setText(getString(R.string.icon_clock)+" "+concert.getHora());
        localitzacio.setText(getString(R.string.icon_place)+" "+concert.getLocalitzacio()+" "+concert.getPoblacio());
        if(concert.getLocalitzacio()=="null"){
            if(concert.getPoblacio()=="null"){
                localitzacio.setVisibility(View.GONE);
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
            web.setVisibility(View.GONE);
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

    @Override
    public void onBackPressed() {
        Intent intent3 = new Intent();
        intent3.putExtra("favs",favs);
        setResult(RESULT_OK, intent3);
        finish();
    }
}

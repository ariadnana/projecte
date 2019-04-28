package com.example.app;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
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

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "ConcertAdapter";
    private static final String URL_BASE = "http://tfg.xicota.cat/concerts";

    List<Concert> concerts;
    private ConcertAdapter adapter;
    private Toolbar mTopToolbar;
    private static final int FILTRE=3;
    private static final int CONCERT=2;
    private Button borrarButton;
    private Button nofavsButton;
    private String artista;
    private String poblacio;
    private String data2;
    private Boolean gratis;
    private TextView noResults;
    private boolean doubleBackToExitPressedOnce = false;
    private String favs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mTopToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(mTopToolbar);

        concerts = new ArrayList<>();
        adapter = new ConcertAdapter(this);

        fetchConcerts(URL_BASE);

        RecyclerView llista = findViewById(R.id.llista);
        llista.setAdapter(adapter);
        llista.setLayoutManager(new LinearLayoutManager(this));

        borrarButton = (Button)findViewById(R.id.button);
        Typeface font = Typeface.createFromAsset( getAssets(), "fonts/fa-solid-900.ttf" );
        borrarButton.setTypeface(font);
        borrarButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                concerts.clear();
                artista = null;
                poblacio = null;
                data2 = null;
                gratis = null;
                fetchConcerts(URL_BASE);
                borrarButton.setVisibility(View.GONE);
            }
        });

        nofavsButton = (Button)findViewById(R.id.button2);
        nofavsButton.setTypeface(font);
        nofavsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                concerts.clear();
                favs=null;
                fetchConcerts(URL_BASE);
                nofavsButton.setVisibility(View.GONE);
            }
        });

        noResults = findViewById(R.id.noResults);
        noResults.setTypeface(font);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_search) {
            filter();
            return true;
        }

        if (id == R.id.action_favs) {
            SharedPreferences favspref = getSharedPreferences("Preferencies", Context.MODE_PRIVATE);
            favs = favspref.getString("favs", "");
            concerts.clear();
            fetchConcerts(URL_BASE+"/favs/"+favs);
            nofavsButton.setVisibility(View.VISIBLE);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    void fetchConcerts(String url) {
        RequestQueue requestQueue;
        JsonObjectRequest jsArrayRequest;

        requestQueue = Volley.newRequestQueue(this);
        jsArrayRequest = new JsonObjectRequest(
                Request.Method.GET,
                url ,
                null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        parseJson(response);
                        adapter.notifyDataSetChanged();
                        ProgressBar pgsBar = (ProgressBar)findViewById(R.id.pBar);
                        pgsBar.setVisibility(View.GONE);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d(TAG, "Error Respuesta en JSON: " + error.getMessage());

                        Toast.makeText(MainActivity.this,
                                "Hi ha hagut un error", Toast.LENGTH_LONG).show();
                    }
                }
        );
        requestQueue.add(jsArrayRequest);
    }


    private class ViewHolder extends RecyclerView.ViewHolder {
        TextView Nom, Dia, Mes, Lloc, Hora, Preu;
        ConstraintLayout parentLayout;

        Typeface font = Typeface.createFromAsset( getAssets(), "fonts/fa-solid-900.ttf" );

        ViewHolder(View itemView) {
            super(itemView);
            this.Nom = itemView.findViewById(R.id.Nom);
            this.Nom.setTypeface(font);
            this.Dia = itemView.findViewById(R.id.Dia);
            this.Dia.setTypeface(font);
            this.Mes = itemView.findViewById(R.id.Mes);
            this.Mes.setTypeface(font);
            this.Lloc = itemView.findViewById(R.id.Lloc);
            this.Lloc.setTypeface(font);
            this.Hora = itemView.findViewById(R.id.Hora);
            this.Hora.setTypeface(font);
            this.Preu = itemView.findViewById(R.id.Preu);
            this.Preu.setTypeface(font);
            this.parentLayout = itemView.findViewById(R.id.parent_layout);
        }
    }

    public class ConcertAdapter extends RecyclerView.Adapter<ViewHolder> {

        private Context mContext;

        public ConcertAdapter(Context context) {
            mContext = context;
        }

        public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View itemView = getLayoutInflater().inflate(R.layout.list_item, parent, false);
            return new ViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(@NonNull ViewHolder holder, final int pos) {
            holder.Nom.setText(String.valueOf(concerts.get(pos).Nom));
            holder.Dia.setText(String.valueOf(concerts.get(pos).Dia));
            holder.Mes.setText(String.valueOf(concerts.get(pos).Mes));
            holder.Lloc.setText(getString(R.string.icon_place)+" "+String.valueOf(concerts.get(pos).Lloc));
            holder.Hora.setText(getString(R.string.icon_clock)+" "+String.valueOf(concerts.get(pos).Hora));
            if(concerts.get(pos).getPreu()=="null") holder.Preu.setText(getString(R.string.icon_price));
            else holder.Preu.setText(getString(R.string.icon_price)+" "+String.valueOf(concerts.get(pos).Preu));
            holder.parentLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    Intent intent = new Intent(MainActivity.this, ConcertActivity.class);
                    intent.putExtra("id", concerts.get(pos).Id);
                    MainActivity.this.startActivityForResult(intent, CONCERT);
                }
            });
        }

        @Override
        public int getItemCount() {
            return concerts.size();
        }
    }

    public void parseJson(JSONObject jsonObject){
        JSONArray jsonArray= null;

        try {
            jsonArray = jsonObject.getJSONArray("items");
            if(jsonArray.length()==0){
                noResults.setVisibility(View.VISIBLE);
            } else {
                noResults.setVisibility(View.GONE);
            }
            for(int i=0; i<jsonArray.length(); i++){

                try {
                    JSONObject objecte= jsonArray.getJSONObject(i);

                    Concert post = new Concert(
                            objecte.getInt("id"),
                            objecte.getString("nom"),
                            objecte.getInt("dia"),
                            objecte.getString("mes"),
                            objecte.getString("hora"),
                            objecte.getString("localitzacio"),
                            objecte.getString("preu"));

                    concerts.add(post);

                } catch (JSONException e) {
                    Log.e("ConcertAdapter", "Error de parsing: "+ e.getMessage());
                }
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data){
        switch (requestCode){
            case FILTRE:
                if (resultCode == RESULT_OK){
                    String cond = "";
                    artista = data.getStringExtra("artista");
                    if(!artista.equals("")){
                        cond = "?artista="+artista;
                    }
                    poblacio = data.getStringExtra("poblacio");
                    if(!poblacio.equals("")){
                        if(cond=="") {
                            cond = "?poblacio="+poblacio;
                        } else {
                            cond = cond+"&poblacio="+poblacio;
                        }
                    }
                    data2 = data.getStringExtra("data");
                    if(!data2.equals("")){
                        if(cond=="") {
                            cond = "?data="+data2.substring(6)+"-"+data2.substring(3, 5)+"-"+data2.substring(0, 2);
                        } else {
                            cond = cond+"&data="+data2.substring(6)+"-"+data2.substring(3, 5)+"-"+data2.substring(0, 2);
                        }
                    }
                    gratis = data.getExtras().getBoolean("gratis");
                    if(gratis){
                        if(cond=="") {
                            cond = "?gratuit=1";
                        } else {
                            cond = cond+"&gratuit=1";
                        }
                    }
                    concerts.clear();
                    fetchConcerts(URL_BASE+cond);
                    borrarButton.setVisibility(View.VISIBLE);
                }
            case CONCERT:
                if (resultCode == RESULT_OK){
                    if(favs!=null){
                        concerts.clear();
                        favs = data.getStringExtra("favs");
                        fetchConcerts(URL_BASE+"/favs/"+favs);
                    }
                }
        }
    }

    public void filter(){
        Intent intent = new Intent(MainActivity.this, FiltreActivity.class);
        intent.putExtra("artista", artista);
        intent.putExtra("poblacio", poblacio);
        intent.putExtra("data",data2);
        intent.putExtra("gratis", gratis);
        MainActivity.this.startActivityForResult(intent, FILTRE);
    }

    @Override
    public void onBackPressed() {
        if (artista==null && poblacio==null && data2==null && gratis==null) {
            if(favs==null) {
                if (doubleBackToExitPressedOnce) {
                    super.onBackPressed();
                    return;
                }

                this.doubleBackToExitPressedOnce = true;
                Toast.makeText(this, "Prem enrere un altre cop per sortir", Toast.LENGTH_SHORT).show();

                new Handler().postDelayed(new Runnable() {

                    @Override
                    public void run() {
                        doubleBackToExitPressedOnce = false;
                    }
                }, 2000);
            } else {
                favs = null;
                concerts.clear();
                fetchConcerts(URL_BASE);
                nofavsButton.setVisibility(View.GONE);
            }
        } else {
            filter();
        }
    }
}

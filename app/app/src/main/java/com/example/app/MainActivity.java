package com.example.app;

import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mTopToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(mTopToolbar);

        concerts = new ArrayList<>();
        adapter = new ConcertAdapter(this);

        fetchConcerts();

        RecyclerView llista = findViewById(R.id.llista);
        llista.setAdapter(adapter);
        llista.setLayoutManager(new LinearLayoutManager(this));
    }

    void fetchConcerts() {
        RequestQueue requestQueue;
        JsonObjectRequest jsArrayRequest;

        requestQueue = Volley.newRequestQueue(this);
        jsArrayRequest = new JsonObjectRequest(
                Request.Method.GET,
                URL_BASE ,
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

                    Intent intent = new Intent(mContext, ConcertActivity.class);
                    intent.putExtra("id", concerts.get(pos).Id);
                    mContext.startActivity(intent);
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
}

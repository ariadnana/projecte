package com.example.app;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.LinearLayoutManager;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
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

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        RecyclerView llista = findViewById(R.id.llista);
        llista.setLayoutManager(new LinearLayoutManager(this));
        llista.setHasFixedSize(true);
        llista.setAdapter(new ConcertAdapter(this));
    }


    private class ViewHolder extends RecyclerView.ViewHolder {
        TextView Nom, Data, Lloc;

        ViewHolder(View itemView) {
            super(itemView);
            this.Nom = itemView.findViewById(R.id.Nom);
            this.Data = itemView.findViewById(R.id.Data);
            this.Lloc = itemView.findViewById(R.id.Lloc);
        }
    }

    public class ConcertAdapter extends RecyclerView.Adapter<ViewHolder> {
        private String URL_BASE = "http://192.168.1.40/API/public/web/concerts";
        private static final String TAG = "ConcertAdapter";
        List<Concert> items;
        private RequestQueue requestQueue;
        JsonObjectRequest jsArrayRequest;

        public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View itemView = getLayoutInflater().inflate(R.layout.list_item, parent, false);
            return new ViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(@NonNull ViewHolder holder, int pos) {
            holder.Nom.setText(String.valueOf(items.get(pos)));
            holder.Data.setText(String.valueOf(items.get(pos)));
            holder.Lloc.setText(String.valueOf(items.get(pos)));
        }

        @Override
        public int getItemCount() {
            return items.size();
        }

        public ConcertAdapter(Context context) {
            requestQueue = Volley.newRequestQueue(context);
            jsArrayRequest = new JsonObjectRequest(
                    Request.Method.GET,
                    URL_BASE ,
                    null,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            items = parseJson(response);
                            notifyDataSetChanged();
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Log.d(TAG, "Error Respuesta en JSON: " + error.getMessage());

                        }
                    }
            );

            requestQueue.add(jsArrayRequest);
        }
    }

    public List<Concert> parseJson(JSONObject jsonObject){
        List<Concert> Concerts = new ArrayList();
        JSONArray jsonArray= null;

        try {
            jsonArray = jsonObject.getJSONArray("items");

            for(int i=0; i<jsonArray.length(); i++){

                try {
                    JSONObject objecte= jsonArray.getJSONObject(i);

                    Concert post = new Concert(
                            objecte.getString("nom"),
                            objecte.getString("localitzacio"),
                            objecte.getString("data"));


                    Concerts.add(post);

                } catch (JSONException e) {
                    Log.e("ConcertAdapter", "Error de parsing: "+ e.getMessage());
                }
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }


        return Concerts;
    }
}

package com.shiv_ndroid98.cinebox.people;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.shiv_ndroid98.cinebox.R;

import java.util.Arrays;
import java.util.List;

public class PeopleSearchActivity extends AppCompatActivity {

    RecyclerView BmovieList;
    PopularAdaptor popularAdaptor;

    Toolbar toolbar;
    ProgressBar pb;
    RelativeLayout rl;
    List<Result> al;
    Result[] movies;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_people_search);

        String q = getIntent().getStringExtra("query");
        String URL = "https://api.themoviedb.org/3/search/person?api_key=60e3c97f405f942ade7455b8d5a6993f&search_type=ngram&query=" + q;
        BmovieList = (RecyclerView) findViewById(R.id.Bmovie_list);

        pb = (ProgressBar) findViewById(R.id.pbLoading);
        pb.setVisibility(ProgressBar.VISIBLE);


        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Popular People");


        GridLayoutManager mgridlayoutmanager = new GridLayoutManager(this, 2);
        BmovieList.setLayoutManager(mgridlayoutmanager);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }

        //API REQUEST


        popularAdaptor = new PopularAdaptor(this);

        BmovieList.setAdapter(popularAdaptor);

        StringRequest request = new StringRequest(URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("CODE", response);
                GsonBuilder gsonBuilder = new GsonBuilder();
                Gson gson = gsonBuilder.create();
                //Movie movie =  gson.fromJson(response,Movie.class);
                pb.setVisibility(ProgressBar.INVISIBLE);
                PopularPeopleResult popularPeopleResult = gson.fromJson(response, PopularPeopleResult.class);

                al = popularPeopleResult.getResults();
                movies = new Result[al.size()];
                al.toArray(movies);

                popularAdaptor.addItems(Arrays.asList(movies));

                //Log.d("TAG",movies.getImdbID());
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(PeopleSearchActivity.this, "Something went wrong ! ", Toast.LENGTH_SHORT).show();
            }
        });

        RequestQueue queue = Volley.newRequestQueue(this);
        queue.add(request);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (item.getItemId() == android.R.id.home) {
            finish();
        }

        return super.onOptionsItemSelected(item);
    }

}
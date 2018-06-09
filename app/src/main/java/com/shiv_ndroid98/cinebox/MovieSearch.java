package com.shiv_ndroid98.cinebox;

import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.Arrays;
import java.util.List;

import static android.provider.ContactsContract.CommonDataKinds.Website.URL;
import static com.shiv_ndroid98.cinebox.R.id.movie_list;

public class MovieSearch extends AppCompatActivity {

    RecyclerView movieList;
    AdaptorRecentHindi adaptor;
    Toolbar toolbar;
    DrawerLayout drawer;
    ActionBarDrawerToggle toggle;
    List<Result> al;
    Result[] movies;
    ProgressBar pb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_search);

        String q = getIntent().getStringExtra("query");
        String URL = "https://api.themoviedb.org/3/search/movie?api_key=60e3c97f405f942ade7455b8d5a6993f&query="+q;

        movieList = (RecyclerView) findViewById(movie_list);



        GridLayoutManager mgridlayoutmanager = new GridLayoutManager(this, 2);
        movieList.setLayoutManager(mgridlayoutmanager);

        //toolbar

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //drawer


        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        //navigation view


        pb = (ProgressBar) findViewById(R.id.pbLoading);
        pb.setVisibility(ProgressBar.VISIBLE);

        //infinite paging
        // RecyclerView rvItems = (RecyclerView) findViewById(R.id.rvContacts);
        // LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        movieList.setLayoutManager(mgridlayoutmanager);

        adaptor = new AdaptorRecentHindi(this);

        movieList.setAdapter(adaptor);

        StringRequest request = new StringRequest(URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("CODE", response);
                GsonBuilder gsonBuilder = new GsonBuilder();
                Gson gson = gsonBuilder.create();
                //Movie movie =  gson.fromJson(response,Movie.class);
                pb.setVisibility(ProgressBar.INVISIBLE);
                HindiMoviesRecent hindi_movies_recent = gson.fromJson(response, HindiMoviesRecent.class);

                al = hindi_movies_recent.getResults();
                //Result[] movies = new Result[al.size()];
                movies = new Result[al.size()];
                al.toArray(movies);

                adaptor.addItems(Arrays.asList(movies));

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(MovieSearch.this, "Something went wrong ! ", Toast.LENGTH_SHORT).show();
            }
        });

        RequestQueue queue = Volley.newRequestQueue(this);
        queue.add(request);


    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // The action bar home/up action should open or close the drawer.


        switch (item.getItemId()) {
            case android.R.id.home:
                drawer.openDrawer(GravityCompat.START);
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

}

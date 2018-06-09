package com.shiv_ndroid98.cinebox;

import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
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

import static android.icu.lang.UCharacter.GraphemeClusterBreak.L;

public class MovieInTheatres extends AppCompatActivity {

    String URL = "https://api.themoviedb.org/3/movie/now_playing?api_key=60e3c97f405f942ade7455b8d5a6993f&page=1";

    RecyclerView BmovieList;
    AdaptorRecentHindi adaptorRecentHindi;

    Toolbar toolbar;
    ProgressBar pb;

    List<Result> al;
    Result[] movies;
    private EndlessRecyclerViewScrollListener scrollListener;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_in_theatres);

        BmovieList = (RecyclerView) findViewById(R.id.Bmovie_list);

        pb = (ProgressBar) findViewById(R.id.pbLoading);
        pb.setVisibility(ProgressBar.VISIBLE);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("In Theatres");

        GridLayoutManager mgridlayoutmanager = new GridLayoutManager(this,2);
        BmovieList.setLayoutManager(mgridlayoutmanager);
       // LinearLayoutManager mlinear = new LinearLayoutManager(this);
        //BmovieList.setLayoutManager(mlinear);
        if(getSupportActionBar()!=null)
        {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }

        scrollListener = new EndlessRecyclerViewScrollListener(mgridlayoutmanager) {
            @Override
            public void onLoadMore(int page, int totalItemsCount, RecyclerView view) {
                // Triggered only when new data needs to be appended to the list
                // Add whatever code is needed to append new items to the bottom of the list
                //loadNextDataFromApi(page);
            }
        };


        BmovieList.addOnScrollListener(scrollListener);

        adaptorRecentHindi = new AdaptorRecentHindi(this);

        BmovieList.setAdapter(adaptorRecentHindi);

        StringRequest request = new StringRequest(URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("CODE",response);
                GsonBuilder gsonBuilder = new GsonBuilder();
                Gson gson = gsonBuilder.create();
                //Movie movie =  gson.fromJson(response,Movie.class);
                pb.setVisibility(ProgressBar.INVISIBLE);
                HindiMoviesRecent hindi_movies_recent  = gson.fromJson(response,HindiMoviesRecent.class);

                al = hindi_movies_recent.getResults();
                movies = new Result[al.size()];
                al.toArray(movies);

                adaptorRecentHindi.addItems(Arrays.asList(movies));

                //Log.d("TAG",movies.getImdbID());
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(MovieInTheatres.this,"Something went wrong ! ",Toast.LENGTH_SHORT).show();
            }
        });

        RequestQueue queue = Volley.newRequestQueue(this);
        queue.add(request);

    }

    //infinite paging load more
    public void loadNextDataFromApi(int offset) {

        //   https://api.themoviedb.org/3/movie/now_playing?api_key=###&page=1
        String URLNEXT = "https://api.themoviedb.org/3/movie/now_playing?api_key=60e3c97f405f942ade7455b8d5a6993f&page="+(offset+1);
        StringRequest request = new StringRequest(URLNEXT, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("CODE",response);
                GsonBuilder gsonBuilder = new GsonBuilder();
                Gson gson = gsonBuilder.create();
                //Movie movie =  gson.fromJson(response,Movie.class);
                pb.setVisibility(ProgressBar.INVISIBLE);

                HindiMoviesRecent hindi_movies_recent  = gson.fromJson(response,HindiMoviesRecent.class);

                List<Result> al1 = hindi_movies_recent.getResults();
                Result[] moremovies = new Result[al1.size()];
                al1.toArray(moremovies);




                adaptorRecentHindi.addItems(Arrays.asList(movies));

                //Log.d("TAG",movies.getImdbID());
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(MovieInTheatres.this,"Something went wrong ! ",Toast.LENGTH_SHORT).show();
            }
        });

        RequestQueue queue = Volley.newRequestQueue(this);
        queue.add(request);

        // Send an API request to retrieve appropriate paginated data
        //  --> Send the request including an offset value (i.e `page`) as a query parameter.
        //  --> Deserialize and construct new model objects from the API response
        //  --> Append the new data objects to the existing set of items inside the array of items
        //  --> Notify the adapter of the new items made with `notifyItemRangeInserted()`


    }


    /*@Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_search, menu);
        MenuItem searchItem = menu.findItem(R.id.action_search);
        final SearchView searchView = (SearchView) MenuItemCompat.getActionView(searchItem);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {


                String URL = "http://www.omdbapi.com/?s="+query+"&apikey=d8395802";

                StringRequest request = new StringRequest(URL, new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("CODE",response);
                        GsonBuilder gsonBuilder = new GsonBuilder();
                        Gson gson = gsonBuilder.create();
                        Movie movie =  gson.fromJson(response,Movie.class);
                        List<Search> al = movie.getSearch();
                        Search[] movies = new Search[al.size()];
                        al.toArray(movies);
                        //  BmovieList.setAdapter(new AdaptorRecentHindi(RecentHindi.this,movies));

                        //Log.d("TAG",movies.getImdbID());
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(MovieInTheatres.this,"Something went wrong ! ",Toast.LENGTH_SHORT).show();
                    }
                });

                RequestQueue queue = Volley.newRequestQueue(MovieInTheatres.this);
                queue.add(request);


                searchView.clearFocus();

                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });
        return super.onCreateOptionsMenu(menu);
    }
*/
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if(item.getItemId()==android.R.id.home)
        {
            finish();
        }



        return super.onOptionsItemSelected(item);
    }

}

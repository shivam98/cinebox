package com.shiv_ndroid98.cinebox;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
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
import com.shiv_ndroid98.cinebox.people.PopularPeople;

import java.util.Arrays;
import java.util.List;

import static com.shiv_ndroid98.cinebox.R.id.movie_list;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {


    //String URL = "https://api.themoviedb.org/3/search/movie?api_key=60e3c97f405f942ade7455b8d5a6993f&query=hello";
    String URL = "https://api.themoviedb.org/3/movie/popular?api_key=60e3c97f405f942ade7455b8d5a6993f&language=en-US&page=1";  //tmdb link for popular movies
    RecyclerView movieList;
    AdaptorRecentHindi adaptor;
    Toolbar toolbar;
    DrawerLayout drawer;
    ActionBarDrawerToggle toggle;
    List<Result> al;
    Result[] movies;
    ProgressBar pb;
    private EndlessRecyclerViewScrollListener scrollListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        movieList = (RecyclerView) findViewById(movie_list);



        GridLayoutManager mgridlayoutmanager = new GridLayoutManager(this, 2);
        movieList.setLayoutManager(mgridlayoutmanager);

        //toolbar

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //drawer

        drawer = (DrawerLayout) findViewById(R.id.drawer);
        toggle = new ActionBarDrawerToggle(this, drawer, R.string.open, R.string.close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        //navigation view

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

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

                //Log.d("TAG",movies.getImdbID());
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(MainActivity.this, "Something went wrong ! ", Toast.LENGTH_SHORT).show();
            }
        });

        RequestQueue queue = Volley.newRequestQueue(this);
        queue.add(request);


        // Retain an instance so that you can call `resetState()` for fresh searches
        scrollListener = new EndlessRecyclerViewScrollListener(mgridlayoutmanager) {
            @Override
            public void onLoadMore(int page, int totalItemsCount, RecyclerView view) {
                // Triggered only when new data needs to be appended to the list
                // Add whatever code is needed to append new items to the bottom of the list
                loadNextDataFromApi(page);

            }
        };


        // Adds the scroll listener to RecyclerView
        movieList.addOnScrollListener(scrollListener);


    }

    //infinite paging load more
    public void loadNextDataFromApi(int offset) {

        Log.d("TAG", offset + "\n");
        String URLNEXT = "https://api.themoviedb.org/3/movie/popular?api_key=60e3c97f405f942ade7455b8d5a6993f&language=en-US&page=" + (offset + 1);
        StringRequest request = new StringRequest(URLNEXT, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("CODE", response);
                GsonBuilder gsonBuilder = new GsonBuilder();
                Gson gson = gsonBuilder.create();
                //Movie movie =  gson.fromJson(response,Movie.class);
                pb.setVisibility(ProgressBar.INVISIBLE);
                HindiMoviesRecent hindi_movies_recent = gson.fromJson(response, HindiMoviesRecent.class);

                List<Result> al1 = hindi_movies_recent.getResults();

                adaptor.addItems(al1);

                Log.d("TAG", movies[0].getTitle());
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(MainActivity.this, "Something went wrong ! ", Toast.LENGTH_SHORT).show();
            }
        });

        RequestQueue queue = Volley.newRequestQueue(this);
        queue.add(request);

        // Send an API request to retrieve appropriate paginated data
        //  --> Send the request including an offset value (i.e `page`) as a query parameter.
        //  --> Deserialize and construct new model objects from the API response
        //  --> Append the new data objects to the existing set of items inside the array of items
        //  --> Notify the adapter of the new items made with `notifyItemRangeInserted()`

/*
        view.post(new Runnable() {
            @Override
            public void run() {
                adapter.notifyItemRangeInserted(curSize, allContacts.size() - 1);
            }
        });
    */
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_search, menu);
        MenuItem searchItem = menu.findItem(R.id.action_search);
        final SearchView searchView = (SearchView) MenuItemCompat.getActionView(searchItem);

        //final RecyclerView rview = (RecyclerView) findViewById(R.id.movie_list);

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {


                Intent i = new Intent(MainActivity.this,MovieSearch.class);
                i.putExtra("query",query);
                startActivity(i);

                //searchView.clearFocus();
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });
        return super.onCreateOptionsMenu(menu);

    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {

        switch (item.getItemId()) {

            case R.id.bollywood: {
                //do somthing

                Intent intent = new Intent(MainActivity.this, RecentHindi.class);
                //intent.putExtra("title",title);
                startActivity(intent);

                break;
            }

            case R.id.movieintheatre: {
                //do somthing

                Intent intent = new Intent(MainActivity.this, MovieInTheatres.class);
                //intent.putExtra("title",title);
                startActivity(intent);

                break;
            }

            case R.id.popular: {
                //do somthing

                Intent intent = new Intent(MainActivity.this, PopularMovies.class);
                //intent.putExtra("title",title);
                startActivity(intent);

                break;
            }

            case R.id.people: {

                Intent intent = new Intent(MainActivity.this, PopularPeople.class);
                //intent.putExtra("title",title);
                startActivity(intent);
                break;
            }
            case R.id.about:
            {
                Intent intent = new Intent(MainActivity.this, About.class);
                //intent.putExtra("title",title);
                startActivity(intent);
                break;
            }
        }
        //close navigation drawer
        // mDrawerLayout.closeDrawer(GravityCompat.START);


        drawer.closeDrawer(GravityCompat.START);
        return true;

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

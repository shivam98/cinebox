package com.shiv_ndroid98.cinebox.people;

import android.content.Intent;
import android.media.Image;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.shiv_ndroid98.cinebox.AdaptorRecentHindi;
import com.shiv_ndroid98.cinebox.Information;
import com.shiv_ndroid98.cinebox.R;
import com.shiv_ndroid98.cinebox.TMDBMovieInfo;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static android.R.attr.id;
import static com.shiv_ndroid98.cinebox.R.id.movie_list;
import static com.shiv_ndroid98.cinebox.R.id.plot;
import static com.shiv_ndroid98.cinebox.R.string.budget;

public class PeopleInformation extends AppCompatActivity {

    String id;
    TextView title,biography;
    TextView born,popularity;
    ProgressBar pb;
    Toolbar toolbar;
    AdaptorRecentHindi adaptor;
    ImageView img;
    List<KnownFor> al;
    KnownFor[] movies;
    NestedScrollView view;
    RecyclerView movieList;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_people_information);

        id =  getIntent().getStringExtra("id");
        Intent intent = getIntent();
        Bundle args = intent.getBundleExtra("BUNDLE");
        final List<KnownFor> object = (List<KnownFor>) args.getSerializable("ARRAYLIST");
        //Log.d("TAG", String.valueOf(object));
        final String moreinfo = "https://api.themoviedb.org/3/person/"+id+"?api_key=60e3c97f405f942ade7455b8d5a6993f&language=en-US";
        title = (TextView) findViewById(R.id.mov_title);
        biography = (TextView) findViewById(R.id.biography);
        img = (ImageView) findViewById(R.id.img_actor);
        born = (TextView) findViewById(R.id.borndate);
        pb = (ProgressBar) findViewById(R.id.pbLoading);
        popularity = (TextView)findViewById(R.id.year);
        view = (NestedScrollView)findViewById(R.id.nested_scroll);
        movieList = (RecyclerView) findViewById(movie_list);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Popular People");

        //view.setVisibility(NestedScrollView.INVISIBLE);
        //pb.setVisibility(ProgressBar.VISIBLE);

        GridLayoutManager mgridlayoutmanager = new GridLayoutManager(this, 2);
        movieList.setLayoutManager(mgridlayoutmanager);

        if(getSupportActionBar()!=null)
        {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }

        adaptor = new AdaptorRecentHindi(this);
        movieList.setAdapter(adaptor);

        StringRequest request = new StringRequest(moreinfo, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("CODE", response);
                GsonBuilder gsonBuilder = new GsonBuilder();
                Gson gson = gsonBuilder.create();

                //pb.setVisibility(ProgressBar.INVISIBLE);
                //view.setVisibility(NestedScrollView.VISIBLE);
                PopularPeopleInfo movieinfo = gson.fromJson(response, PopularPeopleInfo.class);
                //Glide.with(img.getContext()).load(movieinfo.getPoster()).into(img);
                String posterPath = "http://image.tmdb.org/t/p/w780/" + movieinfo.getProfilePath();
                Glide.with(img.getContext()).load(posterPath).placeholder(R.drawable.backimage).into(img);
                String t = movieinfo.getName();
                String pop  = Double.toString(movieinfo.getPopularity());
                popularity.setText(pop);
                title.setText(t);
                born.setText(movieinfo.getBirthday());
                biography.setText(movieinfo.getBiography());

                al = object;
                //Result[] movies = new Result[al.size()];
                movies = new KnownFor[al.size()];
                al.toArray(movies);
                //Log.d("TAG", movies[0].getTitle());

                //adaptor.addItems(Arrays.asList(movies));




            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(PeopleInformation.this, "Something went wrong ! ", Toast.LENGTH_SHORT).show();
            }
        });

        RequestQueue queue = Volley.newRequestQueue(this);
        queue.add(request);









    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if(item.getItemId()==android.R.id.home)
        {
            finish();
        }



        return super.onOptionsItemSelected(item);
    }




}

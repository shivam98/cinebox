package com.shiv_ndroid98.cinebox.people;

import android.media.Image;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
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
import com.shiv_ndroid98.cinebox.Information;
import com.shiv_ndroid98.cinebox.R;
import com.shiv_ndroid98.cinebox.TMDBMovieInfo;

import static android.R.attr.id;
import static com.shiv_ndroid98.cinebox.R.id.plot;
import static com.shiv_ndroid98.cinebox.R.string.budget;

public class PeopleInformation extends AppCompatActivity {

    String id;
    TextView title,biography;
    ImageView img;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_people_information);

        id =  getIntent().getStringExtra("id");
        final String moreinfo = "https://api.themoviedb.org/3/person/"+id+"?api_key=60e3c97f405f942ade7455b8d5a6993f&language=en-US";
        title = (TextView) findViewById(R.id.mov_title);
        biography = (TextView) findViewById(R.id.biography);
        img = (ImageView) findViewById(R.id.img_actor);

        StringRequest request = new StringRequest(moreinfo, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("CODE", response);
                GsonBuilder gsonBuilder = new GsonBuilder();
                Gson gson = gsonBuilder.create();

                //    pb.setVisibility(ProgressBar.INVISIBLE);

                PopularPeopleInfo movieinfo = gson.fromJson(response, PopularPeopleInfo.class);
                //Glide.with(img.getContext()).load(movieinfo.getPoster()).into(img);
                String t = movieinfo.getName();
                title.setText(t);
                biography.setText(movieinfo.getBiography());
                String posterPath = "http://image.tmdb.org/t/p/w780/" + movieinfo.getProfilePath();
                Glide.with(img.getContext()).load(posterPath).into(img);

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
}

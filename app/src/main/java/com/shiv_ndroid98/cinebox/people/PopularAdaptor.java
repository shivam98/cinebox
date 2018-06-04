package com.shiv_ndroid98.cinebox.people;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.shiv_ndroid98.cinebox.Information;
import com.shiv_ndroid98.cinebox.R;

import java.util.ArrayList;
import java.util.Collection;

import static com.shiv_ndroid98.cinebox.R.id.rating;

/**
 * Created by Shivam98 on 4/20/2018.
 */

public class PopularAdaptor extends RecyclerView.Adapter<PopularAdaptor.Vholder> {

    private Context context;
    private ArrayList<Result> data;

    public PopularAdaptor(Context context)
    {
        this.context = context;
        this.data = new ArrayList<>();
    }

    @Override
    public Vholder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.item_user_layout,parent,false);
        return new Vholder(view,context);
    }

    @Override
    public void onBindViewHolder(Vholder holder, int position) {
        Result movie = data.get(position);
        holder.text_movie.setText(movie.getName());
        holder.rating.setVisibility(View.INVISIBLE);
        //furthur info for bollywood recent movies
        String posterPath = "http://image.tmdb.org/t/p/w780/"+movie.getProfilePath();
       // Glide.with(holder.image_movie.getContext()).load(posterPath).override(180,150).fitCenter().into(holder.image_movie);

        Glide.with(holder.image_movie.getContext()).load(posterPath).into(holder.image_movie);

    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public void addItems(Collection<Result> items) {
        data.addAll(items);

        notifyDataSetChanged();
    }

    public class Vholder extends RecyclerView.ViewHolder implements View.OnClickListener
    {
        ImageView image_movie;
        TextView text_movie;
        TextView rating;
        private Context context;
        public Vholder(View itemView, Context context) {
            super(itemView);
            itemView.setOnClickListener(this);
            this.context = context;
            image_movie = itemView.findViewById(R.id.img_movie);
            text_movie = itemView.findViewById(R.id.movie_text);
            rating  = itemView.findViewById(R.id.rating);
        }

        @Override
        public void onClick(View view) {
            int position = getAdapterPosition();
            Result movie = data.get(position);
            String title = (movie.getName());
            String id =  movie.getId().toString();
            Intent intent = new Intent(this.context,PeopleInformation.class);
           // intent.putExtra("title",title);
            intent.putExtra("id",id);
            this.context.startActivity(intent);
        }
    }

}

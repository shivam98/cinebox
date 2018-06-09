package com.shiv_ndroid98.cinebox;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.shiv_ndroid98.cinebox.people.KnownFor;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static android.R.attr.data;


public class AdaptorRecentHindi extends RecyclerView.Adapter<AdaptorRecentHindi.Vholder>{

    private Context context;
    private ArrayList<Result> data;

    public AdaptorRecentHindi(Context context)
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
        holder.text_movie.setText(movie.getTitle());
        String rate = movie.getVoteAverage().toString();
        holder.rating_movie.setText(rate);
        //furthur info for bollywood recent movies
        String posterPath = "http://image.tmdb.org/t/p/w780/"+movie.getPosterPath();
        Glide.with(holder.image_movie.getContext()).load(posterPath).placeholder(R.drawable.backimage).into(holder.image_movie);
        //holder.type.setText("movie");

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
        TextView text_movie,rating_movie;

        private Context context;
        public Vholder(View itemView, Context context) {
            super(itemView);
            itemView.setOnClickListener(this);
            this.context = context;
            image_movie = itemView.findViewById(R.id.img_movie);
            text_movie = itemView.findViewById(R.id.movie_text);
            rating_movie = itemView.findViewById(R.id.rating);

        }

        @Override
        public void onClick(View view) {
            int position = getAdapterPosition();
            Result movie = data.get(position);
            String title = (movie.getTitle());
            String id =  movie.getId().toString();
            String poster = movie.getBackdropPath().toString();
            Intent intent = new Intent(this.context,Information.class);
            intent.putExtra("title",title);
            intent.putExtra("id",id);
            intent.putExtra("backdrop",poster);
            this.context.startActivity(intent);
        }
    }
}

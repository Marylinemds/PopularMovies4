package com.example.android.popularmovies1;

import android.content.Context;
import android.database.Cursor;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.example.android.popularmovies1.data.MoviesContract;
import com.squareup.picasso.Picasso;



import java.util.List;

import static com.example.android.popularmovies1.R.layout.movieitem;

/**
 * Created by Maryline on 2/15/2017.
 */

public class MovieAdapter extends RecyclerView.Adapter<MovieAdapter.ImageViewHolder> {

    final private ListItemClickHandler mOnClickHandler;
    private Cursor mCursor;
    int count;

    public boolean isFavorite;

    public boolean getFavorite() {
        return true;
    }

    public void setFavorite(boolean isFavorite) {
        this.isFavorite = isFavorite;
    }

    List<Movie> movies;

    public MovieAdapter(ListItemClickHandler listener, Cursor cursor) {
        mOnClickHandler = listener;
        this.mCursor = cursor;
    }

    public List<Movie> getMovies() {
        return movies;
    }

    public void setMovies(List<Movie> movies) {
        this.movies = movies;
    }

    @Override
    public ImageViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // crear context + layout inflater + create view del movieitem inflando
        // crear imageview holder y pasar el view que acabo de inflar + return imageview


        Context context = parent.getContext();
        int layoutIdForPoster = movieitem;
        LayoutInflater inflater = LayoutInflater.from(context);
        boolean shouldAttachToParentImmediately = false;

        View view = inflater.inflate(layoutIdForPoster, parent, shouldAttachToParentImmediately);
        ImageViewHolder viewHolder = new ImageViewHolder(view);


        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ImageViewHolder viewHolder, int position) {

        Context context = viewHolder.poster.getContext();

        if (!isFavorite) {

            Movie movie = movies.get(position);

            String moviePath = movie.getMoviePath();

            Picasso.with(context).load("http://image.tmdb.org/t/p/" + "w185" + moviePath).into(viewHolder.poster);



        }
        count = movies.size();

    }

    @Override
    public int getItemCount() {
            return movies == null ? 0 : movies.size();

    }

    public class ImageViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        public ImageView poster;

        public ImageViewHolder(View itemView) {
            super(itemView);

            poster = (ImageView) itemView.findViewById(R.id.movie_poster);

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            int position = getAdapterPosition();
            Movie movie = movies.get(position);
            mOnClickHandler.onClick(movie);
        }
    }

    public interface ListItemClickHandler{
        void onClick(Movie movie);
    }

    public void swapCursor(Cursor newCursor) {
        // COMPLETED (16) Inside, check if the current cursor is not null, and close it if so
        // Always close the previous mCursor first
        if (mCursor != null) mCursor.close();
        // COMPLETED (17) Update the local mCursor to be equal to  newCursor
        mCursor = newCursor;
        // COMPLETED (18) Check if the newCursor is not null, and call this.notifyDataSetChanged() if so
        if (newCursor != null) {
            // Force the RecyclerView to refresh
            this.notifyDataSetChanged();
        }
    }


}
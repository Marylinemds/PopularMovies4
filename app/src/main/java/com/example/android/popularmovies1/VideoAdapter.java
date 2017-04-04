package com.example.android.popularmovies1;

import android.content.Context;
import android.database.Cursor;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import java.util.List;

import static com.example.android.popularmovies1.R.layout.videoitem;

/**
 * Created by Maryline on 3/31/2017.
 */

/*public class VideoAdapter extends RecyclerView.Adapter<com.example.android.popularmovies1.VideoAdapter.ViewHolder>{


        final private com.example.android.popularmovies1.VideoAdapter.ListItemClickHandler mOnClickHandler;
        public Cursor mCursor;


        List<Video> videos;

        public VideoAdapter(com.example.android.popularmovies1.VideoAdapter.ListItemClickHandler listener) {
            mOnClickHandler = listener;
        }

        public List<Video> getVideos() {
            return videos;
        }

        public void setVideos(List<Video> videos) {
            this.videos = videos;
        }

        @Override
        public com.example.android.popularmovies1.VideoAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            // crear context + layout inflater + create view del movieitem inflando
            // crear imageview holder y pasar el view que acabo de inflar + return imageview


            Context context = parent.getContext();
            int layoutIdForPoster = videoitem;
            LayoutInflater inflater = LayoutInflater.from(context);
            boolean shouldAttachToParentImmediately = false;

            View view = inflater.inflate(layoutIdForPoster, parent, shouldAttachToParentImmediately);
            RecyclerView.ViewHolder viewHolder = new RecyclerView.ViewHolder(view);


            return viewHolder;
        }

        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int position) {
            // try and catch, if not null
            // coger los datos del API y meterlos al imageview del viewholder
            Context context = viewHolder.videoButton.getContext();

            Video video = videos.get(position);

            //String something = video.getsomething();

           // Picasso.with(context).load("http://image.tmdb.org/t/p/" + "w185" + moviePath).into(viewHolder.poster);

        }

        @Override
        public int getItemCount() {
            //return cuantos item en Json array
            return videos == null ? 0 : videos.size();
        }

        public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

            public Button videoButton;

            public ViewHolder(View itemView) {
                super(itemView);

                videoButton = (Button) itemView.findViewById(R.id.video_item);

                itemView.setOnClickListener(this);
            }

            @Override
            public void onClick(View view) {
                int clickedPosition = getAdapterPosition();
                mOnClickHandler.onClick(clickedPosition);
            }
        }

        public interface ListItemClickHandler{
            void onClick(int clickedItemIndex);
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

}

*/

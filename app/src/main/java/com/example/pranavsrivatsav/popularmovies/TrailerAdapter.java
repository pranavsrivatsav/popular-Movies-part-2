package com.example.pranavsrivatsav.popularmovies;

import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

/**
 * Created by Pranav Srivatsav on 2/18/2017.
 */

public class TrailerAdapter extends RecyclerView.Adapter<TrailerAdapter.TrailerAdapterViewHolder>{

    private String LOG_TAG=TrailerAdapter.class.getSimpleName();
    private Trailer[] trailerList;
    private final TrailerAdapterOnClickHandler mClickHandler;

    public interface TrailerAdapterOnClickHandler{
        void OnClick(Trailer trailer);
    }

    public void setTrailerList(Trailer[] trailerList){
        this.trailerList=trailerList;
        notifyDataSetChanged();
    }

    public TrailerAdapter(TrailerAdapterOnClickHandler mClickHandler) {
        Log.i(LOG_TAG,"constructing trailerAdapter");
        this.mClickHandler = mClickHandler;
    }

    public class TrailerAdapterViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        public final ImageView mThumbnail;
        public final TextView mTitle;

        public TrailerAdapterViewHolder(View itemView) {
            super(itemView);
            Log.i(LOG_TAG,"Constructing Trailer viewholder");
            itemView.setOnClickListener(this);
            mThumbnail = (ImageView) itemView.findViewById(R.id.thumbnail);
            mTitle = (TextView) itemView.findViewById(R.id.trailer_title);
        }

        @Override
        public void onClick(View view) {
            int position=getAdapterPosition();
            mClickHandler.OnClick(trailerList[position]);
        }
    }

    @Override
    public TrailerAdapterViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Log.i(LOG_TAG,"oncreate viewholder");
        int viewholderLayout=R.layout.trailer_item;
        LayoutInflater layoutInflater=LayoutInflater.from(parent.getContext());
        View view=layoutInflater.inflate(viewholderLayout,parent,false);
        return new TrailerAdapterViewHolder(view);
    }

    @Override
    public void onBindViewHolder(TrailerAdapterViewHolder holder, int position) {
        Log.i(LOG_TAG,"Binding viewholder");
        String traileritemTitle=trailerList[position].getTitle();
        String traileritemId=trailerList[position].getSource();
        holder.mTitle.setText(traileritemTitle);
            Picasso.with(holder.itemView.getContext())
                    .load(NetworkUtils.buildThumbnailUrl(traileritemId).toString())
                    .placeholder(R.drawable.dark_placeholder)
                    .error(R.drawable.dark_placeholder)
                    .into(holder.mThumbnail);

    }

    @Override
    public int getItemCount() {
        Log.i(LOG_TAG,"Getting thumbnail count");

        if(trailerList==null)
            return 0;
        else
            return trailerList.length;
    }
}

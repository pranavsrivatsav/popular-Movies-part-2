package com.example.pranavsrivatsav.popularmovies;

import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

/**
 * Created by Pranav Srivatsav on 2/18/2017.
 */

public class ReviewAdapter extends RecyclerView.Adapter<ReviewAdapter.ReviewAdapterViewHolder> {

    private String LOG_TAG=ReviewAdapter.class.getSimpleName();
    private Review[] reviewList;

    public int getReviewlistSize(){
        if(reviewList==null)
            return 0;
        return reviewList.length;
    }

    public void setReviewList(Review[] reviewList){
        this.reviewList=reviewList;
        notifyDataSetChanged();
    }

    public class ReviewAdapterViewHolder extends RecyclerView.ViewHolder{

        public final TextView mUser;
        public final TextView mReview;

        public ReviewAdapterViewHolder(View itemView) {
            super(itemView);
            Log.i(LOG_TAG,"Constructing Review viewholder");
            mUser = (TextView) itemView.findViewById(R.id.user);
            mReview = (TextView) itemView.findViewById(R.id.user_review);
        }


    }

    @Override
    public ReviewAdapterViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Log.i(LOG_TAG,"oncreate review viewholder");
        int viewHolderLayout=R.layout.review_item;
        LayoutInflater layoutInflater=LayoutInflater.from(parent.getContext());
        View view=layoutInflater.inflate(viewHolderLayout,parent,false);
        return new ReviewAdapterViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ReviewAdapterViewHolder holder, int position) {
        Log.i(LOG_TAG,"binding review viewholder");
        String user=reviewList[position].getAuthor();
        String review=reviewList[position].getContent();
        holder.mUser.setText(user);
        holder.mReview.setText(review);
    }

    @Override
    public int getItemCount() {
        if(reviewList==null)
            return 0;
        else
            return reviewList.length;
    }

}

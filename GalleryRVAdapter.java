package com.example.android.billsplittingapp;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import java.util.List;

/**
 * Created by praty on 26-06-2018.
 */

public class GalleryRVAdapter extends RecyclerView.Adapter<GalleryRVAdapter.GalleryViewHolder> {
    private List<GalleryRV> images;
    private Context mContext;
    String fromWich;
    GlobalVariables appState;
    public GalleryRVAdapter(Context context, List<GalleryRV> images,String fromWich) {
        mContext = context;
        this.images = images;
        this.fromWich = fromWich ;
        appState = (GlobalVariables)context.getApplicationContext();
    }
    @Override
    public GalleryRVAdapter.GalleryViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_gallery, parent, false);

        return new GalleryRVAdapter.GalleryViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(GalleryRVAdapter.GalleryViewHolder holder, int position) {
        final GalleryRV image = images.get(position);
        Glide.with(mContext)
                .load(Utils.DEAD_PERSON_GALLERY_URL+image.getImage())
                .thumbnail(0.1f)
                .apply(RequestOptions.centerCropTransform())
                //.fitCenter()
                //.diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(holder.thumbnail);

    }

    @Override
    public int getItemCount() {
        return images.size();
    }

    public class GalleryViewHolder extends RecyclerView.ViewHolder {
        public ImageView thumbnail;

        public GalleryViewHolder(View itemView) {
            super(itemView);
            thumbnail = (ImageView) itemView.findViewById(R.id.galimage);
        }
    }
}

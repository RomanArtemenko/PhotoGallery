package com.batman.photogallery;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Roman on 26.09.2017.
 */

public class PhotoGalleryFragment extends Fragment {
    private static final String TAG = "PhotoGalleryFragment";

    private RecyclerView mPhotoRecyclerView;
    private List<GalleryItem> mItems = new ArrayList<>();

    public static PhotoGalleryFragment newInstance() {
        return new PhotoGalleryFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
        new FetchItemsTask().execute();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_photo_gallery, container, false);

        mPhotoRecyclerView = (RecyclerView) v.findViewById(R.id.fragment_photo_gallery_recycler_view);
        mPhotoRecyclerView.setLayoutManager(new GridLayoutManager(getContext(), 3));

        setupAdapter();

        return v;
    }

    private void setupAdapter() {
        if (isAdded()) {
            mPhotoRecyclerView.setAdapter(new PhotoAdapter(mItems));
        }
    }

    private class PhotoHolder extends RecyclerView.ViewHolder {
        private TextView mTitleTextView;

        public PhotoHolder(View itemView) {
            super(itemView);
            mTitleTextView = (TextView) itemView;
        }

        public void bingGalleryItem(GalleryItem item) {
            mTitleTextView.setText(item.toString());
        }
    }

    private class ProgreeBarHolder extends RecyclerView.ViewHolder {
        private ProgressBar mProgressBarView;

        public ProgreeBarHolder(View itemView) {
            super(itemView);
            mProgressBarView = (ProgressBar) itemView.findViewById(R.id.progressBar);
        }

        public void  bing() {
            mProgressBarView.setIndeterminate(true);
        }
    }

    private class PhotoAdapter extends RecyclerView.Adapter {
        private final int VIEW_ITEM = 1;
        private final int VIEW_PROG = 0;

        private List<GalleryItem> mGalleryItems;

        public PhotoAdapter(List<GalleryItem> galleryItems) {
            mGalleryItems = galleryItems;
        }

        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            RecyclerView.ViewHolder vh;
            View v;
            if (viewType == VIEW_ITEM) {
                v = new TextView(getActivity());
                vh = new PhotoHolder(v);
            } else {
                v =  LayoutInflater.from(parent.getContext()).inflate(R.layout.progress_item, parent, false);
                vh = new ProgreeBarHolder(v) ;
            }
            return vh;
        }

        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
            GalleryItem galleryItem = mGalleryItems.get(position);

            if (holder instanceof PhotoHolder) {
                ((PhotoHolder) holder).bingGalleryItem(galleryItem);
            } else {
                Log.i(TAG, "Trying bind progressBar");
                ((ProgreeBarHolder) holder).bing();
                Log.i(TAG, "END Trying bind progressBar");
            }

        }

        @Override
        public int getItemCount() {
            return mGalleryItems.size();
        }

        @Override
        public int getItemViewType(int position) {
            return mGalleryItems.get(position) != null ? VIEW_ITEM : VIEW_PROG;
        }
    }

    private class FetchItemsTask extends AsyncTask<Void, Void, List<GalleryItem>> {
        @Override
        protected List<GalleryItem> doInBackground(Void... params) {
            return new FlickrFetch().fetchItems();
        }

        @Override
        protected void onPostExecute(List<GalleryItem> items) {
            mItems = items;
            mItems.add(null);
            setupAdapter();
        }
    }
}

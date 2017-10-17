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
import android.widget.LinearLayout;
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
    private boolean loadingData = false;
    private int curPage = 1;
    private int curPosition;

    public static PhotoGalleryFragment newInstance() {
        return new PhotoGalleryFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
        new FetchItemsTask().execute(curPage);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_photo_gallery, container, false);

        mPhotoRecyclerView = (RecyclerView) v.findViewById(R.id.fragment_photo_gallery_recycler_view);
        mPhotoRecyclerView.setLayoutManager(new GridLayoutManager(getContext(), 3));
        mPhotoRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                Log.i(TAG,"Current position : " + String.valueOf(curPosition));
                if (mItems.size() - 1 == curPosition && !loadingData) {
                    Toast.makeText(getContext(), "Need load next page : " + curPage, Toast.LENGTH_SHORT).show();
                    new FetchItemsTask().execute(curPage);
                }
            }
        });

        setupAdapter();

        return v;
    }

    private void setupAdapter() {
        if (isAdded()) {
            if (curPage == 1 || mPhotoRecyclerView.getAdapter() == null) {
                mPhotoRecyclerView.setAdapter(new PhotoAdapter(mItems));
            } else {
                mPhotoRecyclerView.getAdapter().notifyDataSetChanged();
            }

        }
    }

    private class PhotoHolder extends RecyclerView.ViewHolder {
        private TextView mTitleTextView;

        public PhotoHolder(View itemView) {
            super(itemView);
            mTitleTextView = (TextView) itemView;
        }

        public void bindGalleryItem(GalleryItem item) {
            mTitleTextView.setText(item.toString());
        }
    }

    private class SpinnerHolder extends RecyclerView.ViewHolder {
        private ProgressBar mProgressBar;


        public SpinnerHolder(View itemView) {
            super(itemView);
            mProgressBar = (ProgressBar) itemView.findViewById(R.id.spinner);
        }

        public void bind() {
            mProgressBar.setIndeterminate(true);
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

            if (viewType == VIEW_ITEM) {
                TextView textView = new TextView(getActivity());
                vh = new PhotoHolder(textView);
            } else {
                View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.progress_item, parent, false);
                vh = new SpinnerHolder(v);
            }

            return vh;
        }

        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
            curPosition = position;
            if (holder instanceof PhotoHolder) {
                GalleryItem galleryItem = mGalleryItems.get(position);
                ((PhotoHolder) holder).bindGalleryItem(galleryItem);
            } else {
                ((SpinnerHolder) holder).bind();
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

    private class FetchItemsTask extends AsyncTask<Integer, Void, List<GalleryItem>> {
        @Override
        protected List<GalleryItem> doInBackground(Integer... params) {
            loadingData = true;
            return new FlickrFetch().fetchItems(params[0]);
        }

        @Override
        protected void onPostExecute(List<GalleryItem> items) {
            if (!mItems.isEmpty()) {
                mItems.remove(mItems.size() - 1);
            }
            mItems.addAll(items);

            mItems.add(null);


            Log.i(TAG, "Count of items : " + mItems.size());
            setupAdapter();

            curPage++;
            Toast.makeText(getActivity(),"Next page : " + curPage, Toast.LENGTH_LONG).show();
            loadingData = false;

        }
    }
}

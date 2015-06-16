package com.example.sketch.newsdotnet;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class ArchiveRecyclerAdapter extends RecyclerView.Adapter<ArchiveRecyclerAdapter.ViewHolder> {

    private ArrayList<Article> mDataset;

    public ArchiveRecyclerAdapter(ArrayList<Article> dataset) {
        mDataset = dataset;
    }

    ArchiveAdapterCallbacks mCallbacks = null;

    @Override
    public ArchiveRecyclerAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.recycler_item_archive, parent, false);

        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(ArchiveRecyclerAdapter.ViewHolder holder, int position) {
        holder.mTextView.setText(mDataset.get(position).Title);
    }

    @Override
    public int getItemCount() {
        return mDataset.size();
    }

    public void setData(ArrayList<Article> dataset) {
        mDataset.clear();
        mDataset.addAll(dataset);
    }

    public Article getItem(int position) {
        if (position < 0 || position >= mDataset.size())
            return null;
        return mDataset.get(position);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView mTextView;

        public ViewHolder(View v) {
            super(v);
            mTextView = (TextView) v.findViewById(R.id.tv_archive_article_title);
            v.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    mCallbacks.onArchiveItemClick(view);
                }
            });
        }
    }

    public interface ArchiveAdapterCallbacks {
        void onArchiveItemClick(View view);
    }

    public void setOnArchiveItemClickListener(ArchiveAdapterCallbacks listener) {
        mCallbacks = listener;
    }
}

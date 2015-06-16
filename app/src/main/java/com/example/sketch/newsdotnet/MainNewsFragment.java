package com.example.sketch.newsdotnet;

import android.app.Activity;
import android.app.Fragment;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

/**
 * A placeholder fragment containing a simple view.
 */
public class MainNewsFragment extends Fragment {

    private RecyclerView mRecyclerView;
    private RecyclerView.LayoutManager mLayoutManager;
    private MainRecyclerAdapter mAdapter;

    public MainNewsFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_main, container, false);

        ArrayList<String> myDataset = getDataSet();

        mRecyclerView = (RecyclerView) rootView.findViewById(R.id.recycler_view_main);

        mRecyclerView.setHasFixedSize(true);

        mLayoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(mLayoutManager);

        mAdapter = new MainRecyclerAdapter(myDataset);
        mRecyclerView.setAdapter(mAdapter);

        return rootView;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
    }

    private ArrayList<String> getDataSet() {
        ArrayList<String> mDataSet = new ArrayList();

        for (int i = 0; i < 100; i++) {
            mDataSet.add(i, "item" + i);
        }
        mDataSet.add(mDataSet.size(), "пункт" + mDataSet.size() + 1);
        return mDataSet;
    }

    public class MainPageAsyncTask extends AsyncTask<Void, Void, String> {

        @Override
        protected String doInBackground(Void... params) {
            String mainPageArticlesJSON = getMainPageArticles();

            return "";
        }

        private String getMainPageArticles() {
            Uri uriGetMainPageArticles = Uri.parse("https://newsdotnet.azurewebsites.net/ClientApi/MainPageArticles");
            return ApiHelper.executeApiMethod(uriGetMainPageArticles);
        }

        @Override
        protected void onPostExecute(String s) {

        }
    }
}
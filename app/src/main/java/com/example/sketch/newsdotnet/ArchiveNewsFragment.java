package com.example.sketch.newsdotnet;

import android.app.Activity;
import android.app.FragmentTransaction;
import android.content.ContentValues;
import android.location.Address;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Date;
import java.util.Vector;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * Use the {@link ArchiveNewsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ArchiveNewsFragment extends Fragment
                                 implements ArchiveRecyclerAdapter.ArchiveAdapterCallbacks {

    private RecyclerView mRecyclerView;
    private RecyclerView.LayoutManager mLayoutManager;
    private ArchiveRecyclerAdapter mAdapter;

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     */
    // TODO: Rename and change types and number of parameters
    public static ArchiveNewsFragment newInstance() {
        ArchiveNewsFragment fragment = new ArchiveNewsFragment();
        return fragment;
    }

    public ArchiveNewsFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d("NewsDotNet", "onCreate");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Log.d("NewsDotNet", "onCreateView");

        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_archive_news, container, false);

        mRecyclerView = (RecyclerView) rootView.findViewById(R.id.recycler_view_archive);

        mRecyclerView.setHasFixedSize(true);

        mLayoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(mLayoutManager);

        mAdapter = new ArchiveRecyclerAdapter(new ArrayList<Article>());
        mRecyclerView.setAdapter(mAdapter);

        mAdapter.setOnArchiveItemClickListener(this);
        new ArchiveAsyncTask().execute();
        return rootView;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        Log.d("NewsDotNet", "onAttach");
        Log.d("NewsDotNet", "called task");
        //new ArchiveAsyncTask().execute();
    }

    @Override
    public void onDetach() {
        Log.d("NewsDotNet", "onDetach");
        super.onDetach();
    }

    @Override
    public void onArchiveItemClick(View view) {
        int position = mRecyclerView.getChildAdapterPosition(view);
        Article article = mAdapter.getItem(position);
        if (article == null)
            return;
        ((ArticleSelectCallback)getActivity()).onArticleSelected(article.AddressName);
    }

    //--------------------------  Archive Async Task  ----------------------------------

    public class ArchiveAsyncTask extends AsyncTask<Void, Void, ArrayList<Article>> {

        @Override
        protected ArrayList<Article> doInBackground(Void... params) {
            Log.d("NewsDotNet", "started task");
            String archiveArticlesJSON = getArchiveArticles();
            Log.d("NewsDotNet", "received json");
            if (null == archiveArticlesJSON)
                return null;
            return parseJson(archiveArticlesJSON);
        }

        private String getArchiveArticles() {
            Uri uriGetArchiveArticles = Uri.parse("https://newsdotnet.azurewebsites.net/ClientApi/ArchiveArticles");
            return ApiHelper.executeApiMethod(uriGetArchiveArticles);
        }

        @Override
        protected void onPostExecute(ArrayList<Article> articles) {
            if (articles == null) {
                Toast.makeText(getActivity(), "Не удалось получить данные с сервера. Проверьте сетевое соединение", Toast.LENGTH_SHORT).show();
                return;
            }
            mAdapter.setData(articles);
            mAdapter.notifyDataSetChanged();
        }

        private ArrayList<Article> parseJson(String json) {
            ArrayList<Article> result = new ArrayList<Article>();

            try {
                JSONArray articlesArray = new JSONArray(json);

                for (int i = 0; i < articlesArray.length(); i++)
                {
                    Article article = new Article();
                    JSONObject jsonArticle = articlesArray.getJSONObject(i);
                    article.Title = jsonArticle.getString("Title");
                    article.AddressName = jsonArticle.getString("AddressName");
                    result.add(article);
                }
            }
            catch (JSONException e) {
                Log.e("NewsDotNet", e.getMessage(), e);
                e.printStackTrace();
            }
            Log.d("NewsDotNet", "parse finished");
            return result;
        }
    }
}

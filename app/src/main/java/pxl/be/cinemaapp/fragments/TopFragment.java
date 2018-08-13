package pxl.be.cinemaapp.fragments;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import pxl.be.cinemaapp.R;
import pxl.be.cinemaapp.adapters.MoviesAdapter;
import pxl.be.cinemaapp.helper.CustomHttpHandler;
import pxl.be.cinemaapp.helper.ResultMovieConverter;
import pxl.be.cinemaapp.pojo.Movie;
import pxl.be.cinemaapp.pojo.MoviePojo;
import pxl.be.cinemaapp.pojo.Result;

public class TopFragment extends Fragment {
    private static final String TAG = TopFragment.class.getSimpleName();
    private static final String URL = "https://api.themoviedb.org/3/movie/top_rated";
    private static final String KEY = "?api_key=112b449b192d9449b0e7b19ff270c63c";
    private static final String PAGE = "&page=";

    private View view;
    private ProgressDialog progressDialog;
    private List<Result> resultList;
    private List<Movie> movies;
    private ObjectMapper mapper = new ObjectMapper();
    private MoviesAdapter adapter;
    private int currentPage = 1;
    private int maxPage;


    public TopFragment() {

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_top, container, true);
        resultList = new ArrayList<>();
        movies = new ArrayList<>();
        adapter = new MoviesAdapter(getContext(), R.layout.fragment_top, movies);
        ListView listView = (ListView) view.findViewById(R.id.movies_listview);
        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Fragment detailFragment = new DetailFragment();
                Bundle bundle = new Bundle();

                detailFragment.setArguments(bundle);
                FragmentManager fragmentManager = getFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

            }
        });

        new TopFragment.GetMovies().execute();

        return view;
    }


    /**
     * Async task class to get json by making HTTP call
     */
    private class GetMovies extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            progressDialog = new ProgressDialog(view.getContext());
            progressDialog.setMessage("Please wait...");
            progressDialog.setCancelable(false);
            progressDialog.show();
            resultList.clear();
            movies.clear();


            if (currentPage == maxPage) {
                Button btn = (Button) view.findViewById(R.id.next_button);
                btn.setEnabled(false);
            } else {
                Button btn = (Button) view.findViewById(R.id.next_button);
                btn.setEnabled(true);
            }
            if (currentPage == 1) {
                Button btn = (Button) view.findViewById(R.id.prev_button);
                btn.setEnabled(false);
            } else {
                Button btn = (Button) view.findViewById(R.id.prev_button);
                btn.setEnabled(true);
            }


        }

        @Override
        protected Void doInBackground(Void... arg0) {
            CustomHttpHandler customHttpHandler = new CustomHttpHandler();


            String jsonStr = customHttpHandler.makeServiceCall(URL + KEY + PAGE + currentPage);

            Log.e(TAG, "Response from URL: " + jsonStr);

            if (jsonStr != null) {
                try {
                    MoviePojo pojo = mapper.readValue(jsonStr, MoviePojo.class);
                    resultList.addAll(pojo.getResults());
                    movies = new ResultMovieConverter().getMovieListFromResultList(resultList, getContext());
                    maxPage = pojo.getTotalPages();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            } else {
                Log.e(TAG, "Couldn't get json from server.");
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            if (progressDialog.isShowing())
                progressDialog.dismiss();

            adapter.notifyDataSetChanged();
            TextView textView = (TextView) view.findViewById(R.id.page);
            textView.setText(currentPage + "/" + maxPage);
        }

    }
}


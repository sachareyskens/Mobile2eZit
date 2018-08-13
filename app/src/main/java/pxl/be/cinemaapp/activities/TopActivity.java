package pxl.be.cinemaapp.activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import pxl.be.cinemaapp.R;
import pxl.be.cinemaapp.adapters.MoviesAdapter;
import pxl.be.cinemaapp.configuration.MovieAPIConfig;
import pxl.be.cinemaapp.helper.CustomHttpHandler;
import pxl.be.cinemaapp.helper.GetGenresFromApi;
import pxl.be.cinemaapp.helper.ResultMovieConverter;
import pxl.be.cinemaapp.pojo.Genre;
import pxl.be.cinemaapp.pojo.Movie;
import pxl.be.cinemaapp.pojo.MoviePojo;
import pxl.be.cinemaapp.pojo.Result;

public class TopActivity extends AppCompatActivity {
    private static final String TAG = TopActivity.class.getSimpleName();
    private static final String URL = MovieAPIConfig.MOVIEDB_API_BASEURL + "/top_rated";
    private static final String PARAM_KEY = "?api_key=" + MovieAPIConfig.MOVIEDB_API_KEY;
    private static final String PARAM_PAGE = "&page=";

    private ProgressDialog progressDialog;

    private List<Result> resultList;
    private List<Movie> movies;
    private List<Genre> genreList;
    private ObjectMapper mapper = new ObjectMapper();
    private MoviesAdapter adapter;
    private int currentPage = 1;
    private int maxPage;
    ResultMovieConverter converter;

    private static final String PREFS_NAME = "prefs";
    private static final String PREF_DARK_THEME = "dark_theme";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        SharedPreferences preferences = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        boolean useDarkTheme = preferences.getBoolean(PREF_DARK_THEME, false);

        if (useDarkTheme) {
            setTheme(R.style.AppTheme_Dark_NoActionBar);
        }

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_top_movies);

        converter = new ResultMovieConverter();
        resultList = new ArrayList<>();
        movies = new ArrayList<>();
        genreList = new ArrayList<>();

        new GetGenresFromApi().execute();
        new TopActivity.GetMovies().execute();
    }

    @Override
    public void onPostCreate(@Nullable Bundle savedInstanceState, @Nullable PersistableBundle persistentState) {
        super.onPostCreate(savedInstanceState, persistentState);
        Log.e("ERROR", "onPostCreate()");

        ListView listView = (ListView) findViewById(R.id.movies_listview);
        if(listView==null)
            Log.e("ERROR", "List == null");
        Movie m = (Movie) listView.getAdapter().getItem(1);
        Log.e("ERROR", "There is a movie at position 1: " + m.getTitle());
    }

    public void nextPage(View view) {
        if (currentPage != maxPage) {
            currentPage++;
        }
        new TopActivity.GetMovies().execute();

    }

    public void prevPage(View view) {
        if (currentPage != 1) {
            currentPage--;
        }
        new TopActivity.GetMovies().execute();
    }

    /**
     * Async task class to get json by making HTTP call
     */
    private class GetMovies extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            // Showing progress dialog
            progressDialog = new ProgressDialog(TopActivity.this);
            progressDialog.setMessage("Please wait...");
            progressDialog.setCancelable(true);
            progressDialog.show();
            resultList.clear();
            movies.clear();

            //Button check
            if (currentPage == maxPage) {
                Button btn = (Button) findViewById(R.id.next_button);
                btn.setEnabled(false);
            } else {
                Button btn = (Button) findViewById(R.id.next_button);
                btn.setEnabled(true);
            }
            if (currentPage == 1) {
                Button btn = (Button) findViewById(R.id.prev_button);
                btn.setEnabled(false);
            } else {
                Button btn = (Button) findViewById(R.id.prev_button);
                btn.setEnabled(true);
            }


        }

        @Override
        protected Void doInBackground(Void... arg0) {
            CustomHttpHandler customHttpHandler = new CustomHttpHandler();


            String jsonStr = customHttpHandler.makeServiceCall(URL + PARAM_KEY + PARAM_PAGE + currentPage);

            Log.e(TAG, "Response from URL: " + jsonStr);

            if (jsonStr != null) {
                try {
                    MoviePojo pojo = mapper.readValue(jsonStr, MoviePojo.class);
                    resultList.addAll(pojo.getResults());
                    ResultMovieConverter resultAdapter = new ResultMovieConverter();
                    movies = resultAdapter.getMovieListFromResultList(resultList, TopActivity.this);
                    maxPage = pojo.getTotalPages();

                } catch (IOException e) {
                    e.printStackTrace();
                }
            } else {
                Log.e(TAG, "Couldn't get json from server.");
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(getApplicationContext(),
                                "Couldn't get json from server. Check LogCat for possible errors!",
                                Toast.LENGTH_LONG)
                                .show();
                    }
                });
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            if (progressDialog.isShowing())
                progressDialog.dismiss();
            setupAdapter();
            adapter.notifyDataSetChanged();
            TextView view = (TextView) findViewById(R.id.page_textview);
            view.setText(currentPage + "/" + maxPage);
        }
    }

    private void setupAdapter() {
        adapter = new MoviesAdapter(TopActivity.this, R.layout.activity_top_movies, movies);
        ListView listView = (ListView) findViewById(R.id.movies_listview);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Movie movie = adapter.getItem(position);
                Intent intent = new Intent(TopActivity.this, DetailActivity.class);
                intent.putExtra("Movie", movie);
                startActivity(intent);
            }
        });
    }
}


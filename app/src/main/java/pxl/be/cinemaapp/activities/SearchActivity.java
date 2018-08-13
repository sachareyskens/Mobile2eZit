package pxl.be.cinemaapp.activities;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SearchView;
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
import pxl.be.cinemaapp.helper.ResultMovieConverter;
import pxl.be.cinemaapp.pojo.Genre;
import pxl.be.cinemaapp.pojo.GenreWrapper;
import pxl.be.cinemaapp.pojo.Movie;
import pxl.be.cinemaapp.pojo.MoviePojo;
import pxl.be.cinemaapp.pojo.Result;


public class SearchActivity extends AppCompatActivity {
    private static final String TAG = TopActivity.class.getSimpleName();
    private static final String URL = "https://api.themoviedb.org/3/search/movie";
    private static final String KEY = "?api_key=50daba33b793597ab70080d17929c9c6";
    private static final String PAGE = "&page=";

    private ProgressDialog progressDialog;
    private List<Result> resultList;
    private List<Movie> movies;
    private ObjectMapper mapper = new ObjectMapper();
    private MoviesAdapter adapter;
    private String srcQuery;
    private List<Genre> genreList;
    private int currentPage = 1;
    private int maxPage;


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
        setContentView(R.layout.activity_search);
        resultList = new ArrayList<>();
        movies = new ArrayList<>();
        genreList = new ArrayList<>();
        adapter = new MoviesAdapter(this, R.layout.activity_search, movies);
        setButtonFalse();

        SearchView searchView = (SearchView) findViewById(R.id.search_view);
        searchView.setQueryHint("Search Movie");


        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                if (srcQuery != null) {
                    new SearchActivity.GetMovies().execute();
                } else {
                    Toast.makeText(getApplicationContext(),
                            "Please enter a search query",
                            Toast.LENGTH_LONG)
                            .show();
                }

                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                srcQuery = "&query=" + newText.replaceAll("\\s", "%20");
                currentPage = 1;
                setButtonFalse();
                return false;
            }
        });
    }

    public void setButtonFalse() {
        Button btn = (Button) findViewById(R.id.next_button);
        btn.setEnabled(false);
        Button btn2 = (Button) findViewById(R.id.prev_button);
        btn2.setEnabled(false);
    }

    public void nextPage(View view) {
        if (currentPage != maxPage) {
            currentPage++;
        }
        new SearchActivity.GetMovies().execute();

    }

    public void prevPage(View view) {
        if (currentPage != 1) {
            currentPage--;
        }
        new SearchActivity.GetMovies().execute();

    }

    private class GetMovies extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = new ProgressDialog(SearchActivity.this);
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
            String jsonStr = customHttpHandler.makeServiceCall(URL + KEY + srcQuery + PAGE + currentPage);
            String jsonStrGenre = customHttpHandler.makeServiceCall(MovieAPIConfig.MOVIEDB_AP_GENRE);

            Log.e(TAG, "Response from URL: " + jsonStr);
            if (jsonStr != null) {
                try {
                    GenreWrapper genreWrapper = mapper.readValue(jsonStrGenre,GenreWrapper.class);
                    Genre.genres = genreWrapper.getGenres();

                    MoviePojo pojo = mapper.readValue(jsonStr, MoviePojo.class);
                    resultList.addAll(pojo.getResults());
                    movies = new ResultMovieConverter().getMovieListFromResultList(resultList, getApplicationContext());
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
        adapter = new MoviesAdapter(SearchActivity.this, R.layout.activity_top_movies, movies);
        ListView listView = (ListView) findViewById(R.id.movies_listview);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Movie movie = adapter.getItem(position);
                Intent intent = new Intent(SearchActivity.this, DetailActivity.class);
                intent.putExtra("Movie", movie);
                startActivity(intent);
            }
        });
    }

}


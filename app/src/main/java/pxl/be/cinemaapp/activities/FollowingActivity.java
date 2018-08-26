package pxl.be.cinemaapp.activities;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import pxl.be.cinemaapp.R;
import pxl.be.cinemaapp.adapters.CachedMoviesDbAdapter;
import pxl.be.cinemaapp.fragments.DetailFragment;
import pxl.be.cinemaapp.helper.GetGenresFromApi;
import pxl.be.cinemaapp.pojo.Movie;


public class FollowingActivity extends AppCompatActivity {
    private static final String TAG = FollowingActivity.class.getSimpleName();

    private boolean mTwoPane;
    public static final String ARG_ITEM_ID = "Movie";
    private List<Movie> movies;
    private CustomAdapter adapter;
    private ProgressDialog progressDialog;

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
        setContentView(R.layout.activity_following);

        movies = new ArrayList<>();


        new FollowingGenresGrabber() {}.execute();

        if (findViewById(R.id.movie_detail_container) != null) {
            mTwoPane = true;
        }
    }

    private void setupRecyclerView(@NonNull RecyclerView recyclerView) {
        Log.e(TAG, "setupRecyclerView is called");
        recyclerView.setAdapter(new CustomAdapter(this, movies));
    }

    private class FollowingGenresGrabber extends GetGenresFromApi {
        public FollowingGenresGrabber() {

        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            // Showing progress dialog
            progressDialog = new ProgressDialog(FollowingActivity.this);
            progressDialog.setMessage("Please wait...");
            progressDialog.setCancelable(true);
            progressDialog.show();
            movies.clear();
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            if (progressDialog.isShowing())
                progressDialog.dismiss();
            CachedMoviesDbAdapter dbAdapter = new CachedMoviesDbAdapter(FollowingActivity.this);
            movies = dbAdapter.getData();
            Log.e(TAG, "movies size: " + movies.size());
            adapter = new CustomAdapter(FollowingActivity.this, movies);
            RecyclerView recyclerView = (RecyclerView) findViewById(R.id.movie_list);
            setupRecyclerView(recyclerView);
            for(Movie m : movies){
                Log.e("FollowingActivity", "Cached Movies Title: " + m.getTitle());
            }
        }

    }

    private class CustomAdapter extends RecyclerView.Adapter<CustomAdapter.MyViewHolder>{
        private LayoutInflater inflater;
        private Context context;
        private List<Movie> movies;

        public CustomAdapter(Context context, List<Movie> movies) {
            inflater = LayoutInflater.from(context);
            this.context = context;
            this.movies=movies;
        }

        @Override
        public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = inflater.inflate(R.layout.movie_list_content, parent, false);
            MyViewHolder holder = new MyViewHolder(view);
            return holder;
        }

        @Override
        public void onBindViewHolder(final MyViewHolder holder, final int position) {
            holder.title.setText(movies.get(position).getTitle());
            holder.genres.setText(movies.get(position).getGenresString());
            holder.releaseDate.setText(movies.get(position).getReleaseDate());

            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mTwoPane) {
                        Bundle arguments = new Bundle();
                        arguments.putParcelable("Movie", movies.get(position));
                        DetailFragment fragment = new DetailFragment();
                        fragment.setArguments(arguments);
                        getFragmentManager().beginTransaction()
                                .replace(R.id.movie_detail_container, fragment)
                                .commit();
                    } else {
                        Context context = v.getContext();
                        Intent intent = new Intent(context, DetailActivity.class);
                        intent.putExtra(ARG_ITEM_ID, movies.get(position));
                        context.startActivity(intent);
                    }
                }
            });
        }

        @Override
        public int getItemCount() {
            return movies.size();
        }

        class MyViewHolder extends RecyclerView.ViewHolder
        {
            TextView title;
            TextView genres;
            TextView releaseDate;

            public MyViewHolder(View itemView) {
                super(itemView);
                title = (TextView)itemView.findViewById(R.id.title_textview);
                genres = (TextView)itemView.findViewById(R.id.genre_textview);
                releaseDate = (TextView)itemView.findViewById(R.id.releasedate_textview);
            }
        }
    }
}

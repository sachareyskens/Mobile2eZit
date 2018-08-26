package pxl.be.cinemaapp.activities;
import android.app.Fragment;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.FacebookSdk;
import com.facebook.share.model.SharePhoto;
import com.facebook.share.model.SharePhotoContent;
import com.facebook.share.widget.ShareButton;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.android.youtube.player.YouTubeBaseActivity;
import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayerView;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import pxl.be.cinemaapp.R;
import pxl.be.cinemaapp.adapters.CachedMoviesDbAdapter;
import pxl.be.cinemaapp.configuration.YoutubeConfig;
import pxl.be.cinemaapp.helper.CustomHttpHandler;
import pxl.be.cinemaapp.helper.GetGenresFromApi;
import pxl.be.cinemaapp.pojo.Id;
import pxl.be.cinemaapp.pojo.Item;
import pxl.be.cinemaapp.pojo.Movie;
import pxl.be.cinemaapp.pojo.YoutubePojo;

public class DetailActivity extends YouTubeBaseActivity
        implements YouTubePlayer.OnInitializedListener {
    private static final int RECOVERY_REQUEST = 1;
    private YouTubePlayerView youTubeView;
    private static final String TAG = DetailActivity.class.getSimpleName();
    private static final String URL = "https://www.googleapis.com/youtube/v3/search?part=id&q=";
    private static final String URL2 = "&type=video&key=AIzaSyAJBYk-qMTl7QvGa9RyFlSb2L5Bo9X7wLo";

    private Movie movie;
    private ProgressDialog progressDialog;
    private List<Item> itemslist;
    private ShareButton shareButton;
    private Bitmap image;
    private int counter = 0;
    private ObjectMapper mapper = new ObjectMapper();
    private static final String PREFS_NAME = "prefs";
    private static final String PREF_DARK_THEME = "dark_theme";

    private TextView label;
    private TextView overview_tv;
    private ImageView image_view;
    private RatingBar rating_bar;
    private TextView content_tv;
    private TextView release_tv;
    private TextView genres_tv;
    private Switch followed_switch;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        // Code cleanup requested by Servaas Tilkin
        label = ((TextView) findViewById(R.id.title_label));
        overview_tv = ((TextView) findViewById(R.id.overview_tv));
        image_view = ((ImageView) findViewById(R.id.imageView));
        rating_bar = ((RatingBar) findViewById(R.id.ratingBar));
        content_tv = ((TextView) findViewById(R.id.origional_content_tv));
        release_tv = ((TextView) findViewById(R.id.release_tv));
        genres_tv = ((TextView) findViewById(R.id.genres_tv));
        followed_switch = ((Switch) findViewById(R.id.followed_switch));

        SharedPreferences preferences = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        boolean useDarkTheme = preferences.getBoolean(PREF_DARK_THEME, false);

        if (useDarkTheme) {
            setTheme(R.style.AppTheme_Dark_NoActionBar);
        }
        super.onCreate(savedInstanceState);
        FacebookSdk.sdkInitialize(getApplicationContext());
        setContentView(R.layout.activity_detail);
        Bundle extras = getIntent().getExtras();

        movie = (Movie) extras.get("Movie");
        itemslist = new ArrayList<>();

        final Button button = (Button) findViewById(R.id.followed_switch);
        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // Code here executes on main thread after user presses button
                if (movie.isCached()) {
                    CachedMoviesDbAdapter dbAdapter = new CachedMoviesDbAdapter(DetailActivity.this);
                    long id = dbAdapter.delete(movie.getId());
                    if (id > 0) {
                        Log.e("/DetailActivity", movie.getTitle() + " successfully uncached");
                    } else {
                        Log.e("/DetailActivity", movie.getTitle() + " no such cached movie found !");
                    }
                } else {
                    CachedMoviesDbAdapter dbAdapter = new CachedMoviesDbAdapter(DetailActivity.this);
                    long id = dbAdapter.insertData(movie);
                    Log.e("DetailActivity/", "id: " + id);
                    List<Movie> movies = dbAdapter.getData();

                    for (Movie m : movies) {
                        if (m.getOriginalTitle() == movie.getOriginalTitle())
                            Log.e("/DetailActivity", m.getTitle() + " successfully cached");
                    }
                }
            }
        });


        new GetGenresFromApi().execute();
        new DetailActivity.GetYoutbeVideo().execute();

    }


    public void playVideo() {
        youTubeView = (YouTubePlayerView) findViewById(R.id.youtube_view);
        youTubeView.initialize(YoutubeConfig.YOUTUBE_API_KEY, this);
    }

    @Override
    public void onInitializationSuccess(YouTubePlayer.Provider provider, YouTubePlayer player, boolean wasRestored) {
        if (!wasRestored) {
            player.cueVideo(movie.getVideoUrl());
        }
    }

    @Override
    public void onInitializationFailure(YouTubePlayer.Provider provider, YouTubeInitializationResult errorReason) {
        Log.e(TAG, "onInitializationFailure()");
        if (errorReason.isUserRecoverableError()) {
            errorReason.getErrorDialog(this, RECOVERY_REQUEST).show();
        } else {
            String error = String.format(getString(R.string.player_error), errorReason.toString());
            Toast.makeText(this, error, Toast.LENGTH_LONG).show();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == RECOVERY_REQUEST) {
            // Retry initialization if user performed a recovery action
            getYouTubePlayerProvider().initialize(YoutubeConfig.YOUTUBE_API_KEY, this);
        }
    }

    protected YouTubePlayer.Provider getYouTubePlayerProvider() {
        return youTubeView;
    }

    public void setValues() {
        label.setText(" " + movie.getOriginalTitle());
        overview_tv.setText(movie.getOverview());
        image_view.setImageBitmap(movie.getPoster());
        rating_bar.setRating((float) (movie.getVoteAverage() / 2));
        content_tv.setText(movie.getOriginalLanguage().toUpperCase());
        release_tv.setText(movie.getReleaseDate());
        genres_tv.setText(movie.getGenresString());
        followed_switch.setChecked(movie.isCached());
    }


    /**
     * Async task class to get json by making HTTP call
     */
    private class GetYoutbeVideo extends AsyncTask<Void, Void, Void> {

        private boolean requestHasFailed = true;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            // Showing progress dialog
            progressDialog = new ProgressDialog(DetailActivity.this);
            progressDialog.setMessage("Please wait...");
            progressDialog.setCancelable(false);
            progressDialog.show();
        }

        @Override
        protected void onCancelled() {
            Log.e("DetailActivity", "Youtube error when loading video");
        }

        @Override
        protected Void doInBackground(Void... arg0) {
            CustomHttpHandler customHttpHandler = new CustomHttpHandler();

            // Making a request to URL and getting response
            String jsonStr = customHttpHandler.makeServiceCall(URL +
                    movie.getOriginalTitle().replaceAll("\\s+", "+") + "+Trailer" + URL2);

            //Log.e(TAG, "Response from URL: " + jsonStr);

            if (jsonStr != null) {
                try {
                    YoutubePojo pojo = mapper.readValue(jsonStr, YoutubePojo.class);
                    itemslist.addAll(pojo.getItems());
                    Item item = itemslist.get(0);
                    Id id = item.getId();
                    movie.setVideoUrl(id.getVideoId());
                    requestHasFailed = false;
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
                movie.setVideoUrl(null);
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            if (progressDialog.isShowing())
                progressDialog.dismiss();
            playVideo();
            if (!requestHasFailed) {

            } else {
                Log.e(TAG, "Couldn't get youtube video from server.");
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(getApplicationContext(),
                                "Couldn't get youtube video from server. Check internet connection!",
                                Toast.LENGTH_LONG)
                                .show();
                    }
                });
            }

            setCachedMovies();
            setValues();

            //save the screenshot
            View rootView = findViewById(android.R.id.content).getRootView();
            rootView.setDrawingCacheEnabled(true);
            // creates immutable clone of image
            image = Bitmap.createBitmap(rootView.getDrawingCache());
            // destroy
            rootView.destroyDrawingCache();


            ShareButton fbShareButton = (ShareButton) findViewById(R.id.share_button);
            SharePhoto photo = new SharePhoto.Builder().setBitmap(image).build();
            SharePhotoContent content = new SharePhotoContent.Builder().addPhoto(photo).build();

            fbShareButton.setShareContent(content);
        }

    }

    public void setCachedMovies() {
        CachedMoviesDbAdapter dbAdapter = new CachedMoviesDbAdapter(DetailActivity.this);
        List<Movie> movies = dbAdapter.getData();
        for (Movie m : movies) {
            if (m.getId().equals(movie.getId())) {
                Log.e("DetailActivity", m.getTitle() + " is cached");
                movie.setCached(true);
                return;
            }
        }
        if (!movie.isCached())
            Log.e("DetailActivity", movie.getTitle() + " not cached");
    }
}


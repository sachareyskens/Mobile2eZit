package pxl.be.cinemaapp.fragments;
import android.app.Fragment;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.Switch;
import android.widget.TextView;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.List;

import pxl.be.cinemaapp.R;
import pxl.be.cinemaapp.adapters.CachedMoviesDbAdapter;
import pxl.be.cinemaapp.pojo.Item;
import pxl.be.cinemaapp.pojo.Movie;


public class DetailFragment extends Fragment {
    private static final String URL = "https://www.googleapis.com/youtube/v3/search?part=id&q=";
    private static final String URL2 = "&type=video&key=AIzaSyAxwM69lMTZPJyNgxGJ1d_B47jaPbmh0qw";

    private static final String TAG = DetailFragment.class.getSimpleName();

    private View view;

    private static final int RECOVERY_REQUEST = 1;
    private Movie movie;

    private ProgressDialog progressDialog;
    private List<Item> itemslist;
    private ObjectMapper mapper = new ObjectMapper();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Log.e(TAG, "onCreateView");
        view = inflater.inflate(R.layout.fragment_detail, container, false);
        Log.e(TAG, "after inflate");
        Bundle bundle = this.getArguments();
        if (bundle != null) {
            Log.e(TAG, "bundle != null");
            movie = bundle.getParcelable("Movie");
            Log.e(TAG, "bundle.getParcelable");
            setValues();
        }
        return view;
    }

    private void setValues() {
        Log.e(TAG, "setValues()");
        ((TextView) view.findViewById(R.id.title_label)).setText(" " + movie.getOriginalTitle());
        ((TextView) view.findViewById(R.id.overview_tv)).setText(movie.getOverview());
        ((ImageView) view.findViewById(R.id.imageView)).setImageBitmap(movie.getPoster());
        ((RatingBar) view.findViewById(R.id.ratingBar)).setRating((float) (movie.getVoteAverage()/2));
        ((TextView) view.findViewById(R.id.origional_content_tv)).setText(movie.getOriginalLanguage().toUpperCase());
        ((TextView) view.findViewById(R.id.release_tv)).setText(movie.getReleaseDate());
        ((TextView) view.findViewById(R.id.genres_tv)).setText(movie.getGenresString());
        ((Switch) view.findViewById(R.id.followed_switch)).setChecked(movie.isCached());
        Log.e(TAG, "End setValues()");
    }


    private void setCachedMovies() {
        CachedMoviesDbAdapter dbAdapter = new CachedMoviesDbAdapter(getActivity());
        List<Movie> movies = dbAdapter.getData();
        for(Movie m : movies){
            if(m.getId().equals(movie.getId())){
                Log.e("DetailFragment", m.getTitle() + " is cached");
                movie.setCached(true);
                return;
            }
        }
        if(!movie.isCached())
            Log.e("DetailFragment", movie.getTitle() + " not cached");
    }

}


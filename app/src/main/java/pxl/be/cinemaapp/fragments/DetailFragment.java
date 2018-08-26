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
import pxl.be.cinemaapp.activities.DetailActivity;
import pxl.be.cinemaapp.adapters.CachedMoviesDbAdapter;
import pxl.be.cinemaapp.pojo.Item;
import pxl.be.cinemaapp.pojo.Movie;


public class DetailFragment extends Fragment {


    private static final String TAG = DetailFragment.class.getSimpleName();

    private View view;

    private static final int RECOVERY_REQUEST = 1;
    private Movie movie;
    private DetailActivity detailActivity;
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
        detailActivity.setValues();
    }


    private void setCachedMovies() {
        detailActivity.setCachedMovies();
    }

}


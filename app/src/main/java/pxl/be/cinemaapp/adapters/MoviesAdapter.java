package pxl.be.cinemaapp.adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

import pxl.be.cinemaapp.R;
import pxl.be.cinemaapp.pojo.Movie;

public class MoviesAdapter extends ArrayAdapter<Movie> {

    public MoviesAdapter(Context context, int resource, List<Movie> objects) {
        super(context, resource, objects);
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Movie movie = getItem(position);
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.movie_list_content, parent, false);
        }
        TextView tvName = (TextView) convertView.findViewById(R.id.title_textview);
        TextView tvHome = (TextView) convertView.findViewById(R.id.releasedate_textview);
        TextView tvGenre =  (TextView) convertView.findViewById(R.id.genre_textview);
        tvName.setText(movie.getTitle());
        tvHome.setText(movie.getReleaseDate());
        tvGenre.setText(movie.getGenresString());
        return convertView;
    }
}


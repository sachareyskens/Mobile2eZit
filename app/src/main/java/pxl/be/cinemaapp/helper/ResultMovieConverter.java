package pxl.be.cinemaapp.helper;
import com.squareup.picasso.Picasso;
import android.content.Context;
import android.graphics.Bitmap;

import android.net.Uri;
import android.util.Log;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


import pxl.be.cinemaapp.R;
import pxl.be.cinemaapp.pojo.Genre;
import pxl.be.cinemaapp.pojo.Movie;
import pxl.be.cinemaapp.pojo.Result;

public class ResultMovieConverter {
    Bitmap image;

    public List<Movie> getMovieListFromResultList(List<Result> results, Context context) {
        List<Movie> movies = new ArrayList<>();
        for (Result r : results) {
            movies.add(getMovieFromResult(r, context));
        }
        return movies;
    }

    public Movie getMovieFromResult(Result result, Context context) {
        Movie movie = new Movie();

        movie.setVoteCount(result.getVoteCount());
        movie.setId(result.getId());
        movie.setVoteAverage(result.getVoteAverage());
        movie.setTitle(result.getTitle());
        movie.setPopularity(result.getPopularity());
        movie.setPoster(new ResultMovieConverter().getBitmapFromUrl(context, "https://image.tmdb.org/t/p/w150" +
                result.getPosterPath()));
        movie.setOriginalLanguage(result.getOriginalLanguage());
        movie.setOriginalTitle(result.getOriginalTitle());
        movie.setGenres(getGenresFromIds(result.getGenreIds()));
        getGenresFromIds(result.getGenreIds());
        movie.setBackdrop(new ResultMovieConverter().getBitmapFromUrl(context, "https://image.tmdb.org/t/p/w150" +
                result.getBackdropPath()));
        movie.setAdult(result.getAdult());
        movie.setOverview(result.getOverview());
        movie.setReleaseDate(result.getReleaseDate());

        return movie;
    }

    private List<Genre> getGenresFromIds(List<Integer> genresIds){
        List<Genre> genres = new ArrayList<>();
        for (Integer i : genresIds) {
            for (Genre aG : Genre.genres) {
                if (i == aG.getId()) {
                    genres.add(aG);
                }
            }
        }
        return genres;
    }

    private Bitmap getBitmapFromUrl(Context context, String url) {
        image = null;
        Picasso picasso = new Picasso.Builder(context).listener(new Picasso.Listener() {
            @Override
            public void onImageLoadFailed(Picasso picasso, Uri uri, Exception exception) {
                Log.e("ERROR", exception.toString());
                Log.e("ERROR", "Picasso - Failed to load image:" + uri);
            }
        }).build();

        try {
            image = picasso.load(Uri.parse(url)).error(R.drawable.logo).get();
        } catch (IOException e) {
            e.printStackTrace();
        }


        return image;
    }
}


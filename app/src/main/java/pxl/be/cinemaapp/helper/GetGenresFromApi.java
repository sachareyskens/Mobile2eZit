package pxl.be.cinemaapp.helper;
import android.os.AsyncTask;
import android.os.Handler;
import android.util.Log;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;

import pxl.be.cinemaapp.configuration.MovieAPIConfig;
import pxl.be.cinemaapp.pojo.Genre;
import pxl.be.cinemaapp.pojo.GenreWrapper;
import static android.content.ContentValues.TAG;
public class GetGenresFromApi extends AsyncTask<Void, Void, Void> {

    @Override
    protected Void doInBackground(Void... params) {
        CustomHttpHandler customHttpHandler = new CustomHttpHandler();
        ObjectMapper mapper = new ObjectMapper();

        String jsonStrGenre = customHttpHandler.makeServiceCall(MovieAPIConfig.MOVIEDB_AP_GENRE);

        if (jsonStrGenre != null) {
            try {
                GenreWrapper genreWrapper = mapper.readValue(jsonStrGenre, GenreWrapper.class);
                Genre.genres = genreWrapper.getGenres();
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            Log.e(TAG, "Couldn't get json from server.");
        }

        return null;
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        super.onPostExecute(aVoid);
    }
}


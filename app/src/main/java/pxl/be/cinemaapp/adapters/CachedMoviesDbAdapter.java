package pxl.be.cinemaapp.adapters;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import java.util.ArrayList;
import java.util.List;

import pxl.be.cinemaapp.helper.CachedMoviesDbHelper;
import pxl.be.cinemaapp.pojo.FeedReaderContract;
import pxl.be.cinemaapp.pojo.Genre;
import pxl.be.cinemaapp.pojo.Movie;

public class CachedMoviesDbAdapter {
    CachedMoviesDbHelper cMDbHelper;

    public CachedMoviesDbAdapter(Context context) {
        cMDbHelper = new CachedMoviesDbHelper(context);
    }

    public long insertData(Movie movie) {
        SQLiteDatabase db = cMDbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(FeedReaderContract.FeedEntry.COLUMN_NAME_ID, movie.getId());
        values.put(FeedReaderContract.FeedEntry.COLUMN_NAME_TITLE, movie.getTitle());
        values.put(FeedReaderContract.FeedEntry.COLUMN_NAME_RELEASEDATE, movie.getReleaseDate());
        values.put(FeedReaderContract.FeedEntry.COLUMN_NAME_OVERVIEW, movie.getOverview());
        values.put(FeedReaderContract.FeedEntry.COLUMN_NAME_POSTER, Movie.convertBitmapByteArray(movie.getPoster()));
        values.put(FeedReaderContract.FeedEntry.COLUMN_NAME_ORIGINAL_TITLE, movie.getOriginalTitle());
        values.put(FeedReaderContract.FeedEntry.COLUMN_NAME_VOTE_AVERAGE, movie.getVoteAverage());
        values.put(FeedReaderContract.FeedEntry.COLUMN_NAME_ORIGINAL_LANGUAGE, movie.getOriginalLanguage());
        int adult = (!movie.getAdult()) ? 0 : 1;
        values.put(FeedReaderContract.FeedEntry.COLUMN_NAME_ADULT, adult);
        values.put(FeedReaderContract.FeedEntry.COLUMN_NAME_BACKDROP, Movie.convertBitmapByteArray(movie.getBackdrop()));
        values.put(FeedReaderContract.FeedEntry.COLUMN_NAME_VOTE_COUNT, movie.getVoteCount());
        values.put(FeedReaderContract.FeedEntry.COLUMN_NAME_VIDEO_URL, movie.getVideoUrl());
        values.put(FeedReaderContract.FeedEntry.COLUMN_NAME_POPULARITY, movie.getPopularity());
        values.put(FeedReaderContract.FeedEntry.COLUMN_NAME_GENRES, movie.getGenresString());
        long id = db.insert(FeedReaderContract.FeedEntry.TABLE_NAME, null, values);
        return id;
    }

    public List<Movie> getData() {
        SQLiteDatabase db = cMDbHelper.getWritableDatabase();
        String[] columns = {FeedReaderContract.FeedEntry.COLUMN_NAME_ID,
                FeedReaderContract.FeedEntry.COLUMN_NAME_TITLE,
                FeedReaderContract.FeedEntry.COLUMN_NAME_RELEASEDATE,
                FeedReaderContract.FeedEntry.COLUMN_NAME_OVERVIEW,
                FeedReaderContract.FeedEntry.COLUMN_NAME_POSTER,
                FeedReaderContract.FeedEntry.COLUMN_NAME_ORIGINAL_TITLE,
                FeedReaderContract.FeedEntry.COLUMN_NAME_VOTE_AVERAGE,
                FeedReaderContract.FeedEntry.COLUMN_NAME_ORIGINAL_LANGUAGE,
                FeedReaderContract.FeedEntry.COLUMN_NAME_ADULT,
                FeedReaderContract.FeedEntry.COLUMN_NAME_BACKDROP,
                FeedReaderContract.FeedEntry.COLUMN_NAME_VOTE_COUNT,
                FeedReaderContract.FeedEntry.COLUMN_NAME_VIDEO_URL,
                FeedReaderContract.FeedEntry.COLUMN_NAME_POPULARITY,
                FeedReaderContract.FeedEntry.COLUMN_NAME_GENRES};
        Cursor cursor = db.query(FeedReaderContract.FeedEntry.TABLE_NAME, columns, null, null, null, null, null);
        List<Movie> movies = new ArrayList<>();
        while (cursor.moveToNext()) {
            Movie movie = new Movie();
            movie.setId(cursor.getInt(cursor.getColumnIndex(FeedReaderContract.FeedEntry.COLUMN_NAME_ID)));
            movie.setTitle(cursor.getString(cursor.getColumnIndex(FeedReaderContract.FeedEntry.COLUMN_NAME_TITLE)));
            movie.setReleaseDate(cursor.getString(cursor.getColumnIndex(FeedReaderContract.FeedEntry.COLUMN_NAME_RELEASEDATE)));
            movie.setOverview(cursor.getString(cursor.getColumnIndex(FeedReaderContract.FeedEntry.COLUMN_NAME_OVERVIEW)));
            byte[] bitmapByteArray = cursor.getBlob(cursor.getColumnIndex(FeedReaderContract.FeedEntry.COLUMN_NAME_POSTER));
            Bitmap bitmap = BitmapFactory.decodeByteArray(bitmapByteArray, 0, bitmapByteArray.length);
            movie.setPoster(bitmap);
            movie.setOriginalTitle(cursor.getString(cursor.getColumnIndex(FeedReaderContract.FeedEntry.COLUMN_NAME_ORIGINAL_TITLE)));
            movie.setVoteAverage(cursor.getDouble(cursor.getColumnIndex(FeedReaderContract.FeedEntry.COLUMN_NAME_VOTE_AVERAGE)));
            movie.setOriginalLanguage(cursor.getString(cursor.getColumnIndex(FeedReaderContract.FeedEntry.COLUMN_NAME_ORIGINAL_LANGUAGE)));
            boolean adult = cursor.getInt(cursor.getColumnIndex(FeedReaderContract.FeedEntry.COLUMN_NAME_ADULT)) != 0;
            movie.setAdult(adult);
            bitmapByteArray = cursor.getBlob(cursor.getColumnIndex(FeedReaderContract.FeedEntry.COLUMN_NAME_BACKDROP));
            bitmap = BitmapFactory.decodeByteArray(bitmapByteArray, 0, bitmapByteArray.length);
            movie.setBackdrop(bitmap);
            movie.setVoteCount(cursor.getInt(cursor.getColumnIndex(FeedReaderContract.FeedEntry.COLUMN_NAME_VOTE_COUNT)));
            movie.setVideoUrl(cursor.getString(cursor.getColumnIndex(FeedReaderContract.FeedEntry.COLUMN_NAME_VIDEO_URL)));
            movie.setPopularity(cursor.getDouble(cursor.getColumnIndex(FeedReaderContract.FeedEntry.COLUMN_NAME_POPULARITY)));
            String genresStr = cursor.getString(cursor.getColumnIndex(FeedReaderContract.FeedEntry.COLUMN_NAME_GENRES));
            movie.setGenres(Genre.convertGenresString(genresStr));

            movie.setCached(true);

            movies.add(movie);
        }
        db.close();
        return movies;
    }

    public int delete(int id)
    {
        SQLiteDatabase db = cMDbHelper.getWritableDatabase();
        String[] whereArgs= {String.valueOf(id)};
        int count = db.delete(FeedReaderContract.FeedEntry.TABLE_NAME ,FeedReaderContract.FeedEntry.COLUMN_NAME_ID+" = ?", whereArgs);
        return  count;
    }
}
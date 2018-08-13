package pxl.be.cinemaapp.helper;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import pxl.be.cinemaapp.pojo.FeedReaderContract;

public class CachedMoviesDbHelper extends SQLiteOpenHelper {

    public static final int DATABASE_VERSION = 3;
    public static final String DATABASE_NAME = "CachedMovies.db";

    public CachedMoviesDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CREATE_ENTRIES);
    }

    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        db.execSQL(SQL_DELETE_ENTRIES);
        onCreate(db);
    }

    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        onUpgrade(db, oldVersion, newVersion);
    }

    private static final String SQL_CREATE_ENTRIES =
            "CREATE TABLE " + FeedReaderContract.FeedEntry.TABLE_NAME + " (" +
                    FeedReaderContract.FeedEntry.COLUMN_NAME_ID + " INTEGER," +
                    FeedReaderContract.FeedEntry.COLUMN_NAME_TITLE + " TEXT," +
                    FeedReaderContract.FeedEntry.COLUMN_NAME_OVERVIEW + " TEXT," +
                    FeedReaderContract.FeedEntry.COLUMN_NAME_POSTER + " BLOB," +
                    FeedReaderContract.FeedEntry.COLUMN_NAME_ORIGINAL_TITLE + " TEXT," +
                    FeedReaderContract.FeedEntry.COLUMN_NAME_VOTE_AVERAGE + " REAL," +
                    FeedReaderContract.FeedEntry.COLUMN_NAME_ORIGINAL_LANGUAGE + " TEXT," +
                    FeedReaderContract.FeedEntry.COLUMN_NAME_ADULT + " INTEGER," +
                    FeedReaderContract.FeedEntry.COLUMN_NAME_BACKDROP + " BLOB," +
                    FeedReaderContract.FeedEntry.COLUMN_NAME_VOTE_COUNT + " INTEGER," +
                    FeedReaderContract.FeedEntry.COLUMN_NAME_VIDEO_URL + " TEXT," +
                    FeedReaderContract.FeedEntry.COLUMN_NAME_POPULARITY + " REAL," +
                    FeedReaderContract.FeedEntry.COLUMN_NAME_RELEASEDATE + " DATE," +
                    FeedReaderContract.FeedEntry.COLUMN_NAME_GENRES + " TEXT)";

    private static final String SQL_DELETE_ENTRIES =
            "DROP TABLE IF EXISTS " + FeedReaderContract.FeedEntry.TABLE_NAME;
}

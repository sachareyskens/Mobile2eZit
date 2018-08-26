package pxl.be.cinemaapp.activities;

import android.app.Notification;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import pxl.be.cinemaapp.R;
import pxl.be.cinemaapp.adapters.CachedMoviesDbAdapter;
import pxl.be.cinemaapp.helper.GetGenresFromApi;
import pxl.be.cinemaapp.pojo.Movie;

public class MainActivity extends AppCompatActivity {
    CallbackManager callbackManager;
    LoginButton loginButton;

    private static final String PREFS_NAME = "prefs";
    private static final String PREF_DARK_THEME = "dark_theme";


    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        SharedPreferences preferences = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        boolean useDarkTheme = preferences.getBoolean(PREF_DARK_THEME, false);

        if (useDarkTheme) {
            setTheme(R.style.AppTheme_Dark_NoActionBar);
        }

        super.onCreate(savedInstanceState);
        FacebookSdk.sdkInitialize(getApplicationContext());
        callbackManager = CallbackManager.Factory.create();
        setContentView(R.layout.activity_main);
        loginButton = (LoginButton) findViewById(R.id.fb_login_button);

        loginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                Log.e("DEBUG", "User ID:  " +
                        loginResult.getAccessToken().getUserId() + "\n" +
                        "Auth Token: " + loginResult.getAccessToken().getToken());
            }

            @Override
            public void onCancel() {
                Log.e("DEBUG", "Login attempt cancelled.");
            }

            @Override
            public void onError(FacebookException e) {
                Log.e("DEBUG", "Login attempt failed.");
            }
        });

        try {
            PackageInfo info = getPackageManager().getPackageInfo(
                    "pxl.be.cinemaapp",
                    PackageManager.GET_SIGNATURES);
            for (Signature signature : info.signatures) {
                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                Log.d("KeyHash:", Base64.encodeToString(md.digest(), Base64.DEFAULT));
            }
        } catch (PackageManager.NameNotFoundException e) {

        } catch (NoSuchAlgorithmException e) {

        }


        new MainGenreGrabber(){}.execute();


    }

    private class MainGenreGrabber extends GetGenresFromApi {
        public MainGenreGrabber() {

        }


        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            CachedMoviesDbAdapter dbAdapter = new CachedMoviesDbAdapter(MainActivity.this);
            List<Movie> movieList = new ArrayList<>();
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
            movieList = dbAdapter.getData();
            for (Movie m : movieList) {
                try {
                    Date date = format.parse(m.getReleaseDate());
                    if (date == new Date()) {
                        Notification n = new Notification.Builder(getApplicationContext())
                                .setContentTitle("Cinema App: A movie you followed has been released")
                                .setContentText(m.getTitle())
                                .setSmallIcon(R.drawable.logo)
                                .build();
                        NotificationManager mNotificationManager =
                                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
                        mNotificationManager.notify(m.getId(), n);

                    }
                } catch (ParseException e) {
                    e.printStackTrace();
                }

            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);
    }

    public void goToTopMovies(View view) {
        Intent intent = new Intent(this, TopActivity.class);
        startActivity(intent);

    }

    public void goToSearch(View view) {
        Intent intent = new Intent(this, SearchActivity.class);
        startActivity(intent);
    }

    public void goToUpcoming(View view) {
        Intent intent = new Intent(this, UpcomingActivity.class);
        startActivity(intent);
    }

    public void goToSettings(View view) {
        Intent intent = new Intent(this, SettingsActivity.class);
        startActivity(intent);
    }

    public void goToFollowed(View view) {
        Intent intent = new Intent(this, FollowingActivity.class);
        startActivity(intent);
    }


}

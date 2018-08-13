package pxl.be.cinemaapp.appwidget;
import android.appwidget.AppWidgetManager;
import android.content.Context;
import android.content.Intent;

import android.os.Bundle;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import pxl.be.cinemaapp.R;

public class MovieViewsFactory implements RemoteViewsService.RemoteViewsFactory {
    private static final String[] items={"a", "b", "c",
            "d", "e", "f",
            "g", "h", "i"}; // Random Seed

    private Context context =null;
    private int appWidgetId;

    public MovieViewsFactory(Context applicationContext, Intent intent) {
        this.context =applicationContext;
        appWidgetId=intent.getIntExtra(AppWidgetManager.EXTRA_APPWIDGET_ID,
                AppWidgetManager.INVALID_APPWIDGET_ID);
    }

    @Override
    public void onCreate() {

    }

    @Override
    public void onDataSetChanged() {

    }

    @Override
    public void onDestroy() {

    }

    @Override
    public int getCount() {
        return 0;
    }

    @Override
    public RemoteViews getViewAt(int position) {
        RemoteViews row=new RemoteViews(context.getPackageName(),
                R.layout.row);

        row.setTextViewText(android.R.id.text1, items[position]);

        Intent i=new Intent();
        Bundle extras=new Bundle();

        extras.putString(FollowingAppWidget.EXTRA_WORD, items[position]);
        i.putExtras(extras);
        row.setOnClickFillInIntent(android.R.id.text1, i);

        return(row);
    }

    @Override
    public RemoteViews getLoadingView() {
        return null;
    }

    @Override
    public int getViewTypeCount() {
        return 0;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }
}


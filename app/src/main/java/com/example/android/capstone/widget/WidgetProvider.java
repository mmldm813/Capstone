package com.example.android.capstone.widget;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.widget.RemoteViews;

import com.example.android.capstone.MyApplication;
import com.example.android.capstone.R;
import com.example.android.capstone.database.AppDatabase;
import com.example.android.capstone.database.AppExecutors;

import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.List;

/**
 * Implementation of App Widget functionality.
 */
public class WidgetProvider extends AppWidgetProvider {
    public static String NEXT_DATE_ACTION = "nextDate";
    public static String PREVIOUS_DATE_ACTION = "previousDate";

    private void updateAppWidget(Context context, final AppWidgetManager appWidgetManager,
                                 final int appWidgetId) {
        final MyApplication myApplication = ((MyApplication) context.getApplicationContext());
        final List<Date> journalEntryDatesForWidget = myApplication.getJournalEntryDatesForWidget();
        final int index = myApplication.getWidgetCurrentDateIndex();

        String dateStr = "NONE";
        if (journalEntryDatesForWidget != null && journalEntryDatesForWidget.size() > 0) {
            dateStr = friendlyDate(journalEntryDatesForWidget.get(index));
        }

        // Construct the RemoteViews object
        RemoteViews remoteViews = new RemoteViews(context.getPackageName(), R.layout.widget_layout);
        remoteViews.setTextViewText(R.id.attempt_date, dateStr);

        final Intent intent = new Intent(context, WidgetService.class);
        intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId);
        remoteViews.setRemoteAdapter(appWidgetId, R.id.widget_exercise_list, intent);
        updateExerciseList(context, appWidgetManager);

        final Intent nextButtonClick = new Intent(context, WidgetProvider.class);
        nextButtonClick.setAction(NEXT_DATE_ACTION);
        final PendingIntent refreshPendingIntentNext = PendingIntent.getBroadcast(context, 0,
                nextButtonClick, PendingIntent.FLAG_UPDATE_CURRENT);
        remoteViews.setOnClickPendingIntent(R.id.widget_next_button, refreshPendingIntentNext);

        final Intent prevButtonClick = new Intent(context, WidgetProvider.class);
        prevButtonClick.setAction(PREVIOUS_DATE_ACTION);
        final PendingIntent refreshPendingIntentPrev = PendingIntent.getBroadcast(context, 0,
                prevButtonClick, PendingIntent.FLAG_UPDATE_CURRENT);
        remoteViews.setOnClickPendingIntent(R.id.widget_previous_button, refreshPendingIntentPrev);

        appWidgetManager.updateAppWidget(appWidgetId, remoteViews);
    }

    private void updateExerciseList(final Context context, final AppWidgetManager appWidgetManager) {
        final MyApplication myApplication = (MyApplication) context.getApplicationContext();
        final List<Date> journalEntryDatesForWidget = myApplication.getJournalEntryDatesForWidget();
        if (journalEntryDatesForWidget != null && journalEntryDatesForWidget.size() > 0) {
            final AppDatabase db = AppDatabase.getInstance(myApplication);
            final int index = myApplication.getWidgetCurrentDateIndex();

            AppExecutors.getInstance().diskIO().execute(new Runnable() {
                @Override
                public void run() {
                    Date date = journalEntryDatesForWidget.get(index);
                    myApplication.setJournalExercisesForWidget(db.journalDao().loadJournalEntryAndExerciseByDate(date));

                    ComponentName cn = new ComponentName(context, WidgetProvider.class);
                    for (int appWidgetId : appWidgetManager.getAppWidgetIds(cn)) {
                        appWidgetManager.notifyAppWidgetViewDataChanged(appWidgetId, 0);
                    }
                }
            });
        }
    }

    private String friendlyDate(Date date) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("MMM-dd-yyyy hh:mm aaa");
        return simpleDateFormat.format(date);
    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        // There may be multiple widgets active, so update all of them
        for (int appWidgetId : appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetId);
        }
    }

    @Override
    public void onEnabled(Context context) {
        readFromDatabase(context);
    }

    private void readFromDatabase(final Context context) {
        final MyApplication myApplication = (MyApplication) context.getApplicationContext();
        final AppDatabase db = AppDatabase.getInstance(myApplication);
        AppExecutors.getInstance().diskIO().execute(new Runnable() {
            @Override
            public void run() {
                myApplication.setJournalEntryDatesForWidget(db.journalDao().loadJournalDatesForWidget());
            }
        });
    }

    @Override
    public void onDisabled(Context context) {
        // Enter relevant functionality for when the last widget is disabled
    }
    @Override
    public void onReceive(Context context, Intent intent) {
        final String action = intent.getAction();
        if (action.equals(NEXT_DATE_ACTION) || action.equals(PREVIOUS_DATE_ACTION)) {

            MyApplication myApplication = ((MyApplication) context.getApplicationContext());
            List<Date> journalEntryDatesForWidget = myApplication.getJournalEntryDatesForWidget();
            if (journalEntryDatesForWidget != null && journalEntryDatesForWidget.size() > 0) {
                int newIndex = myApplication.getWidgetCurrentDateIndex();

                if (action.equals(NEXT_DATE_ACTION)) {
                    if (newIndex == journalEntryDatesForWidget.size() - 1) {
                        return;
                    }
                    newIndex++;
                } else if (action.equals(PREVIOUS_DATE_ACTION)) {
                    if (newIndex == 0) {
                        return;
                    }
                    newIndex--;
                }

                myApplication.setWidgetCurrentDateIndex(newIndex);
                updateWidget(context);
            }
        }

        super.onReceive(context, intent);
    }

    private void updateWidget(Context context) {
        MyApplication myApplication = ((MyApplication) context.getApplicationContext());

        AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(context);
        List<Date> journalEntryDatesForWidget = myApplication.getJournalEntryDatesForWidget();
        int currentDateIndex = myApplication.getWidgetCurrentDateIndex();

        RemoteViews remoteViews = new RemoteViews(context.getPackageName(), R.layout.widget_layout);
        remoteViews.setTextViewText(R.id.attempt_date,
                friendlyDate(journalEntryDatesForWidget.get(currentDateIndex)));
        updateExerciseList(context, appWidgetManager);
    }
}


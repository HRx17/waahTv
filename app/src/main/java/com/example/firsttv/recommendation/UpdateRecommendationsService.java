/*
 * Copyright (C) 2015 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.example.firsttv.recommendation;

import android.app.IntentService;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.example.firsttv.R;
import com.example.firsttv.data.VideoProvider;
import com.example.firsttv.model.Movie;
import com.example.firsttv.ui.SeriesDetailsActivity;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/*
 * This class builds up to MAX_RECOMMMENDATIONS of recommendations and defines what happens
 * when they're clicked from Recommendations section on Home screen
 */
public class UpdateRecommendationsService extends IntentService {
    private static final String TAG = "RecommendationsService";
    private static final int MAX_RECOMMENDATIONS = 3;

    private static final int CARD_WIDTH = 313;
    private static final int CARD_HEIGHT = 176;

    private NotificationManager mNotificationManager;

    public UpdateRecommendationsService() {
        super(TAG);
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        Log.d(TAG, "Updating recommendation cards");
        HashMap<Object, List<List>> recommendations = VideoProvider.getMovieList();
        if (recommendations == null) {
            return;
        }

        if (mNotificationManager == null) {
            mNotificationManager = (NotificationManager) getApplicationContext()
                    .getSystemService(Context.NOTIFICATION_SERVICE);
        }

        RecommendationBuilder builder = new RecommendationBuilder()
                .setContext(getApplicationContext())
                .setSmallIcon(R.drawable.videos_by_google_icon);

        // flatten to list
        List flattenedRecommendations = new ArrayList();
        for (Map.Entry<Object, List<List>> entry : recommendations.entrySet()) {
            for (List movie : entry.getValue()) {
               // Log.d(TAG, "Recommendation - " + movie());
                flattenedRecommendations.add(movie);
            }
        }

        Collections.shuffle(flattenedRecommendations);
        Movie movie;
        for (int i = 0; i < flattenedRecommendations.size() && i < MAX_RECOMMENDATIONS; i++) {
            movie = (Movie) flattenedRecommendations.get(i);
            final RecommendationBuilder notificationBuilder = builder
                    .setBackground(movie.getCardImageUrl())
                    .setId(i+1)
                    .setPriority(MAX_RECOMMENDATIONS - i - 1)
                    .setTitle(movie.getTitle())
                    .setDescription(getString(R.string.popular_header))
                    .setIntent(buildPendingIntent(movie, i + 1));
        }
    }

    private PendingIntent buildPendingIntent(Movie movie, int id) {
        Intent detailsIntent = new Intent(this, SeriesDetailsActivity.class);
        detailsIntent.putExtra(SeriesDetailsActivity.MOVIE, movie);

        TaskStackBuilder stackBuilder = TaskStackBuilder.create(this);
        stackBuilder.addParentStack(SeriesDetailsActivity.class);
        stackBuilder.addNextIntent(detailsIntent);
        // Ensure a unique PendingIntents, otherwise all recommendations end up with the same
        // PendingIntent

        return stackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);
    }
}

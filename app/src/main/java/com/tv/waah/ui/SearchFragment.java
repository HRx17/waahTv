/*
 * Copyright (C) 2014 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License
 * is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express
 * or implied. See the License for the specific language governing permissions and limitations under
 * the License.
 */

package com.tv.waah.ui;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

import androidx.core.app.ActivityOptionsCompat;
import androidx.leanback.app.SearchSupportFragment;
import androidx.leanback.widget.ArrayObjectAdapter;
import androidx.leanback.widget.HeaderItem;
import androidx.leanback.widget.ImageCardView;
import androidx.leanback.widget.ListRow;
import androidx.leanback.widget.ListRowPresenter;
import androidx.leanback.widget.ObjectAdapter;
import androidx.leanback.widget.OnItemViewClickedListener;
import androidx.leanback.widget.Presenter;
import androidx.leanback.widget.Row;
import androidx.leanback.widget.RowPresenter;
import androidx.leanback.widget.SpeechRecognitionCallback;

import com.tv.waah.BuildConfig;
import com.tv.waah.R;
import com.tv.waah.model.Movie;
import com.tv.waah.presenter.CardPresenter;

import java.util.ArrayList;
import java.util.List;

/*
 * This class demonstrates how to do in-app search
 * Big Error fixing needed!
 */
public class SearchFragment extends androidx.leanback.app.SearchFragment
        implements SearchSupportFragment.SearchResultProvider {
    private static final String TAG = "SearchFragment";
    private static final boolean DEBUG = true && BuildConfig.DEBUG;
    private static final boolean FINISH_ON_RECOGNIZER_CANCELED = true;
    private static final int REQUEST_SPEECH = 0x00000010;
    private static final long SEARCH_DELAY_MS = 1000L;

    private final Handler mHandler = new Handler();
    private final Runnable mDelayedLoad = new Runnable() {
        @Override
        public void run() {
            loadRows();
        }
    };
    private ArrayObjectAdapter mRowsAdapter;
    private String mQuery;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mRowsAdapter = new ArrayObjectAdapter(new ListRowPresenter());
        setOnItemViewClickedListener(new ItemViewClickedListener());
        if (!hasPermission(Manifest.permission.RECORD_AUDIO)) {
            // SpeechRecognitionCallback is not required and if not provided recognition will be handled
            // using internal speech recognizer, in which case you must have RECORD_AUDIO permission
            setSpeechRecognitionCallback(new SpeechRecognitionCallback() {
                @Override
                public void recognizeSpeech() {
                    if (DEBUG) Log.v(TAG, "recognizeSpeech");
                    try {
                        startActivityForResult(getRecognizerIntent(), REQUEST_SPEECH);
                    } catch (ActivityNotFoundException e) {
                        Log.e(TAG, "Cannot find activity for speech recognizer", e);
                    }
                }
            });
        }
    }

    private void setOnItemViewClickedListener(ItemViewClickedListener itemViewClickedListener) {
    }

    @Override
    public void onPause() {
        mHandler.removeCallbacksAndMessages(null);
        super.onPause();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (DEBUG) Log.v(TAG, "onActivityResult requestCode=" + requestCode +
                " resultCode=" + resultCode +
                " data=" + data);
        switch (requestCode) {
            case REQUEST_SPEECH:
                switch (resultCode) {
                    case Activity.RESULT_OK:
                        setSearchQuery(data, true);
                        break;
                    case Activity.RESULT_CANCELED:
                        // Once recognizer canceled, user expects the current activity to process
                        // the same BACK press as user doesn't know about overlay activity.
                        // However, you may not want this behaviour as it makes harder to
                        // fall back to keyboard input.
                        if (FINISH_ON_RECOGNIZER_CANCELED) {
                            if (!hasResults()) {
                                if (DEBUG) Log.v(TAG, "Delegating BACK press from recognizer");
                                getActivity().onBackPressed();
                            }
                        }
                        break;
                    // the rest includes various recognizer errors, see {@link RecognizerIntent}
                }
                break;
        }
    }
    @Override
    public ObjectAdapter getResultsAdapter() {
        return mRowsAdapter;
    }

    @Override
    public boolean onQueryTextChange(String newQuery) {
        Log.i(TAG, String.format("Search Query Text Change %s", newQuery));
        loadQuery(newQuery);
        return true;
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        Log.i(TAG, String.format("Search Query Text Submit %s", query));
        loadQuery(query);
        return true;
    }

    public boolean hasResults() {
        return mRowsAdapter.size() > 0;
    }

    private boolean hasPermission(final String permission) {
        final Context context = getActivity();
        return PackageManager.PERMISSION_GRANTED == context.getPackageManager().checkPermission(
                permission, context.getPackageName());
    }

    private void loadQuery(String query) {
        mHandler.removeCallbacks(mDelayedLoad);
        if (!TextUtils.isEmpty(query) && !query.equals("nil")) {
            mQuery = query;
            mHandler.postDelayed(mDelayedLoad, SEARCH_DELAY_MS);
        }
    }

    private void loadRows() {
        // offload processing from the UI thread
        new AsyncTask<String, Void, ListRow>() {
            private final String query = mQuery;

            @Override
            protected void onPreExecute() {
                mRowsAdapter.clear();
            }

            @SuppressLint("StaticFieldLeak")
            @Override
            protected ListRow doInBackground(String... params) {
                final List<List> result = new ArrayList<>();

                ArrayObjectAdapter listRowAdapter = new ArrayObjectAdapter(new CardPresenter());
                listRowAdapter.addAll(0, result);
                HeaderItem header = new HeaderItem(getString(R.string.search_results, query));
                return new ListRow(header, listRowAdapter);
            }

            @Override
            protected void onPostExecute(ListRow listRow) {
                mRowsAdapter.add(listRow);
            }
        }.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
    }

    private final class ItemViewClickedListener implements OnItemViewClickedListener {
        @Override
        public void onItemClicked(Presenter.ViewHolder itemViewHolder, Object item,
                                  RowPresenter.ViewHolder rowViewHolder, Row row) {

            if (item instanceof Movie) {
                Movie movie = (Movie) item;
                Log.d(TAG, "Movie: " + movie.toString());
                Intent intent = new Intent(getActivity(), SeriesDetailsActivity.class);

                Bundle bundle = ActivityOptionsCompat.makeSceneTransitionAnimation(
                        getActivity(),
                        ((ImageCardView) itemViewHolder.view).getMainImageView(),
                        SeriesDetailsActivity.SHARED_ELEMENT_NAME).toBundle();
                getActivity().startActivity(intent, bundle);
            } else {
                Toast.makeText(getActivity(), ((String) item), Toast.LENGTH_SHORT)
                        .show();
            }
        }
    }


}

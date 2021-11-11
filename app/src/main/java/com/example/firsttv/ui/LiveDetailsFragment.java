package com.example.firsttv.ui;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.leanback.app.BackgroundManager;
import androidx.leanback.app.BrowseSupportFragment;
import androidx.leanback.widget.ArrayObjectAdapter;
import androidx.leanback.widget.ListRow;
import androidx.leanback.widget.ListRowPresenter;
import androidx.leanback.widget.OnItemViewClickedListener;
import androidx.leanback.widget.OnItemViewSelectedListener;
import androidx.leanback.widget.Presenter;
import androidx.leanback.widget.Row;
import androidx.leanback.widget.RowPresenter;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;
import com.example.firsttv.R;
import com.example.firsttv.RetrofitFiles.JsonPlaceHolderApi;
import com.example.firsttv.RetrofitFiles.RetrofitClient;
import com.example.firsttv.model.ChannelList;
import com.example.firsttv.model.Live;
import com.example.firsttv.model.SubPost;
import com.example.firsttv. presenter.LiveCatPresenter;

import java.util.List;
import java.util.Objects;
import java.util.Timer;
import java.util.TimerTask;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LiveDetailsFragment extends BrowseSupportFragment {
    private static final String TAG = "LiveDetailsFragment";

    private static final int BACKGROUND_UPDATE_DELAY = 300;
    private static final int GRID_ITEM_WIDTH = 200;
    private static final int GRID_ITEM_HEIGHT = 180;
    // private static final int NUM_ROWS = 6;
    //private static final int NUM_COLS = 15;

    private final Handler mHandler = new Handler();
    private Drawable mDefaultBackground;
    private DisplayMetrics mMetrics;
    private Timer mBackgroundTimer;
    private int mBackgroundUri;
    private BackgroundManager mBackgroundManager;

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        Log.i(TAG, "onCreate");
        super.onActivityCreated(savedInstanceState);

        //mBackgroundManager.setDrawable(ContextCompat.getDrawable(requireActivity(), R.drawable.default_background));

        prepareBackgroundManager();

        setupUIElements();

        loadRows();

        setupEventListeners();

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (null != mBackgroundTimer) {
            Log.d(TAG, "onDestroy: " + mBackgroundTimer.toString());
            mBackgroundTimer.cancel();
        }
    }

    private void loadRows() {

        ArrayObjectAdapter rowsAdapter = new ArrayObjectAdapter(new ListRowPresenter());
        LiveCatPresenter liveCatPresenter = new LiveCatPresenter();

        ArrayObjectAdapter listRowAdapter1 = new ArrayObjectAdapter(liveCatPresenter);

        RetrofitClient retrofitClient = new RetrofitClient();
        JsonPlaceHolderApi jsonPlaceHolderApi = retrofitClient.getInstance().getApi();
        Call<SubPost> call = jsonPlaceHolderApi.getSubPosts("a@a.com",LiveDetail.LIVE);
        call.enqueue(new Callback<SubPost>() {
            @Override
            public void onResponse(Call<SubPost> call, Response<SubPost> response) {
                if(!response.isSuccessful()) {
                    Toast.makeText(getActivity(), "Failed to get Response!", Toast.LENGTH_SHORT).show();
                    return;
                }
                List<ChannelList> posts = Objects.requireNonNull(response.body()).getChannelList();
                System.out.println(posts);
                for (ChannelList post : posts) {
                    if (post == null) {
                        Toast.makeText(getActivity(), response.code(), Toast.LENGTH_SHORT).show();
                        return;
                    }
                    Live list1 = new Live();
                    list1.setTitle(post.getChannelName());
                    list1.setLiveImageUrl(post.getChannellogo());
                    list1.setId(post.getChannelurl());
                    list1.setCategory(post.getChannelName());
                    list1.setSeasons(post.getSeasons());
                    listRowAdapter1.add(list1);
                }
            }

            @Override
            public void onFailure(Call<SubPost> call, Throwable t) {
                Toast.makeText(getActivity(), t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

        //HeaderItem header = new HeaderItem(LiveDetail.LIVE);
        rowsAdapter.add(new ListRow(listRowAdapter1));
        setAdapter(rowsAdapter);

    }

    private void prepareBackgroundManager() {

        mBackgroundManager = BackgroundManager.getInstance(getActivity());
        mBackgroundManager.attach(getActivity().getWindow());
        mBackgroundManager.setDrawable(ContextCompat.getDrawable(requireActivity(), R.drawable.images));

        mDefaultBackground = ContextCompat.getDrawable(getActivity(), R.color.background_gradient_end);
        mMetrics = new DisplayMetrics();
        getActivity().getWindowManager().getDefaultDisplay().getMetrics(mMetrics);
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    private void setupUIElements() {
        setTitle(LiveDetail.LIVE);
        // Badge, when set, takes precedent
        // over title
        setHeadersState(HEADERS_ENABLED);
        setHeadersTransitionOnBackEnabled(false);
        // set fastLane (or headers) background color
        setBrandColor(ContextCompat.getColor(requireActivity(), R.color.default_background));
        // set search icon color
        setSearchAffordanceColor(ContextCompat.getColor(getActivity(), R.color.default_background));
    }

    private void setupEventListeners() {
        setOnSearchClickedListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                Toast.makeText(getActivity(), "Implement your own in-app search", Toast.LENGTH_LONG)
                        .show();
            }
        });

        setOnItemViewClickedListener(new ItemViewClickedListener());
        setOnItemViewSelectedListener(new ItemViewSelectedListener());
    }

    private void updateBackground(int uri) {
        int width = mMetrics.widthPixels;
        int height = mMetrics.heightPixels;
        Glide.with(getActivity())
                .load(uri)
                .centerCrop()
                .error(mDefaultBackground)
                .into(new SimpleTarget<Drawable>(width, height) {
                    @Override
                    public void onResourceReady(@NonNull Drawable drawable,
                                                @Nullable Transition<? super Drawable> transition) {
                        mBackgroundManager.setDrawable(drawable);
                    }
                });
        mBackgroundTimer.cancel();
    }

    private void startBackgroundTimer() {
        if (null != mBackgroundTimer) {
            mBackgroundTimer.cancel();
        }
        mBackgroundTimer = new Timer();
        mBackgroundTimer.schedule(new UpdateBackgroundTask(), BACKGROUND_UPDATE_DELAY);
    }

    private final class ItemViewClickedListener implements OnItemViewClickedListener {
        @Override
        public void onItemClicked(Presenter.ViewHolder itemViewHolder, Object item,
                                  RowPresenter.ViewHolder rowViewHolder, Row row) {
            if(item instanceof  Live && ((Live) item).seasons.isEmpty()){
                Live live = (Live) item;
                PlaybackActivity.URLL = live.getId();
                PlaybackActivity.NAME = live.getTitle();
                Intent intent = new Intent(getActivity(), PlaybackActivity.class);
                requireActivity().startActivity(intent);
            }
            else{
                Live live = (Live) item;
                SeriesDetailFragment.SERIESNAME = live.getTitle();
                Intent intent = new Intent(getActivity(), SeriesDetailsActivity.class);
                //intent.putExtra(String.valueOf(SeriesDetailFragment.SERIES), String.valueOf(((Live) item).seasons));
                requireActivity().startActivity(intent);
            }
        }
    }

    private final class ItemViewSelectedListener implements OnItemViewSelectedListener {
        @Override
        public void onItemSelected(
                Presenter.ViewHolder itemViewHolder,
                Object item,
                RowPresenter.ViewHolder rowViewHolder,
                Row row) {
            if(item instanceof Live) {
                mBackgroundUri = R.drawable.images;
                startBackgroundTimer();
            }
        }
    }

    private class UpdateBackgroundTask extends TimerTask {

        @Override
        public void run() {
            mHandler.post(new Runnable() {
                @Override
                public void run() {
                    updateBackground(mBackgroundUri);
                }
            });
        }
    }

    private class GridItemPresenter extends Presenter {
        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent) {
            TextView view = new TextView(parent.getContext());
            view.setLayoutParams(new ViewGroup.LayoutParams(GRID_ITEM_WIDTH, GRID_ITEM_HEIGHT));
            view.setFocusable(true);
            view.setFocusableInTouchMode(true);
            view.setBackgroundColor(
                    ContextCompat.getColor(getActivity(), R.color.default_background));
            view.setTextColor(Color.WHITE);
            view.setGravity(Gravity.CENTER);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(ViewHolder viewHolder, Object item) {
            ((TextView) viewHolder.view).setText((String) item);
        }

        @Override
        public void onUnbindViewHolder(ViewHolder viewHolder) {
        }
    }

}
package com.tv.waah.ui;

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
import android.widget.ProgressBar;
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
import com.tv.waah.R;
import com.tv.waah.RetrofitFiles.JsonPlaceHolderApi;
import com.tv.waah.RetrofitFiles.RetrofitClient;
import com.tv.waah.Utils;
import com.tv.waah.model.ChannelList;
import com.tv.waah.model.Live;
import com.tv.waah.model.SubPost;
import com.tv.waah.presenter.LiveCatPresenter;

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
    private static final int GRID_ITEM_WIDTH = 250;
    private static final int GRID_ITEM_HEIGHT = 210;
    // private static final int NUM_ROWS = 6;
    //private static final int NUM_COLS = 15;
    ProgressBar progressBar;

    private final Handler mHandler = new Handler();
    private Drawable mDefaultBackground;
    private DisplayMetrics mMetrics;
    private Timer mBackgroundTimer;
    private int mBackgroundUri;
    private BackgroundManager mBackgroundManager;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        Log.i(TAG, "onCreate");
        super.onCreate(savedInstanceState);

        progressBar = getActivity().findViewById(R.id.progresswait);
        progressBar.setVisibility(View.VISIBLE);
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

        //BaseGridView baseGridView = new HorizontalGridView(getContext());
        ArrayObjectAdapter rowsAdapter = new ArrayObjectAdapter(new ListRowPresenter());
        LiveCatPresenter liveCatPresenter = new LiveCatPresenter();

        RetrofitClient retrofitClient = new RetrofitClient();
        JsonPlaceHolderApi jsonPlaceHolderApi = RetrofitClient.getInstance().getApi();
        Call<SubPost> call = jsonPlaceHolderApi.getSubPosts(Utils.getEncryptedDeviceId(getActivity().getApplicationContext()),MainActivity.USER,LiveDetail.LIVE);
        call.enqueue(new Callback<SubPost>() {
            @Override
            public void onResponse(Call<SubPost> call, Response<SubPost> response) {
                if(!response.isSuccessful()) {
                    Toast.makeText(getActivity(), "Server is down, please try after sometime!", Toast.LENGTH_SHORT).show();
                    getActivity().finish();
                    return;
                }
                List<ChannelList> posts = Objects.requireNonNull(response.body()).getChannelList();
                if (posts==null){
                    Toast.makeText(getContext(), "Nothing Added yet!", Toast.LENGTH_SHORT).show();
                }
                ArrayObjectAdapter listrowadapter2 = new ArrayObjectAdapter(liveCatPresenter);
                ArrayObjectAdapter listrowadapter3 = new ArrayObjectAdapter(liveCatPresenter);
                assert posts != null;
                int ln = posts.size();
                int i=0;
                int m=0;
                 while(i<ln) {
                     int k = 1;
                     ArrayObjectAdapter listRowAdapter = new ArrayObjectAdapter(liveCatPresenter);
                     for (ChannelList post : posts) {
                         if (post == null) {
                             Toast.makeText(getActivity(), response.message(), Toast.LENGTH_SHORT).show();
                             return;
                         } else if (k > i * 5 && k <= (i + 1) * 5) {
                             Live list1 = new Live();
                             list1.setTitle(post.getChannelName());
                             list1.setLiveImageUrl(post.getChannellogo());
                             list1.setId(post.getChannelurl());
                             list1.setCategory(post.getChannelName());
                             list1.setSeasons(post.getSeasons());
                             listRowAdapter.add(list1);
                             progressBar.setVisibility(View.GONE);
                         }
                         k++;
                     }
                     i++;
                     if (listRowAdapter.size() == 0) {
                         return;
                     } else {
                         rowsAdapter.add(new ListRow(listRowAdapter));
                     }
                 }
            }

            @Override
            public void onFailure(Call<SubPost> call, Throwable t) {
                Toast.makeText(getContext(), t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
        //HeaderItem header = new HeaderItem(LiveDetail.LIVE);
        setAdapter(rowsAdapter);

    }

    private void prepareBackgroundManager() {

        mBackgroundManager = BackgroundManager.getInstance(getActivity());
        mBackgroundManager.attach(getActivity().getWindow());
        mBackgroundManager.setDrawable(ContextCompat.getDrawable(requireActivity(), R.mipmap.images));

        mDefaultBackground = ContextCompat.getDrawable(getActivity(), R.color.background_gradient_end);
        mMetrics = new DisplayMetrics();
        getActivity().getWindowManager().getDefaultDisplay().getMetrics(mMetrics);
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    private void setupUIElements() {
        setTitle(LiveDetail.SHARED_ELEMENT_NAME);
        setHeadersState(HEADERS_DISABLED);
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
                Intent intent = new Intent(getContext(),MainActivity.class);
                startActivity(intent);
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
                getActivity().finish();
            }
            else{
                Live live = (Live) item;
                SeriesDetailFragment.SERIESNAME = live.getTitle();
                Intent intent = new Intent(getActivity(), SeriesDetailsActivity.class);
                requireActivity().startActivity(intent);
                getActivity().finish();
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
                mBackgroundUri = R.mipmap.images;
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
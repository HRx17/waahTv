package com.tv.waah.ui;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
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
import androidx.leanback.widget.PresenterSelector;
import androidx.leanback.widget.Row;
import androidx.leanback.widget.RowPresenter;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;
import com.tv.waah.R;
import com.tv.waah.RetrofitFiles.JsonPlaceHolderApi;
import com.tv.waah.RetrofitFiles.RetrofitClient;
import com.tv.waah.Utils;
import com.tv.waah.data.MyHeaderList;
import com.tv.waah.model.HeaderItemModel;
import com.tv.waah.model.Live;
import com.tv.waah.model.Post;
import com.tv.waah.presenter.IconHeaderItemPresenter;
import com.tv.waah.presenter.LiveCatPresenter;

import java.io.IOException;
import java.util.List;
import java.util.Objects;
import java.util.Timer;
import java.util.TimerTask;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainFragment extends BrowseSupportFragment {

    private static final String TAG = "MainFragment";

    private static final int BACKGROUND_UPDATE_DELAY = 200;
    private static final int GRID_ITEM_WIDTH = 300;
    private static final int GRID_ITEM_HEIGHT = 280;
   // private static final int NUM_ROWS = 6;
    //private static final int NUM_COLS = 15;

    ProgressBar progressBar;
    private final Handler mHandler = new Handler();
    private Drawable mDefaultBackground;
    private DisplayMetrics mMetrics;
    private Timer mBackgroundTimer;
    private int mBackgroundUri;
    private BackgroundManager mBackgroundManager;
    ListRowPresenter listRowPresenter = new ListRowPresenter();

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        Log.i(TAG, "onCreate");
        super.onActivityCreated(savedInstanceState);

        progressBar = getActivity().findViewById(R.id.progresswait);
        progressBar.setVisibility(View.VISIBLE);

       // mBackgroundManager.setDrawable(ContextCompat.getDrawable(requireActivity(), R.drawable.default_background));

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

        ArrayObjectAdapter liveRowAdapter = new ArrayObjectAdapter(liveCatPresenter);
        ArrayObjectAdapter moviesRowAdapter = new ArrayObjectAdapter(liveCatPresenter);
        ArrayObjectAdapter seriesRowAdapter = new ArrayObjectAdapter(liveCatPresenter);
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(RetrofitClient.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        JsonPlaceHolderApi jsonPlaceHolderApi = retrofit.create(JsonPlaceHolderApi.class);
        String encryptedDeviceId = Utils.getEncryptedDeviceId(getActivity().getApplicationContext());
        Call<List<Post>> call = jsonPlaceHolderApi.getPosts(encryptedDeviceId,MainActivity.USER);
        call.enqueue(new Callback<List<Post>>() {
            @Override
            public void onResponse(Call<List<Post>> call, retrofit2.Response<List<Post>> response) {

                if(!response.isSuccessful()){
                    String msg = null;
                    try {
                        msg = response.errorBody().string();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    Toast.makeText(getActivity(), msg, Toast.LENGTH_SHORT).show();
                    return;
                }
                List<Post> posts = response.body();
                for(Post post : Objects.requireNonNull(posts)){
                    if(post.getDisplayid() == null){
                        Toast.makeText(getContext(), response.message(), Toast.LENGTH_SHORT).show();
                        return;
                    }
                    else if(post.getCategoryType().equals("LIVE")){
                        Live liveItem = new Live();
                        liveItem.setTitle(post.getDisplayName());
                        liveItem.setLiveImageUrl(String.valueOf(post.getIconUrl()));
                        liveItem.setId(post.getDisplayid());
                        liveItem.setCategory(post.getCategoryType());
                        liveRowAdapter.add(liveItem);
                        progressBar.setVisibility(View.GONE);
                    }
                    else if(post.getCategoryType().equals("MOVIES")){
                        Live movieItem = new Live();
                        movieItem.setTitle(post.getDisplayName());
                        movieItem.setLiveImageUrl(String.valueOf(post.getIconUrl()));
                        movieItem.setId(post.getDisplayid());
                        movieItem.setCategory(String.valueOf(post.getCategoryType()));
                        moviesRowAdapter.add(movieItem);
                    }
                    else if(post.getCategoryType().equals("SERIES")){
                        Live series = new Live();
                        series.setTitle(post.getDisplayName());
                        series.setLiveImageUrl(String.valueOf(post.getIconUrl()));
                        series.setId(post.getDisplayid());
                        series.setCategory(String.valueOf(post.getCategoryType()));
                        seriesRowAdapter.add(series);
                    }

                }
            }

            @Override
            public void onFailure(Call<List<Post>> call, Throwable t) {
                Toast.makeText(getActivity(), t.getMessage() , Toast.LENGTH_SHORT).show();
                return;
            }
        });
        int k=0;
        HeaderItemModel header = new HeaderItemModel(k,MyHeaderList.HEADER_CATEGORY[k],MyHeaderList.HEADER_img[k]);
        rowsAdapter.add(new ListRow(header, liveRowAdapter));
        header = new HeaderItemModel(k+1,MyHeaderList.HEADER_CATEGORY[k+1],MyHeaderList.HEADER_img[k+1]);
        rowsAdapter.add(new ListRow(header, moviesRowAdapter));
        header = new HeaderItemModel(k+2,MyHeaderList.HEADER_CATEGORY[k+2],MyHeaderList.HEADER_img[k+2]);
        rowsAdapter.add(new ListRow(header, seriesRowAdapter));
        setAdapter(rowsAdapter);
    }

    private void prepareBackgroundManager() {

        mBackgroundManager = BackgroundManager.getInstance(requireActivity());
        mBackgroundManager.attach(requireActivity().getWindow());
        mBackgroundManager.setDrawable(ContextCompat.getDrawable(requireActivity(), R.mipmap.images));

        mDefaultBackground = ContextCompat.getDrawable(requireActivity(), R.mipmap.images);
        mMetrics = new DisplayMetrics();
        getActivity().getWindowManager().getDefaultDisplay().getMetrics(mMetrics);
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    private void setupUIElements() {
//        searchOrbView.getContext().getResources().getAssets();
//        searchOrbView.setOrbIcon(null);
        setBadgeDrawable(requireActivity().getResources().getDrawable(R.drawable.waahtv_app));
        setTitle(getString(R.string.browse_title));
        // Badge, when set, takes precedent
        // over title
        setHeadersState(HEADERS_ENABLED);
        setHeadersTransitionOnBackEnabled(true);
        setHeaderPresenterSelector(new PresenterSelector() {
            @Override
            public Presenter getPresenter(Object o) {
                return new IconHeaderItemPresenter();
            }
        });
        listRowPresenter.setShadowEnabled(true);

        // set fastLane (or headers) background color
        setBrandColor(ContextCompat.getColor(requireActivity(), R.color.default_background));
        // set search icon color
        setSearchAffordanceColor(ContextCompat.getColor(getActivity(), R.color.default_background));
    }

    private void setupEventListeners() {
        setOnSearchClickedListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(),SettingsActivity.class);
                startActivity(intent);
                getActivity().finish();
            }
        });

        setOnItemViewClickedListener(new ItemViewClickedListener());
        setOnItemViewSelectedListener(new ItemViewSelectedListener());
    }

    private void updateBackground(int uri) {
        int width = mMetrics.widthPixels;
        int height = mMetrics.heightPixels;
        Glide.with(requireActivity())
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
            if(item instanceof  Live){
                Live live = (Live) item;
                Intent intent = new Intent(getActivity(), LiveDetail.class);
                LiveDetail.LIVE = live.getId();
                LiveDetail.SHARED_ELEMENT_NAME = live.getTitle();
                LiveDetail.NOTIFICATION_ID = live.getCategory();
                requireActivity().startActivity(intent);
                getActivity().finish();
            }
            else if (item instanceof String) {
                Toast.makeText(getActivity(), ((String) item), Toast.LENGTH_SHORT).show();
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
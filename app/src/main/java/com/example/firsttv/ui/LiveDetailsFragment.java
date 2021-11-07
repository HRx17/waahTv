package com.example.firsttv.ui;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
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
import com.example.firsttv.JsonPlaceHolderApi;
import com.example.firsttv.Post;
import com.example.firsttv.R;
import com.example.firsttv.model.Live;
import com.example.firsttv.presenter.LiveCatPresenter;

import java.util.List;
import java.util.Objects;
import java.util.Timer;
import java.util.TimerTask;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class LiveDetailsFragment extends BrowseSupportFragment {
    private static final String TAG = "MainFragment";

    private static final int BACKGROUND_UPDATE_DELAY = 300;
    private final Handler mHandler = new Handler();
    private Drawable mDefaultBackground;
    private DisplayMetrics mMetrics;
    private Timer mBackgroundTimer;
    private String mBackgroundUri;
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
       // List<Movie> list = MovieList.setupMovies();
        ArrayObjectAdapter rowsAdapter = new ArrayObjectAdapter(new ListRowPresenter());
        //CardPresenter cardPresenter = new CardPresenter();
        LiveCatPresenter liveCatPresenter = new LiveCatPresenter();

       // int i=0;
        ArrayObjectAdapter listRowAdapter1 = new ArrayObjectAdapter(liveCatPresenter);
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://parrot-tv.azurewebsites.net/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        JsonPlaceHolderApi jsonPlaceHolderApi = retrofit.create(JsonPlaceHolderApi.class);
        Call<List<Post>> call = jsonPlaceHolderApi.getPosts();
        call.enqueue(new Callback<List<Post>>() {
            @Override
            public void onResponse(Call<List<Post>> call, retrofit2.Response<List<Post>> response) {

               // int img[] = {R.drawable.news,R.drawable.music,R.drawable.entertainement,R.drawable.sports};
                if(!response.isSuccessful()){
                    Toast.makeText(getActivity(), "Failed to get response!", Toast.LENGTH_SHORT).show();
                    return;
                }
                List<Post> posts = response.body();
                //int k =0;
                Toast.makeText(getActivity(), "Got response!", Toast.LENGTH_SHORT).show();
                for(Post post : Objects.requireNonNull(posts)){
                    if(post.getDisplayid() == null){
                        Toast.makeText(getContext(), response.message(), Toast.LENGTH_SHORT).show();
                        return;
                    }
                    Live list1 = new Live();
                    list1.setTitle(post.getDisplayid());
                    list1.setLiveImageUrl(String.valueOf(post.getIconUrl()));
                    list1.setId(post.getDisplayid());
                    list1.setCategory(String.valueOf(post.getCategoryType()));
                    listRowAdapter1.add(list1);

                }

            }

            @Override
            public void onFailure(Call<List<Post>> call, Throwable t) {
                Toast.makeText(getActivity(), t.getMessage() , Toast.LENGTH_SHORT).show();
            }
        });

        rowsAdapter.add(new ListRow(listRowAdapter1));
        setAdapter(rowsAdapter);
    }
    private void setupUIElements() {
        setBadgeDrawable(getActivity().getResources().getDrawable(R.drawable.adobe));
        setTitle(getString(R.string.browse_title));
        // Badge, when set, takes precedent
        // over title
        setHeadersState(HEADERS_ENABLED);
        setHeadersTransitionOnBackEnabled(true);

        // set fastLane (or headers) background color
        setBrandColor(ContextCompat.getColor(getActivity(), R.color.default_background));
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

        setOnItemViewClickedListener(new LiveDetailsFragment.ItemViewClickedListener());
        setOnItemViewSelectedListener(new LiveDetailsFragment.ItemViewSelectedListener());
    }

    private void prepareBackgroundManager() {

        mBackgroundManager = BackgroundManager.getInstance(getActivity());
        mBackgroundManager.attach(getActivity().getWindow());
        mBackgroundManager.setDrawable(ContextCompat.getDrawable(requireActivity(), R.drawable.darkk));

        mDefaultBackground = ContextCompat.getDrawable(getActivity(), R.drawable.default_background);
        mMetrics = new DisplayMetrics();
        getActivity().getWindowManager().getDefaultDisplay().getMetrics(mMetrics);
    }

    private void updateBackground(String uri) {
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

            //    Live live = (Live) item;
                Intent intent = new Intent(getActivity(),DetailsActivity.class);
                intent.putExtra(DetailsActivity.MOVIE, String.valueOf(((Live) item).title));
                getActivity().startActivity(intent);

            /*if (item instanceof String) {
                if (((String) item).contains(getString(R.string.error_fragment))) {
                    Intent intent1 = new Intent(getActivity(), BrowseErrorActivity.class);
                    startActivity(intent1);
                } else {
                    Toast.makeText(getActivity(), ((String) item), Toast.LENGTH_SHORT).show();
                }
            }*/
        }
    }

    private final class ItemViewSelectedListener implements OnItemViewSelectedListener {
        @Override
        public void onItemSelected(
                Presenter.ViewHolder itemViewHolder,
                Object item,
                RowPresenter.ViewHolder rowViewHolder,
                Row row) {
            if (item instanceof Live) {
                mBackgroundUri = ((Live) item).getBackgroundImageUrl();
                startBackgroundTimer();
                Intent intent = new Intent(getActivity(),PlaybackActivity.class);
                //startActivity(intent);
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

}
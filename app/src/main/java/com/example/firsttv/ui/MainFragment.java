package com.example.firsttv.ui;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
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
import androidx.leanback.widget.PresenterSelector;
import androidx.leanback.widget.Row;
import androidx.leanback.widget.RowPresenter;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;
import com.example.firsttv.R;
import com.example.firsttv.RetrofitFiles.JsonPlaceHolderApi;
import com.example.firsttv.Splash;
import com.example.firsttv.data.MyHeaderList;
import com.example.firsttv.model.HeaderItemModel;
import com.example.firsttv.model.Live;
import com.example.firsttv.model.Movie;
import com.example.firsttv.model.Post;
import com.example.firsttv.presenter.CardPresenter;
import com.example.firsttv.presenter.IconHeaderItemPresenter;
import com.example.firsttv.presenter.LiveCatPresenter;

import java.util.List;
import java.util.Objects;
import java.util.Timer;
import java.util.TimerTask;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainFragment extends BrowseSupportFragment {

    public void onBackPressed(){
        return;
    }

    private static final String TAG = "MainFragment";

    private static final int BACKGROUND_UPDATE_DELAY = 200;
    private static final int GRID_ITEM_WIDTH = 300;
    private static final int GRID_ITEM_HEIGHT = 280;
   // private static final int NUM_ROWS = 6;
    //private static final int NUM_COLS = 15;

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
        //List<Movie> list = MovieList.setupMovies();

        ArrayObjectAdapter rowsAdapter = new ArrayObjectAdapter(new ListRowPresenter());
        //CardPresenter cardPresenter = new CardPresenter();
        LiveCatPresenter liveCatPresenter = new LiveCatPresenter();
        CardPresenter cardPresenter = new CardPresenter();

        ArrayObjectAdapter listRowAdapter1 = new ArrayObjectAdapter(liveCatPresenter);
        ArrayObjectAdapter listrowadapter2 = new ArrayObjectAdapter(liveCatPresenter);
        ArrayObjectAdapter listrowadapter3 = new ArrayObjectAdapter(liveCatPresenter);
        ArrayObjectAdapter listrowadapter4 = new ArrayObjectAdapter(cardPresenter);
        Movie live = new Movie();
        live.setLiveImageUrl("https://www.google.com/imgres?imgurl=https%3A%2F%2Fcdn1.iconfinder.com%2Fdata%2Ficons%2Fweb-and-mobile-ui-outline-icon-kit%2F512%2FUI_Icons_2-04-512.png&imgrefurl=https%3A%2F%2Fwww.iconfinder.com%2Ficons%2F877957%2Flog_out_sign_out_logout_log_out_sign_logout_sign_signout_signout_sign_icon&tbnid=bf7IGAVKfwfeqM&vet=12ahUKEwjF0PmL0rH0AhUEj-YKHW-cA9AQMygGegUIARDPAQ..i&docid=BDU8Usogqj9ZeM&w=512&h=512&itg=1&q=signout&ved=2ahUKEwjF0PmL0rH0AhUEj-YKHW-cA9AQMygGegUIARDPAQ");
        listrowadapter4.add(live);

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://parrot-tv.azurewebsites.net/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        JsonPlaceHolderApi jsonPlaceHolderApi = retrofit.create(JsonPlaceHolderApi.class);
        Call<List<Post>> call = jsonPlaceHolderApi.getPosts();
        call.enqueue(new Callback<List<Post>>() {
            @Override
            public void onResponse(Call<List<Post>> call, retrofit2.Response<List<Post>> response) {

                //int img[] = {R.drawable.news,R.drawable.music,R.drawable.entertainement,R.drawable.sports};
                if(!response.isSuccessful()){
                    Toast.makeText(getActivity(), "Failed to get response!", Toast.LENGTH_SHORT).show();
                    return;
                }
                List<Post> posts = response.body();
                int i = 0,k=0,j=0,a=0;
                for(Post post : Objects.requireNonNull(posts)){
                    if(post.getDisplayid() == null){
                        Toast.makeText(getContext(), response.message(), Toast.LENGTH_SHORT).show();
                        return;
                    }
                    else if(post.getCategoryType().equals("LIVE")){
                        Live list1 = new Live();
                        list1.setTitle(post.getDisplayName());
                        list1.setLiveImageUrl(String.valueOf(post.getIconUrl()));
                        list1.setId(post.getDisplayid());
                        list1.setCategory(post.getCategoryType());
                        listRowAdapter1.add(list1);
                        i++;
                    }
                    else if(post.getCategoryType().equals("MOVIES")){
                        Live list2 = new Live();
                        list2.setTitle(String.valueOf(post.getDisplayName()));
                        list2.setLiveImageUrl(String.valueOf(post.getIconUrl()));
                        list2.setId(post.getDisplayid());
                        list2.setCategory(String.valueOf(post.getCategoryType()));
                        listrowadapter2.add(list2);
                    }
                    else if(post.getCategoryType().equals("SERIES")){
                        Live list3 = new Live();
                        list3.setTitle(post.getDisplayid());
                        list3.setLiveImageUrl(String.valueOf(post.getIconUrl()));
                        list3.setId(post.getDisplayid());
                        list3.setCategory(String.valueOf(post.getCategoryType()));
                        listrowadapter3.add(list3);
                    }

                }
            }

            @Override
            public void onFailure(Call<List<Post>> call, Throwable t) {
                Toast.makeText(getActivity(), t.getMessage() , Toast.LENGTH_SHORT).show();
            }
        });
        int k=0;
        HeaderItemModel header = new HeaderItemModel(k,MyHeaderList.HEADER_CATEGORY[k],MyHeaderList.HEADER_img[k]);
        rowsAdapter.add(new ListRow(header, listRowAdapter1));
        k++;
        header = new HeaderItemModel(k,MyHeaderList.HEADER_CATEGORY[k],MyHeaderList.HEADER_img[k]);
        rowsAdapter.add(new ListRow(header, listrowadapter2));
        k++;
        header = new HeaderItemModel(k,MyHeaderList.HEADER_CATEGORY[k],MyHeaderList.HEADER_img[k]);
        rowsAdapter.add(new ListRow(header, listrowadapter3));
        header = new HeaderItemModel(k,MyHeaderList.HEADER_CATEGORY[k],MyHeaderList.HEADER_img[k]);
        rowsAdapter.add(new ListRow(header,listrowadapter4));
        setAdapter(rowsAdapter);
    }

    private void prepareBackgroundManager() {

        mBackgroundManager = BackgroundManager.getInstance(requireActivity());
        mBackgroundManager.attach(requireActivity().getWindow());
        mBackgroundManager.setDrawable(ContextCompat.getDrawable(requireActivity(), R.drawable.images));

        mDefaultBackground = ContextCompat.getDrawable(getActivity(), R.drawable.images);
        mMetrics = new DisplayMetrics();
        getActivity().getWindowManager().getDefaultDisplay().getMetrics(mMetrics);
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    private void setupUIElements() {
        //setBadgeDrawable(requireActivity().getResources().getDrawable(R.drawable.ic_baseline_settings_24));
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
        listRowPresenter.setShadowEnabled(false);

        // set fastLane (or headers) background color
        setBrandColor(ContextCompat.getColor(requireActivity(), R.color.default_background));
        // set search icon color
        setSearchAffordanceColor(ContextCompat.getColor(getActivity(), R.color.default_background));
    }

    private void setupEventListeners() {
        setOnSearchClickedListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(),SearchActivity.class);
                startActivity(intent);
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
                LiveDetail.SHARED_ELEMENT_NAME = live.getId();
                String[] parts = LiveDetail.SHARED_ELEMENT_NAME.split("_");
                for(int i=0;i< parts.length;i++){
                    LiveDetail.SHARED_ELEMENT_NAME = parts[i] + " ";
                }
                LiveDetail.NOTIFICATION_ID = live.getCategory();
                //intent.putExtra(LiveDetail.LIVE, String.valueOf(((Live) item).id));
                requireActivity().startActivity(intent);
            }
            else if(item instanceof  Movie){
                SharedPreferences sharedPreferences2 = getActivity().getSharedPreferences("time", 0);
                SharedPreferences.Editor editor2 = sharedPreferences2.edit();
                editor2.clear();
                editor2.apply();
                Intent intent = new Intent(getContext(), Splash.class);
                startActivity(intent);
            }
            else if (item instanceof String) {
                if (((String) item).contains(getString(R.string.error_fragment))) {
                    Intent intent = new Intent(getActivity(), BrowseErrorActivity.class);
                    startActivity(intent);
                } else {
                    Toast.makeText(getActivity(), ((String) item), Toast.LENGTH_SHORT).show();
                }
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
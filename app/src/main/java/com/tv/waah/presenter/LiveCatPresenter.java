package com.tv.waah.presenter;

import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.ViewGroup;

import androidx.core.content.ContextCompat;
import androidx.leanback.widget.ImageCardView;
import androidx.leanback.widget.Presenter;

import com.tv.waah.R;
import com.tv.waah.model.Live;
import com.squareup.picasso.Picasso;

public class LiveCatPresenter extends Presenter {
    private static final String TAG = "LivePresenter";

    // change screen  width height to accomodate channells.
    // i think 70inch screen will have issue, so we need to check the screen height and according adjust.
    // to do for future as we can show more channels in bigger screen.
    private static final int CARD_WIDTH = 333;
    private static final int CARD_HEIGHT = 240;
    private static int sSelectedBackgroundColor;
    private static int sDefaultBackgroundColor;
    private Drawable mDefaultCardImage;

    private static void updateCardBackgroundColor(ImageCardView view, boolean selected) {
        int color =  sDefaultBackgroundColor;
        // Both background colors should be set because the view"s background is temporarily visible
        // during animations.
        view.setBackgroundColor(color);
        view.findViewById(R.id.info_field).setBackgroundColor(color);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent) {
        Log.d(TAG, "onCreateViewHolder");

        sDefaultBackgroundColor =
                ContextCompat.getColor(parent.getContext(), R.color.default_background);
        sSelectedBackgroundColor =
                ContextCompat.getColor(parent.getContext(), R.color.default_background);
        /*
         * This template uses a default image in res/drawable, but the general case for Android TV
         * will require your resources in xhdpi. For more information, see
         * https://developer.android.com/training/tv/start/layouts.html#density-resources
         */
        mDefaultCardImage = ContextCompat.getDrawable(parent.getContext(), R.drawable.movie);

        ImageCardView cardView =
                new ImageCardView(parent.getContext()) {
                    @Override
                    public void setSelected(boolean selected) {
                        updateCardBackgroundColor(this, selected);
                        super.setSelected(selected);
                    }
                };

        cardView.setFocusable(true);
        cardView.setFocusableInTouchMode(true);
        updateCardBackgroundColor(cardView, false);
        return new ViewHolder(cardView);
    }

    @Override
    public void onBindViewHolder(Presenter.ViewHolder viewHolder, Object item) {
        Live live = (Live) item;
        ImageCardView cardView = (ImageCardView) viewHolder.view;
        Log.d(TAG, "onBindViewHolder");
        if (live.getLiveImageUrl() !=null){
            if(live.getLiveImageUrl().isEmpty()){
                live.setLiveImageUrl(String.valueOf(R.drawable.movie));
            }
            cardView.setTitleText(live.getTitle());
            cardView.setMainImageDimensions(CARD_WIDTH, CARD_HEIGHT);
            Picasso.get()
                    .load(live.getLiveImageUrl())
                    .resize(720,480)
                    .error(mDefaultCardImage)
                    .into(cardView.getMainImageView());

           /* Glide.with(viewHolder.view.getContext())
                    .load(live.getLiveImageUrl())
                    .override(720,480)
                    .centerCrop()
                    .error(mDefaultCardImage)
                    .into(cardView.getMainImageView());*/
        }
    }

    @Override
    public void onUnbindViewHolder(Presenter.ViewHolder viewHolder) {
        Log.d(TAG, "onUnbindViewHolder");
        ImageCardView cardView = (ImageCardView) viewHolder.view;
        // Remove references to images so that the garbage collector can free up memory
        cardView.setBadgeImage(null);
        cardView.setMainImage(null);
    }
}

package com.example.firsttv.presenter;

import androidx.leanback.widget.AbstractDetailsDescriptionPresenter;

import com.example.firsttv.model.Live;

public class DetailsDescriptionPresenter extends AbstractDetailsDescriptionPresenter {

    @Override
    protected void onBindDescription(ViewHolder viewHolder, Object item) {
        Live movie = (Live) item;

        if (movie != null) {
            viewHolder.getTitle().setText(movie.getTitle());
            viewHolder.getSubtitle().setText(movie.getId());
            viewHolder.getBody().setText(movie.getLiveImageUrl());
        }
    }
}
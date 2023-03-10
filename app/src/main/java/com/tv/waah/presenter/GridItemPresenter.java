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

package com.tv.waah.presenter;

import android.app.Fragment;
import android.graphics.Color;
import android.view.Gravity;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.leanback.widget.AbstractDetailsDescriptionPresenter;
import androidx.leanback.widget.Presenter;

import com.tv.waah.R;


public abstract class GridItemPresenter extends Presenter {
    private static final int GRID_ITEM_WIDTH = 200;
    private static final int GRID_ITEM_HEIGHT = 200;

    private final Fragment mainFragment;

    public GridItemPresenter(Fragment fragment) {
        this.mainFragment = fragment;
    }

    @Override
    public AbstractDetailsDescriptionPresenter.ViewHolder onCreateViewHolder(ViewGroup parent) {
        TextView view = new TextView(parent.getContext());
        view.setLayoutParams(new ViewGroup.LayoutParams(GRID_ITEM_WIDTH, GRID_ITEM_HEIGHT));
        view.setFocusable(true);
        view.setFocusableInTouchMode(true);
        view.setBackgroundColor(mainFragment.getResources().getColor(R.color.default_background));
        view.setTextColor(Color.WHITE);
        view.setGravity(Gravity.CENTER);
        return new AbstractDetailsDescriptionPresenter.ViewHolder(view);
    }

    public void onBindViewHolder(AbstractDetailsDescriptionPresenter.ViewHolder viewHolder, Object item) {
        ((TextView) viewHolder.view).setText((String) item);
    }

    public void onUnbindViewHolder(AbstractDetailsDescriptionPresenter.ViewHolder viewHolder) {
    }
}

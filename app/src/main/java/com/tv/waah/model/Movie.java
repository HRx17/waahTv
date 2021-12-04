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

package com.tv.waah.model;

import java.util.List;

/*
 * Movie class represents video entity with title, description, image thumbs and video url.
 */
public class Movie {

    public String title;
    public String id;
    public String category;
    public String liveImageUrl;
    public List<Seasons> seasons;

    public Movie(List<Seasons> seasons) {
        this.seasons = seasons;
    }

    public List<Seasons> getSeasons() {
        return seasons;
    }

    public void setSeasons(List<Seasons> seasons) {
        this.seasons = seasons;
    }

    public String getLiveImageUrl() {
        return liveImageUrl;
    }

    public void setLiveImageUrl(String liveImageUrl) {
        this.liveImageUrl = liveImageUrl;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public void String(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Movie() {
    }

    public Movie(String title, String liveImageUrl, String category) {
        this.title = title;
        this.category = category;
        this.liveImageUrl = liveImageUrl;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getBackgroundImageUrl() { return null;
    }
}

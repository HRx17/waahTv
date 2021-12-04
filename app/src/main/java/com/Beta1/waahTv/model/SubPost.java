package com.Beta1.waahTv.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;


public class SubPost {

        @SerializedName("id")
        @Expose
        private String id;
        @SerializedName("category")
        @Expose
        private String category;
        @SerializedName("userEmail")
        @Expose
        private String userEmail;
        @SerializedName("channelList")
        @Expose
        private List<ChannelList> channelList;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public SubPost withId(String id) {
            this.id = id;
            return this;
        }

        public String getCategory() {
            return category;
        }

        public void setCategory(String category) {
            this.category = category;
        }

        public SubPost withCategory(String category) {
            this.category = category;
            return this;
        }

        public String getUserEmail() {
            return userEmail;
        }

        public void setUserEmail(String userEmail) {
            this.userEmail = userEmail;
        }

        public SubPost withUserEmail(String userEmail) {
            this.userEmail = userEmail;
            return this;
        }

        public List<ChannelList> getChannelList() {
            return channelList;
        }

        public void setChannelList(List<ChannelList> channelList) {
            this.channelList = channelList;
        }

        public SubPost withChannelList(List<ChannelList> channelList) {
            this.channelList = channelList;
            return this;
        }
}
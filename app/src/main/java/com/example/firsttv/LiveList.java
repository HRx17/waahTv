package com.example.firsttv;

import com.example.firsttv.model.Live;

import java.util.ArrayList;
import java.util.List;

public final class LiveList {


    static List<Live> list1;
    private static long count = 0;

    public static List<Live> setupLive() {
        list1 = new ArrayList<>();
       /* Retrofit retrofit = new Retrofit.Builder()
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
                Toast.makeText(getActivity(), "Got response!", Toast.LENGTH_SHORT).show();
                for(Post post : Objects.requireNonNull(posts)){
                    if(post.getDisplayid() == null){
                        Toast.makeText(getContext(), response.message(), Toast.LENGTH_SHORT).show();
                        return;
                    }
                    if(Objects.equals(post.getCategoryType(), "LIVE")){
                        list1.setTitle(post.getDisplayName());
                        list1.setLiveImageUrl(post.getIconUrl());
                        list1.setId(post.getDisplayid());
                        list1.setCategory(post.getCategoryType());
                        listRowAdapter1.add(list1);
                    }
                    else if(Objects.equals(post.getDisplayid(), "MOVIES")){
                        list2.setTitle(post.getDisplayName());
                        list2.setLiveImageUrl(post.getIconUrl());
                        list2.setId(post.getDisplayid());
                        list2.setCategory(post.getCategoryType());
                        listrowadapter2.add(list2);
                        //k++;
                    }
                    else if(Objects.equals(post.getDisplayName(), "SERIES")){
                        list3.setTitle(post.getDisplayid());
                        list3.setLiveImageUrl(post.getIconUrl());
                        list3.setId(post.getDisplayid());
                        list3.setCategory(post.getCategoryType());
                        listrowadapter3.add(list3);
                        //k++;
                    }

                }
            }

            @Override
            public void onFailure(Call<List<Post>> call, Throwable t) {
                Toast.makeText(getActivity(), t.getMessage() , Toast.LENGTH_SHORT).show();
            }
        });*/
        String title[] = new String[0];
        int liveImageUrl[] = {
                R.drawable.news,R.drawable.music,R.drawable.entertainement,R.drawable.sports
        };

        for (int index = 0; index < title.length; ++index) {
            list1.add(
                    buildMovieInfo1(
                            title[index],
                            liveImageUrl[index]));
        }

        return list1;
    }

    private static Live buildMovieInfo1(String title, int liveImageUrl){
        Live live = new Live();
        live.setTitle(title);
        //live.setLiveImageUrl(liveImageUrl);
        return live;
    }
}

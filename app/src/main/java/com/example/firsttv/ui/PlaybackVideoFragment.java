package com.example.firsttv.ui;

import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.widget.Toast;

import androidx.leanback.app.VideoSupportFragment;
import androidx.leanback.app.VideoSupportFragmentGlueHost;
import androidx.leanback.media.MediaPlayerAdapter;
import androidx.leanback.media.PlaybackTransportControlGlue;
import androidx.leanback.widget.PlaybackControlsRow;

import com.example.firsttv.RetrofitFiles.FailResponse;
import com.example.firsttv.RetrofitFiles.RetrofitClient;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Handles video playback with media controls.
 * Another video [layer option with exo player library(implementation 'com.google.android.exoplayer:exoplayer:2.7.2')
 */
public class PlaybackVideoFragment extends VideoSupportFragment {

    private PlaybackTransportControlGlue<MediaPlayerAdapter> mTransportControlGlue;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        VideoSupportFragmentGlueHost glueHost =
                new VideoSupportFragmentGlueHost(PlaybackVideoFragment.this);

        MediaPlayerAdapter playerAdapter = new MediaPlayerAdapter(getActivity());
        playerAdapter.setRepeatAction(PlaybackControlsRow.RepeatAction.INDEX_ONE);
        if(LiveDetail.NOTIFICATION_ID.equals("LIVE")){
            mTransportControlGlue = new PlaybackTransportControlGlue<>(getActivity(), playerAdapter);
            mTransportControlGlue.setHost(glueHost);
            //mTransportControlGlue.setTitle(PlaybackActivity.NAME);
            //mTransportControlGlue.setSubtitle(live.getId());
            mTransportControlGlue.playWhenPrepared();
            playerAdapter.setDataSource(Uri.parse(PlaybackActivity.URLL));
            mTransportControlGlue.setControlsOverlayAutoHideEnabled(false);
            hideControlsOverlay(true);
        }
        else {
            mTransportControlGlue = new PlaybackTransportControlGlue<>(getActivity(), playerAdapter);
            mTransportControlGlue.setHost(glueHost);
            mTransportControlGlue.setTitle(PlaybackActivity.NAME);
            //mTransportControlGlue.setSubtitle(live.getId());
            mTransportControlGlue.playWhenPrepared();
            playerAdapter.setDataSource(Uri.parse(PlaybackActivity.URLL));
            mTransportControlGlue.setSeekEnabled(true);
        }

        playerAdapter.getMediaPlayer().setOnErrorListener(new MediaPlayer.OnErrorListener() {
            @Override
            public boolean onError(MediaPlayer mp, int what, int extra) {
                FailResponse failResponse = new FailResponse(PlaybackActivity.NAME,PlaybackActivity.URLL,String.valueOf(what));
                Call<FailResponse> call = RetrofitClient
                        .getInstance().getApi().response(failResponse);
                call.enqueue(new Callback<FailResponse>() {
                    @Override
                    public void onResponse(Call<FailResponse> call, Response<FailResponse> response) {
                        if(response.isSuccessful()) {
                            Toast.makeText(getContext(), mp.toString(), Toast.LENGTH_SHORT).show();
                            Toast.makeText(getContext(), response.message(), Toast.LENGTH_SHORT).show();
                            return;
                        }
                        else{
                            Toast.makeText(getContext(), "response not successfull:"+response.message(), Toast.LENGTH_SHORT).show();
                            return;
                        }
                    }

                    @Override
                    public void onFailure(Call<FailResponse> call, Throwable t) {
                        Toast.makeText(getContext(), "fail:"+t.getMessage(), Toast.LENGTH_LONG).show();
                        System.out.println(t.getMessage());
                        return;
                    }
                });
                return false;
            }
        });
    }


    @Override
    public void onPause() {
        super.onPause();
        if (mTransportControlGlue != null) {
            mTransportControlGlue.pause();
        }
    }
}

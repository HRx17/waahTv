package com.tv.waah.ui;

import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.widget.Toast;

import androidx.leanback.app.VideoSupportFragment;
import androidx.leanback.app.VideoSupportFragmentGlueHost;
import androidx.leanback.media.MediaPlayerAdapter;
import androidx.leanback.media.PlaybackTransportControlGlue;
import androidx.leanback.widget.PlaybackControlsRow;

import com.tv.waah.RetrofitFiles.FailResponse;
import com.tv.waah.RetrofitFiles.RetrofitClient;

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
            private String convertMessageToString(int input)
            {
                switch (input) {
                    case MediaPlayer.MEDIA_ERROR_SERVER_DIED:
                        return "MEDIA_ERROR_SERVER_DIED";
                    case MediaPlayer.MEDIA_ERROR_UNKNOWN:
                        return "MEDIA_ERROR_UNKNOWN";
                    case MediaPlayer.MEDIA_ERROR_IO:
                        return "MEDIA_ERROR_IO";
                    case MediaPlayer.MEDIA_ERROR_MALFORMED:
                        return "MEDIA_ERROR_MALFORMED";
                    case MediaPlayer.MEDIA_ERROR_UNSUPPORTED:
                        return "MEDIA_ERROR_UNSUPPORTED";
                    case MediaPlayer.MEDIA_ERROR_TIMED_OUT:
                        return "MEDIA_ERROR_TIMED_OUT";
                }
                return "Unknown";
            }
            @Override
            public boolean onError(MediaPlayer mp, int what, int extra) {
                String error = String.format("what[%s], extra[%s]",convertMessageToString(what), convertMessageToString(extra));
                FailResponse failResponse = new FailResponse(PlaybackActivity.EMAIL,PlaybackActivity.NAME,PlaybackActivity.URLL,error);
                Call<Void> call = RetrofitClient
                        .getInstance().getApi().response(failResponse);
                call.enqueue(new Callback<Void>() {
                    @Override
                    public void onResponse(Call<Void> call, Response<Void> response) {
                        if(response.isSuccessful()) {
                            Toast.makeText(getContext(), "Channel is down, please try after some time.", Toast.LENGTH_SHORT).show();
                            return;
                        }
                    }

                    @Override
                    public void onFailure(Call<Void> call, Throwable t) {
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

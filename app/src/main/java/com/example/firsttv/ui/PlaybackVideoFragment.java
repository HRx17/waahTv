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
 */
public class PlaybackVideoFragment extends VideoSupportFragment {

    private PlaybackTransportControlGlue<MediaPlayerAdapter> mTransportControlGlue;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //final Live live =
        //(Live) getActivity().getIntent().getSerializableExtra(LiveDetail.LIVE);

        VideoSupportFragmentGlueHost glueHost =
                new VideoSupportFragmentGlueHost(PlaybackVideoFragment.this);

        MediaPlayerAdapter playerAdapter = new MediaPlayerAdapter(getActivity());
        playerAdapter.setRepeatAction(PlaybackControlsRow.RepeatAction.INDEX_NONE);

        mTransportControlGlue = new PlaybackTransportControlGlue<>(getActivity(), playerAdapter);
        mTransportControlGlue.setHost(glueHost);
        mTransportControlGlue.setTitle(PlaybackActivity.NAME);
        //mTransportControlGlue.setSubtitle(live.getId());
        mTransportControlGlue.playWhenPrepared();
        playerAdapter.setDataSource(Uri.parse(PlaybackActivity.URLL));

        playerAdapter.getMediaPlayer().setOnErrorListener(new MediaPlayer.OnErrorListener() {
            @Override
            public boolean onError(MediaPlayer mp, int what, int extra) {
                Call<FailResponse> call = RetrofitClient
                        .getInstance().getApi().response(PlaybackActivity.NAME,PlaybackActivity.URLL,mp.toString());
                call.enqueue(new Callback<FailResponse>() {
                    @Override
                    public void onResponse(Call<FailResponse> call, Response<FailResponse> response) {
                        Toast.makeText(getContext(), mp.toString(), Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onFailure(Call<FailResponse> call, Throwable t) {
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

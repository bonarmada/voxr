package com.bombon.voxr.fragment;

import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.util.ArrayMap;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bombon.voxr.R;
import com.bombon.voxr.activity.MainActivity;
import com.bombon.voxr.adapter.HistoryAdapter;
import com.bombon.voxr.model.Record;
import com.bombon.voxr.service.RecordService;
import com.bombon.voxr.service.UserService;
import com.bombon.voxr.util.ErrorCode;
import com.bombon.voxr.util.ServiceCallback;
import com.google.android.exoplayer2.ExoPlaybackException;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.PlaybackParameters;
import com.google.android.exoplayer2.Player;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.Timeline;
import com.google.android.exoplayer2.extractor.DefaultExtractorsFactory;
import com.google.android.exoplayer2.extractor.ExtractorsFactory;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.source.TrackGroupArray;
import com.google.android.exoplayer2.trackselection.AdaptiveTrackSelection;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.trackselection.TrackSelection;
import com.google.android.exoplayer2.trackselection.TrackSelectionArray;
import com.google.android.exoplayer2.trackselection.TrackSelector;
import com.google.android.exoplayer2.ui.SimpleExoPlayerView;
import com.google.android.exoplayer2.upstream.BandwidthMeter;
import com.google.android.exoplayer2.upstream.DataSource;
import com.google.android.exoplayer2.upstream.DefaultBandwidthMeter;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.util.Util;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Vaughn on 9/22/17.
 */

public class HistoryFragment extends Fragment implements HistoryAdapter.HistoryAdapterInteraction{
    private static final String TAG = HistoryFragment.class.getSimpleName();

    @Inject
    UserService userService;
    @Inject
    RecordService recordService;

    @BindView(R.id.recycler_view)
    RecyclerView recyclerView;

    @BindView(R.id.exo_player)
    SimpleExoPlayerView playerView;

    private HistoryAdapter adapter;
    private List<Record> records = new ArrayList<>();
    private SimpleExoPlayer player;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_history, container, false);
        ButterKnife.bind(this, v);

        ((MainActivity) getActivity()).getComponent().inject(this);
        return v;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        setupExoPlayer();
        setupRecyclerView();
        fetchRecords();
    }

    private void setupExoPlayer() {
        // Create player
        BandwidthMeter bandwidthMeter = new DefaultBandwidthMeter();
        TrackSelection.Factory videoTrackSelectionFactory =
                new AdaptiveTrackSelection.Factory(bandwidthMeter);
        TrackSelector trackSelector =
                new DefaultTrackSelector(videoTrackSelectionFactory);
        // 2. Create the player
        player =
                ExoPlayerFactory.newSimpleInstance(getActivity(), trackSelector);

        player.addListener(new Player.EventListener() {
            @Override
            public void onTimelineChanged(Timeline timeline, Object manifest) {

            }

            @Override
            public void onTracksChanged(TrackGroupArray trackGroups, TrackSelectionArray trackSelections) {

            }

            @Override
            public void onLoadingChanged(boolean isLoading) {

            }

            @Override
            public void onPlayerStateChanged(boolean playWhenReady, int playbackState) {
                switch(playbackState) {
                    case Player.STATE_READY:
                        break;
                    case Player.STATE_ENDED:
                        adapter.notifyDataSetChanged();
                        break;
                    default:
                        break;
                }
            }

            @Override
            public void onRepeatModeChanged(int repeatMode) {

            }

            @Override
            public void onPlayerError(ExoPlaybackException error) {

            }

            @Override
            public void onPositionDiscontinuity() {

            }

            @Override
            public void onPlaybackParametersChanged(PlaybackParameters playbackParameters) {

            }
        });

//        // Set player to view
//        playerView.setPlayer(player);
//        playerView.setControllerAutoShow(false);
//        playerView.setControllerHideOnTouch(false);
//        playerView.showController();
    }

    private MediaSource loadSourceFromUrl(String url){
        Uri mediaSourceUri = Uri.parse(url);

        // Produces DataSource instances through which media data is loaded.
        DataSource.Factory dataSourceFactory = new DefaultDataSourceFactory(getActivity(),
                Util.getUserAgent(getActivity(), "voxr"));
        // Produces Extractor instances for parsing the media data.
        ExtractorsFactory extractorsFactory = new DefaultExtractorsFactory();
        // This is the MediaSource representing the media to be played.
        return new ExtractorMediaSource(mediaSourceUri,
                dataSourceFactory, extractorsFactory, null, null);
    }

    private void preparePlayer(String url){
        player.prepare(loadSourceFromUrl(url));
    }


    private void setupRecyclerView() {
        adapter = new HistoryAdapter(getActivity(), records, this);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.addItemDecoration(new DividerItemDecoration(
                getActivity(),
                DividerItemDecoration.VERTICAL)
        );
    }

    private void fetchRecords() {
        recordService.get(userService.getLoggedIn().getId(), new ServiceCallback<List<Record>>() {
            @Override
            public void onSuccess(int statusCode, List<Record> result) {
                Log.e(TAG, statusCode + "");
                if (statusCode == 200) {
                    adapter.refresh(result);
                }
            }

            @Override
            public void onError(int code, String message) {

            }

        });
    }

    @Override
    public void btnPlayOnClick(int position, String url) {
        Log.e(TAG, url);
        preparePlayer(url);
        player.setPlayWhenReady(true);
    }

    @Override
    public void btnStopOnClick(int position) {
        player.stop();
        adapter.notifyDataSetChanged();
    }

    @Override
    public void onStop() {
        super.onStop();
        player.stop();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (playerView != null){
            playerView.setPlayer(null);
        }
        if (player != null) {
            player.stop();
            player.release();
            player = null;
        }
    }
}

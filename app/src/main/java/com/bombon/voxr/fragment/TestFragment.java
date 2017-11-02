package com.bombon.voxr.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bombon.voxr.R;
import com.bombon.voxr.activity.MainActivity;
import com.facebook.shimmer.ShimmerFrameLayout;

import at.grabner.circleprogress.CircleProgressView;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cafe.adriel.androidaudiorecorder.AndroidAudioRecorder;
import cafe.adriel.androidaudiorecorder.model.AudioChannel;
import cafe.adriel.androidaudiorecorder.model.AudioSampleRate;
import cafe.adriel.androidaudiorecorder.model.AudioSource;

import static android.app.Activity.RESULT_CANCELED;
import static android.app.Activity.RESULT_OK;

public class TestFragment extends Fragment {

    @BindView(R.id.circleProgress)
    CircleProgressView circleProgressView;

    @BindView(R.id.visualizerContainer)
    ShimmerFrameLayout shimmerFrameLayout;

    @OnClick(R.id.ivRecord)
    void recordOnClick() {
        initRecorder();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_test, container, false);
        ButterKnife.bind(this, v);
        ((MainActivity) getActivity()).getComponent().inject(this);
        return v;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        shimmerFrameLayout.setDuration(3000);
        shimmerFrameLayout.startShimmerAnimation();
    }

    private void initRecorder() {
        String filePath = Environment.getExternalStorageDirectory() + "/recorded_audio.wav";
        int color = ContextCompat.getColor(getActivity(), R.color.colorPrimaryDark);
        int requestCode = 0;
        AndroidAudioRecorder.with(getActivity())
                // Required
                .setFilePath(filePath)
                .setColor(color)
                .setRequestCode(requestCode)

                // Optional
                .setSource(AudioSource.MIC)
                .setChannel(AudioChannel.STEREO)
                .setSampleRate(AudioSampleRate.HZ_16000)
                .setAutoStart(false)
                .setKeepDisplayOn(true)

                // Start recording
                .record();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 0) {
            if (resultCode == RESULT_OK) {
                // Great! User has recorded and saved the audio file
            } else if (resultCode == RESULT_CANCELED) {
                // Oops! User has canceled the recording
            }
        }
    }

}

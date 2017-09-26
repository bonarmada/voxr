package com.bombon.voxr.fragment;


import android.Manifest;
import android.content.pm.PackageManager;
import android.media.AudioFormat;
import android.media.AudioRecord;
import android.media.MediaRecorder;
import android.opengl.GLSurfaceView;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bombon.voxr.R;
import com.bombon.voxr.activity.MainActivity;
import com.bombon.voxr.util.AnimUtils;
import com.bombon.voxr.util.PreferencesUtil;
import com.majeur.cling.Cling;
import com.majeur.cling.ClingManager;
import com.majeur.cling.ViewTarget;
import com.yalantis.audio.lib.AudioUtil;
import com.yalantis.waves.util.Horizon;

import javax.inject.Inject;

import at.grabner.circleprogress.CircleProgressView;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainFragment extends Fragment {

    @Inject
    PreferencesUtil preferencesUtil;

    @BindView(R.id.glSurface)
    GLSurfaceView glSurfaceView;

    @BindView(R.id.ivRecord)
    ImageView ivRecord;

    @BindView(R.id.circleProgress)
    CircleProgressView circleProgressView;

    @OnClick(R.id.ivRecord)
    void recordOnClick() {
        if (isRecording) {
            AnimUtils.ImageViewAnimatedChange(getActivity(), ivRecord, ContextCompat.getDrawable(getActivity(), R.drawable.ic_mic_black_24dp));
            circleProgressView.stopSpinning();
            isRecording = false;
        } else {
            AnimUtils.ImageViewAnimatedChange(getActivity(), ivRecord, ContextCompat.getDrawable(getActivity(), R.drawable.ic_stop_black_24dp));
            circleProgressView.spin();
            isRecording = true;
        }
    }

    private static final int REQUEST_PERMISSION_RECORD_AUDIO = 1;

    private static final int RECORDER_SAMPLE_RATE = 44100;
    private static final int RECORDER_CHANNELS = 1;
    private static final int RECORDER_ENCODING_BIT = 16;
    private static final int RECORDER_AUDIO_ENCODING = AudioFormat.ENCODING_PCM_16BIT;
    private static final int MAX_DECIBELS = 120;
    private boolean isRecording = false;

    private Horizon mHorizon;
    private AudioRecord audioRecord;
    private Thread recordingThread;
    private byte[] buffer;

    private ClingManager mClingManager;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_main, container, false);
        ButterKnife.bind(this, v);
        ((MainActivity) getActivity()).getComponent().inject(this);
        return v;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mHorizon = new Horizon(glSurfaceView, ContextCompat.getColor(getActivity(), R.color.charcoal),
                RECORDER_SAMPLE_RATE, RECORDER_CHANNELS, RECORDER_ENCODING_BIT);
        mHorizon.setMaxVolumeDb(MAX_DECIBELS);
    }

    @Override
    public void onResume() {
        super.onResume();
        glSurfaceView.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        glSurfaceView.onPause();
        isRecording = false;

        if (audioRecord != null) {
            audioRecord.release();
        }
        AudioUtil.disposeProcessor();
    }

    @Override
    public void onStop() {
        super.onStop();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case REQUEST_PERMISSION_RECORD_AUDIO:
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    checkPermissionsAndStart();
                } else {
                    getActivity().finish();
                }
        }

    }

    private void checkPermissionsAndStart() {
        if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.RECORD_AUDIO},
                    REQUEST_PERMISSION_RECORD_AUDIO);
        } else {
            initRecorder();
            if (audioRecord.getState() == AudioRecord.STATE_INITIALIZED) {
                startRecording();
            }
        }
    }

    private void initRecorder() {
        final int bufferSize = 2 * AudioRecord.getMinBufferSize(RECORDER_SAMPLE_RATE,
                RECORDER_CHANNELS, RECORDER_AUDIO_ENCODING);
        audioRecord = new AudioRecord(MediaRecorder.AudioSource.MIC, RECORDER_SAMPLE_RATE,
                RECORDER_CHANNELS, RECORDER_AUDIO_ENCODING, bufferSize);
        AudioUtil.initProcessor(RECORDER_SAMPLE_RATE, RECORDER_CHANNELS, RECORDER_ENCODING_BIT);

        recordingThread = new Thread("recorder") {
            @Override
            public void run() {
                super.run();
                buffer = new byte[bufferSize];
                Looper.prepare();
                audioRecord.setRecordPositionUpdateListener(recordPositionUpdateListener, new Handler(Looper.myLooper()));
                int bytePerSample = RECORDER_ENCODING_BIT / 8;
                float samplesToDraw = bufferSize / bytePerSample;
                audioRecord.setPositionNotificationPeriod((int) samplesToDraw);
                //We need to read first chunk to motivate recordPositionUpdateListener.
                //Mostly, for lower versions - https://code.google.com/p/android/issues/detail?id=53996
                audioRecord.read(buffer, 0, bufferSize);
                Looper.loop();
            }
        };
    }

    private void startRecording() {
        if (audioRecord != null) {
            audioRecord.startRecording();
        }
        recordingThread.start();
    }

    /**
     * this listener helps us to synchronise real time
     * and actual drawing
     */
    private AudioRecord.OnRecordPositionUpdateListener recordPositionUpdateListener = new AudioRecord.OnRecordPositionUpdateListener() {
        @Override
        public void onMarkerReached(AudioRecord recorder) {
            //empty for now
        }

        @Override
        public void onPeriodicNotification(AudioRecord recorder) {
            if (audioRecord.getRecordingState() == AudioRecord.RECORDSTATE_RECORDING
                    && audioRecord.read(buffer, 0, buffer.length) != -1) {
                mHorizon.updateView(buffer);
            }
        }
    };

    public void showHelpOverlay(){

        mClingManager = new ClingManager(getActivity());

        // When no Target is set, Target.NONE is used
        mClingManager.addCling(new Cling.Builder(getActivity())
                .setTitle("Welcome to this app")
                .setContent("This application is meant to be the best app you will ever try on android.")
                .build());


        // Target: Record Button
        mClingManager.addCling(new Cling.Builder(getActivity())
                .setTarget(new ViewTarget(circleProgressView))
                .setTitle("This is the recording button")
                .setContent("It lets you do stuff.")
                .build());


        mClingManager.setCallbacks(new ClingManager.Callbacks() {
            @Override
            public boolean onClingClick(int position) {
                // We return false to tell to cling manager that we didn't handle this,
                // so it can perform the default action (ie. showing the next Cling).
                // This is the default value returned by super.onClingClick(position), so
                // in a real project, we would just leave this method unoverriden.
                return false;
            }

            @Override
            public void onClingShow(int position) {
//                Toast.makeText(MyActivity.this, "Cling #" + position + " is shown", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onClingHide(int position) {
//                Toast.makeText(MyActivity.this, "Cling #" + position + " is hidden", Toast.LENGTH_SHORT).show();
//
//                // Last Cling has been shown, tutorial is ended.
//                if (position == 2) {
//                    mSharedPreferences.edit()
//                            .putBoolean(START_TUTORIAL_KEY, false)
//                            .apply();
//
//                    mClingManager = null;
//                }
            }
        });

        mClingManager.start();
    }

}

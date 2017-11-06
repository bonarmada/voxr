package com.bombon.voxr.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.TaskStackBuilder;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.res.TypedArrayUtils;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bombon.voxr.R;
import com.bombon.voxr.activity.MainActivity;
import com.bombon.voxr.model.Emotion;
import com.bombon.voxr.model.Record;
import com.bombon.voxr.model.User;
import com.bombon.voxr.model.pojo.WavFile;
import com.bombon.voxr.service.RecordService;
import com.bombon.voxr.service.UserService;
import com.bombon.voxr.util.ServiceCallback;
import com.facebook.shimmer.ShimmerFrameLayout;
import com.github.mikephil.charting.utils.FileUtils;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

import javax.inject.Inject;

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
    private final String TAG = TestFragment.this.getClass().getSimpleName();

    @Inject
    RecordService recordService;

    @Inject
    UserService userService;

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
//        String filePath = Environment.getExternalStorageDirectory() + "/recorded_audio.wav";
        String filePath = getActivity().getFilesDir() + "/recorded_audio.wav";
        int color = ContextCompat.getColor(getActivity(), R.color.colorPrimaryDark);
        int requestCode = 0;
        AndroidAudioRecorder.with(this)
                // Required
                .setFilePath(filePath)
                .setColor(color)
                .setRequestCode(requestCode)

                // Optional
                .setSource(AudioSource.MIC)
                .setChannel(AudioChannel.STEREO)
                .setSampleRate(AudioSampleRate.HZ_44100)
                .setAutoStart(false)
                .setKeepDisplayOn(true)

                // Start recording
                .recordFromFragment();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 0) {
            if (resultCode == RESULT_OK) {
                // Great! User has recorded and saved the audio file
                Log.e(TAG, "User has recorded and saved the audio file");

                try {
                    File file = new File(getActivity().getFilesDir() + "/recorded_audio.wav");
                    byte[] bytes = readFileToByteArray(file);

                    String encodedWavFile = Base64.encodeToString(bytes, 0);
                    Log.e("TestFragment", encodedWavFile);

                    // Create object
                    WavFile wavFile = new WavFile();
                    wavFile.setWavFile(encodedWavFile);

                    // Retrieve logged in
                    int userId = userService.getLoggedIn().getId();
                    recordService.createRecord(userId, wavFile, new ServiceCallback<Record>() {
                        @Override
                        public void onSuccess(int statusCode, Record result) {
                            Log.e("hehe", statusCode  + "");
                            Log.e("emo", result.toString());
                        }

                        @Override
                        public void onError(int statusCode, String message) {
                            Log.e("hehe", statusCode  + "");
                            Log.e("emo", message);
                        }
                    });

                } catch (IOException e) {
                    Log.e("hehe", "asdasd"  + "");
                    e.printStackTrace();
                }

            } else if (resultCode == RESULT_CANCELED) {
                // Oops! User has canceled the recording
                Log.e(TAG, "User has canceled the recording");
            }
        }
    }

    private byte[] readFileToByteArray(File file) throws IOException {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        BufferedInputStream in = new BufferedInputStream(new FileInputStream(file));

        int read;
        byte[] buff = new byte[1024];
        while ((read = in.read(buff)) > 0) {
            out.write(buff, 0, read);
        }
        out.flush();
        return out.toByteArray();
    }

}

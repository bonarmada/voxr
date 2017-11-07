package com.bombon.voxr.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.CardView;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bombon.voxr.R;
import com.bombon.voxr.activity.MainActivity;
import com.bombon.voxr.model.EmotionEnum;
import com.bombon.voxr.model.Record;
import com.bombon.voxr.model.pojo.WavFile;
import com.bombon.voxr.service.RecordService;
import com.bombon.voxr.service.UserService;
import com.bombon.voxr.util.ServiceCallback;
import com.facebook.shimmer.ShimmerFrameLayout;
import com.squareup.picasso.Picasso;

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

    // constants
    private static final int STATE_PRERECORD = 1;
    private static final int STATE_POSTRECORD = 2;
    private static final int STATE_EMOTION_PROCESSED = 3;

    @Inject
    RecordService recordService;

    @Inject
    UserService userService;

    @BindView(R.id.circleProgress)
    CircleProgressView circleProgressView;

    @BindView(R.id.visualizerContainer)
    ShimmerFrameLayout shimmerFrameLayout;

    @BindView(R.id.overlay)
    RelativeLayout overlay;

    @BindView(R.id.cardView)
    CardView cardView;

    @BindView(R.id.text_progress)
    TextView tvProgress;

    @BindView(R.id.img_emotion)
    ImageView ivEmotion;

    @BindView(R.id.bar_progress)
    ProgressBar progressBar;

    @BindView(R.id.emotion)
    TextView tvEmotion;

    @OnClick(R.id.ivRecord)
    void recordOnClick() {
        initRecorder();
    }

    @OnClick(R.id.dismiss)
    void dismissOnClick() {
        changeUIState(STATE_PRERECORD);
    }

    @OnClick(R.id.overlay)
    void overlayOnClick() {
        return;
    }

    private boolean isInit = false;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_test, container, false);
        ButterKnife.bind(this, v);

        ((MainActivity) getActivity()).getComponent().inject(this);

        changeUIState(STATE_PRERECORD);

        return v;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        shimmerFrameLayout.setDuration(3000);
        shimmerFrameLayout.startShimmerAnimation();
    }

    private void changeUIState(int statePrerecord) {
        Log.e(TAG, statePrerecord + "");
        switch (statePrerecord) {
            case STATE_PRERECORD:
                overlay.setVisibility(View.GONE);
                break;

            case STATE_POSTRECORD:
                overlay.setVisibility(View.VISIBLE);

                tvEmotion.setVisibility(View.GONE);
                ivEmotion.setVisibility(View.GONE);

                progressBar.setIndeterminate(true);
                progressBar.setVisibility(View.VISIBLE);

                tvProgress.setText(R.string.emotion_processing);
                break;

            case STATE_EMOTION_PROCESSED:
                tvEmotion.setVisibility(View.VISIBLE);
                ivEmotion.setVisibility(View.VISIBLE);

                progressBar.setVisibility(View.GONE);
                tvProgress.setText(R.string.emotion_finished);
                break;
        }
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

                changeUIState(STATE_POSTRECORD);

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
                            changeUIState(STATE_EMOTION_PROCESSED);
                            displayResultToUI(result, false);
                        }

                        @Override
                        public void onError(int statusCode, String message) {
                            changeUIState(STATE_EMOTION_PROCESSED);
                            displayResultToUI(null, true);
                            Log.e("hehe", statusCode + "");
                            Log.e("emo", message);
                        }
                    });

                } catch (IOException e) {
                    Log.e("hehe", "asdasd" + "");
                    e.printStackTrace();
                }

            } else if (resultCode == RESULT_CANCELED) {
                // Oops! User has canceled the recording
                Log.e(TAG, "User has canceled the recording");
            }
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }

    private void displayResultToUI(Record result, boolean isError) {
        if (isError) {
            Picasso.with(getActivity()).load(R.drawable.error).into(ivEmotion);
            tvEmotion.setText(R.string.emotion_message);
            tvProgress.setText(R.string.emotion_error);
            return;
        }

        int drawableId = 0;
        switch (result.getEmotionResult()) {
            case EmotionEnum.HAPPY:
                drawableId = R.drawable.happy;
                break;
            case EmotionEnum.ANGER:
                drawableId = R.drawable.angry;
                break;
            case EmotionEnum.SAD:
                drawableId = R.drawable.sad;
                break;
            case EmotionEnum.NEUTRAL:
                drawableId = R.drawable.neutral;
                break;
            case EmotionEnum.FEAR:
                drawableId = R.drawable.scared;
                break;
        }
        Picasso.with(getActivity()).load(drawableId).into(ivEmotion);
        tvProgress.setText(R.string.emotion_finished);
        tvEmotion.setText(result.getEmotionResult().toString());
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

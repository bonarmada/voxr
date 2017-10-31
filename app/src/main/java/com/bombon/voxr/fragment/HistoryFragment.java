package com.bombon.voxr.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.util.ArrayMap;
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

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Vaughn on 9/22/17.
 */

public class HistoryFragment extends Fragment {
    private static final String TAG = HistoryFragment.class.getSimpleName();

    @Inject
    UserService userService;
    @Inject
    RecordService recordService;

    @BindView(R.id.recycler_view)
    RecyclerView recyclerView;

    private HistoryAdapter adapter;
    private List<Record> records = new ArrayList<>();

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

        setupRecyclerView();
        fetchRecords();
    }

    private void fetchRecords() {
        recordService.get(userService.getLoggedIn().getId(), new ServiceCallback<List<Record>>() {
            @Override
            public void onSuccess(int statusCode, List<Record> result) {
                Log.e(TAG, statusCode + "");
                if (statusCode == 200){
                    adapter.refresh(result);
                }
            }
            @Override
            public void onError(ErrorCode code, String message) {

            }

        });
    }

    private void setupRecyclerView() {
        adapter = new HistoryAdapter(records);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
    }


}

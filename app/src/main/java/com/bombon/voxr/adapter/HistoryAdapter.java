package com.bombon.voxr.adapter;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bombon.voxr.R;
import com.bombon.voxr.model.EmotionEnum;
import com.bombon.voxr.model.Record;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.PercentFormatter;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Vaughn on 9/22/17.
 */

public class HistoryAdapter extends RecyclerView.Adapter<HistoryAdapter.ViewHolder> {

    private Context context;
    private List<Record> records = new ArrayList<>();

    public HistoryAdapter(Context context, List<Record> records) {
        this.records = records;
        this.context = context;
    }

    public void refresh(List<Record> records) {
        this.records = records;
        this.notifyDataSetChanged();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_history, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Record record = records.get(position);
        Log.e("HistoryADapter", record.toString());

        int drawableId = 0;
        switch (record.getEmotionResult()) {
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
        Picasso.with(context).load(drawableId).into(holder.ivEmoji);
        holder.tvEmotion.setText(record.getEmotionResult());

    }

    @Override
    public int getItemCount() {
        return records.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        // Bind Views here
        @BindView(R.id.emoji)
        ImageView ivEmoji;

        @BindView(R.id.emotion)
        TextView tvEmotion;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}

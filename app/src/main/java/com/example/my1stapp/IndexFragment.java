package com.example.my1stapp;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.ValueFormatter;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class IndexFragment extends Fragment {

    private TextView currentWeightTextView, weightGoalTextView, activityTextView, caloriesTextView, dietTextView;
    private Button addWeightButton, addActivityButton, addDietButton;
    private LineChart weightLineChart;

    private WeightDBHelper weightDBHelper;
    private ExerciseDBHelper exerciseDBHelper;
    private DietDBHelper dietDBHelper;

    public IndexFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_index, container, false);

        // 初始化数据库帮助类
        weightDBHelper = new WeightDBHelper(requireContext());
        exerciseDBHelper = new ExerciseDBHelper(requireContext());
        dietDBHelper = new DietDBHelper(requireContext());

        // 初始化UI元素
        currentWeightTextView = view.findViewById(R.id.tvCurrentWeight);
        activityTextView = view.findViewById(R.id.tvActivity);
        caloriesTextView = view.findViewById(R.id.tvCalories);
        dietTextView = view.findViewById(R.id.tvDiet);
        addWeightButton = view.findViewById(R.id.btnAddWeight);
        addActivityButton = view.findViewById(R.id.btnAddActivity);
        addDietButton = view.findViewById(R.id.btnAddDiet);
        weightLineChart = view.findViewById(R.id.LineChart);

        // 初始化折线图
        setupLineChart();

        // 初始化数据显示
        updateWeightData();
        updateActivityData();
        updateDietData();
        updateWeightChart();

        // 设置按钮点击监听器
        addWeightButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openThirdFragment();
            }
        });

        addActivityButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openThirdFragment();
            }
        });

        addDietButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openThirdFragment();
            }
        });

        return view;
    }

    private void setupLineChart() {
        weightLineChart.getDescription().setEnabled(false);
        weightLineChart.setTouchEnabled(true);
        weightLineChart.setDragEnabled(true);
        weightLineChart.setScaleEnabled(true);
        weightLineChart.setPinchZoom(true);

        // 设置X轴格式化
        XAxis xAxis = weightLineChart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setValueFormatter(new DateValueFormatter());
    }

    private void updateWeightChart() {
        SQLiteDatabase db = weightDBHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT " + WeightDBHelper.COLUMN_DATE + ", " + WeightDBHelper.COLUMN_WEIGHT + " FROM " + WeightDBHelper.TABLE_WEIGHT + " ORDER BY " + WeightDBHelper.COLUMN_DATE + " ASC", null);
        List<Entry> entries = new ArrayList<>();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        try {
            while (cursor.moveToNext()) {
                String dateString = cursor.getString(cursor.getColumnIndex(WeightDBHelper.COLUMN_DATE));
                Date date = sdf.parse(dateString);
                float weight = cursor.getFloat(cursor.getColumnIndex(WeightDBHelper.COLUMN_WEIGHT));
                entries.add(new Entry(date.getTime(), weight));
            }
        } catch (ParseException e) {
            e.printStackTrace();
        } finally {
            cursor.close();
        }

        LineDataSet dataSet = new LineDataSet(entries, "体重变化");
        dataSet.setColor(getResources().getColor(R.color.colorAccent));
        dataSet.setCircleColor(getResources().getColor(R.color.colorPrimary));
        dataSet.setLineWidth(2f);
        dataSet.setCircleRadius(3f);
        dataSet.setDrawValues(false);

        LineData lineData = new LineData(dataSet);
        weightLineChart.setData(lineData);
        weightLineChart.invalidate(); // 刷新图表
    }

    private void updateWeightData() {
        SQLiteDatabase db = weightDBHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + WeightDBHelper.TABLE_WEIGHT + " ORDER BY " + WeightDBHelper.COLUMN_DATE + " DESC LIMIT 1", null);
        if (cursor.moveToFirst()) {
            String date = cursor.getString(cursor.getColumnIndex(WeightDBHelper.COLUMN_DATE));
            float weight = cursor.getFloat(cursor.getColumnIndex(WeightDBHelper.COLUMN_WEIGHT));
            currentWeightTextView.setText("当前体重: " + weight + " kg (" + date + ")");
        } else {
            currentWeightTextView.setText("当前体重: -- kg");
        }
        cursor.close();
    }

    private void updateActivityData() {
        SQLiteDatabase db = exerciseDBHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + ExerciseDBHelper.TABLE_EXERCISE + " ORDER BY " + ExerciseDBHelper.COLUMN_ID + " DESC LIMIT 1", null);
        if (cursor.moveToFirst()) {
            String type = cursor.getString(cursor.getColumnIndex(ExerciseDBHelper.COLUMN_TYPE));
            int time = cursor.getInt(cursor.getColumnIndex(ExerciseDBHelper.COLUMN_TIME));
            activityTextView.setText("最近一次运动: " + type + " " + time + "分钟");
            caloriesTextView.setText("消耗热量: -- kcal"); // 这里可以根据运动类型和时间计算消耗热量
        } else {
            activityTextView.setText("最近一次运动: --");
            caloriesTextView.setText("消耗热量: -- kcal");
        }
        cursor.close();
    }

    private void updateDietData() {
        SQLiteDatabase db = dietDBHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT SUM(" + DietDBHelper.COLUMN_CALORIES + ") FROM " + DietDBHelper.TABLE_DIET + " WHERE " + DietDBHelper.COLUMN_DATE + " = date('now')", null);
        if (cursor.moveToFirst()) {
            int totalCalories = cursor.getInt(0);
            dietTextView.setText("今日饮食: " + totalCalories + " kcal");
        } else {
            dietTextView.setText("今日饮食: -- kcal");
        }
        cursor.close();
    }

    private void openThirdFragment() {
        ThirdFragment thirdFragment = new ThirdFragment();
        FragmentTransaction transaction = requireActivity().getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.fragment_top, thirdFragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        weightDBHelper.close();
        exerciseDBHelper.close();
        dietDBHelper.close();
    }

    public static IndexFragment newInstance(String param1, String param2) {
        IndexFragment fragment = new IndexFragment();
        Bundle args = new Bundle();
        args.putString("param1", param1);
        args.putString("param2", param2);
        fragment.setArguments(args);
        return fragment;
    }

    // 自定义X轴值格式化器
    private class DateValueFormatter extends ValueFormatter {
        private final SimpleDateFormat mFormat = new SimpleDateFormat("MM-dd", Locale.getDefault());

        @Override
        public String getFormattedValue(float value) {
            return mFormat.format(new Date((long) value));
        }
    }
}

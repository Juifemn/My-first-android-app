package com.example.my1stapp;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;

public class MainActivity extends AppCompatActivity {

    private TextView currentWeightTextView, weightGoalTextView, activityTextView, caloriesTextView, dietTextView;
    private Button addWeightButton, addActivityButton, addDietButton;

    private WeightDBHelper weightDBHelper;
    private ExerciseDBHelper exerciseDBHelper;
    private DietDBHelper dietDBHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // 初始化数据库帮助类
        weightDBHelper = new WeightDBHelper(this);
        exerciseDBHelper = new ExerciseDBHelper(this);
        dietDBHelper = new DietDBHelper(this);

        // 初始化UI元素
        currentWeightTextView = findViewById(R.id.tvCurrentWeight);
        activityTextView = findViewById(R.id.tvActivity);
        caloriesTextView = findViewById(R.id.tvCalories);
        dietTextView = findViewById(R.id.tvDiet);
        addWeightButton = findViewById(R.id.btnAddWeight);
        addActivityButton = findViewById(R.id.btnAddActivity);
        addDietButton = findViewById(R.id.btnAddDiet);

        // 初始化数据显示
        updateWeightData();
        updateActivityData();
        updateDietData();

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
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.fragmentContainer, thirdFragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        weightDBHelper.close();
        exerciseDBHelper.close();
        dietDBHelper.close();
    }
}

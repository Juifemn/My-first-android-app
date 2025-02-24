package com.example.my1stapp;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.database.sqlite.SQLiteDatabase;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class ThirdFragment extends Fragment {

    private EditText weightInput, exerciseInput, dietInput;
    private TextView dateInput, timeInput;
    private Button addWeightButton, addExerciseButton, addDietButton;
    private Spinner exerciseTypeSpinner;

    private WeightDBHelper weightDBHelper;
    private ExerciseDBHelper exerciseDBHelper;
    private DietDBHelper dietDBHelper;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_third, container, false);

        weightDBHelper = new WeightDBHelper(getContext());
        exerciseDBHelper = new ExerciseDBHelper(getContext());
        dietDBHelper = new DietDBHelper(getContext());

        weightInput = view.findViewById(R.id.weightInput);
        dateInput = view.findViewById(R.id.dateInput);
        exerciseInput = view.findViewById(R.id.exerciseInput);
        exerciseTypeSpinner = view.findViewById(R.id.exerciseTypeSpinner);
        dietInput = view.findViewById(R.id.dietInput);
        timeInput = view.findViewById(R.id.timeInput);
        addWeightButton = view.findViewById(R.id.addWeightButton);
        addExerciseButton = view.findViewById(R.id.addExerciseButton);
        addDietButton = view.findViewById(R.id.addDietButton);

        // 初始化日期选择器
        initDatePicker(dateInput);
        // 初始化时间选择器
        initTimePicker(timeInput);

        addWeightButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addWeightData();
            }
        });

        addExerciseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addExerciseData();
            }
        });

        addDietButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addDietData();
            }
        });

        return view;
    }

    private void initDatePicker(final TextView textView) {
        Calendar calendar = Calendar.getInstance();
        final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());

        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatePickerDialog datePickerDialog = new DatePickerDialog(getContext(), (view, year, monthOfYear, dayOfMonth) -> {
                    Calendar newDate = Calendar.getInstance();
                    newDate.set(year, monthOfYear, dayOfMonth);
                    textView.setText(dateFormat.format(newDate.getTime()));
                }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
                datePickerDialog.show();
            }
        });
    }

    private void initTimePicker(final TextView textView) {
        Calendar calendar = Calendar.getInstance();
        final SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm", Locale.getDefault());

        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TimePickerDialog timePickerDialog = new TimePickerDialog(getContext(), (view, hourOfDay, minute) -> {
                    Calendar newTime = Calendar.getInstance();
                    newTime.set(Calendar.HOUR_OF_DAY, hourOfDay);
                    newTime.set(Calendar.MINUTE, minute);
                    textView.setText(timeFormat.format(newTime.getTime()));
                }, calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE), true);
                timePickerDialog.show();
            }
        });
    }

    private void addWeightData() {
        String weightStr = weightInput.getText().toString();
        String date = dateInput.getText().toString();

        if (!weightStr.isEmpty() && !date.isEmpty()) {
            try {
                float weight = Float.parseFloat(weightStr);
                SQLiteDatabase db = weightDBHelper.getWritableDatabase();
                db.execSQL("INSERT INTO " + WeightDBHelper.TABLE_WEIGHT + " (" + WeightDBHelper.COLUMN_DATE + ", " + WeightDBHelper.COLUMN_WEIGHT + ") VALUES (?, ?)",
                        new Object[]{date, weight});
                Toast.makeText(getContext(), "体重数据添加成功", Toast.LENGTH_SHORT).show();
                weightInput.setText("");
                dateInput.setText("");
            } catch (NumberFormatException e) {
                Toast.makeText(getContext(), "请输入有效的体重数据", Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(getContext(), "请填写体重和日期", Toast.LENGTH_SHORT).show();
        }
    }

    private void addExerciseData() {
        String exerciseTimeStr = exerciseInput.getText().toString();
        String exerciseType = exerciseTypeSpinner.getSelectedItem().toString();

        if (!exerciseTimeStr.isEmpty()) {
            try {
                int exerciseTime = Integer.parseInt(exerciseTimeStr);
                SQLiteDatabase db = exerciseDBHelper.getWritableDatabase();
                db.execSQL("INSERT INTO " + ExerciseDBHelper.TABLE_EXERCISE + " (" + ExerciseDBHelper.COLUMN_TYPE + ", " + ExerciseDBHelper.COLUMN_TIME + ") VALUES (?, ?)",
                        new Object[]{exerciseType, exerciseTime});
                Toast.makeText(getContext(), "运动数据添加成功", Toast.LENGTH_SHORT).show();
                exerciseInput.setText("");
            } catch (NumberFormatException e) {
                Toast.makeText(getContext(), "请输入有效的运动时间", Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(getContext(), "请填写运动时间", Toast.LENGTH_SHORT).show();
        }
    }

    private void addDietData() {
        String caloriesStr = dietInput.getText().toString();
        String date = timeInput.getText().toString();

        if (!caloriesStr.isEmpty() && !date.isEmpty()) {
            try {
                int calories = Integer.parseInt(caloriesStr);
                SQLiteDatabase db = dietDBHelper.getWritableDatabase();
                db.execSQL("INSERT INTO " + DietDBHelper.TABLE_DIET + " (" + DietDBHelper.COLUMN_DATE + ", " + DietDBHelper.COLUMN_CALORIES + ") VALUES (?, ?)",
                        new Object[]{date, calories});
                Toast.makeText(getContext(), "饮食数据添加成功", Toast.LENGTH_SHORT).show();
                dietInput.setText("");
                timeInput.setText("");
            } catch (NumberFormatException e) {
                Toast.makeText(getContext(), "请输入有效的卡路里摄入量", Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(getContext(), "请填写卡路里摄入量和日期", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        weightDBHelper.close();
        exerciseDBHelper.close();
        dietDBHelper.close();
    }
}

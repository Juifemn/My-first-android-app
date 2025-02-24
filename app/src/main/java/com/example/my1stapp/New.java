//package com.example.my1stapp;
//
//import android.app.DatePickerDialog;
//import android.content.DialogInterface;
//import android.content.Intent;
//import android.os.Bundle;
//import android.view.View;
//import android.widget.ArrayAdapter;
//import android.widget.Button;
//import android.widget.DatePicker;
//import android.widget.EditText;
//import android.widget.RadioGroup;
//import android.widget.Spinner;
//import android.widget.TextView;
//import android.widget.AdapterView;
//
//import androidx.activity.EdgeToEdge;
//import androidx.appcompat.app.AlertDialog;
//import androidx.appcompat.app.AppCompatActivity;
//import androidx.core.graphics.Insets;
//import androidx.core.view.ViewCompat;
//import androidx.core.view.WindowInsetsCompat;
//
//import java.util.Calendar;
//
//public class New extends AppCompatActivity {
//    private EditText userName,userBirth,userHobby;
//    private TextView userOutput;
//    private RadioGroup userSex;
//    private Button BtSubmit,BtCancel;
//    private String[] strHobby;
//    private boolean[] checkedID;
//    private Spinner spinner;
//    String strSex;
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        EdgeToEdge.enable(this);
//        setContentView(R.layout.activity_new);
//        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
//            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
//            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
//            return insets;
//        });
//        //控件初始化
//        userName=findViewById(R.id.edit_name);
//        userBirth=findViewById(R.id.edit_birth);
//        userSex=findViewById(R.id.radio_sex);
//        userHobby=findViewById(R.id.edit_hobby);
//        userOutput=findViewById(R.id.text_output);
//        BtSubmit=findViewById(R.id.button_submit);
//        BtCancel=findViewById(R.id.button_cancel);
//        strHobby=new String[]{"体育","音乐","旅行","美术","文学"};
//        checkedID=new boolean[strHobby.length];
//
//        //出生年月选择
//        userBirth.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                DatePickerDialog datePickerDialog=new DatePickerDialog(New.this, new DatePickerDialog.OnDateSetListener() {
//                    @Override
//                    public void onDateSet(DatePicker datePicker, int year, int month, int dayOfMonth) {
//                        userBirth.setText(new StringBuilder().append(year).append("_").append(month).append("_").append(dayOfMonth));
//                    }
//                },Calendar.getInstance().get(Calendar.YEAR),Calendar.getInstance().get(Calendar.MONTH),Calendar.getInstance().get(Calendar.DAY_OF_MONTH));
//                datePickerDialog.show();
//            }
//        });
//
//        //操作性别选择控件
//        userSex.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
//            @Override
//            public void onCheckedChanged(RadioGroup radioGroup, int checkedId) {
//                if (checkedId==R.id.id_male){
//                    strSex="男";
//                } else if (checkedId == R.id.id_female) {
//                    strSex="女";
//                }
//            }
//        });
//        //兴趣选择对话框
//        userHobby.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                AlertDialog.Builder myDialog = new AlertDialog.Builder(New.this);
//                myDialog.setTitle("选择兴趣");
//                myDialog.setMultiChoiceItems(strHobby, checkedID, new DialogInterface.OnMultiChoiceClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialogInterface, int which, boolean isChecked) {
//                        checkedID[which]= isChecked;
//                    }
//                });
//                myDialog.setPositiveButton("确定", new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialogInterface, int which) {
//                        String text="";
//                        for (int i=0;i<checkedID.length;i++){
//                            if (checkedID[i])
//                                text=text+strHobby[i]+" ";
//                        }
//                        userHobby.setText(text);
//                    }
//                });
//                myDialog.setNegativeButton("取消", new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialogInterface, int i) {
//                        userHobby.setText("");
//                    }
//                });
//                myDialog.show();
//            }
//        });
//        //院校列表
//        spinner = findViewById(R.id.spinner_school);
//        String[] schools = new String[]{"浙江大学", "浙江工业大学", "清华大学", "北京大学"};
//        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, schools);
//        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//        spinner.setAdapter(adapter);
//        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
//            @Override
//            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
//                // 获取选中的院校名称
//                String selectedSchool = parent.getItemAtPosition(position).toString();
//                // 更新Spinner的显示为选中的院校名称
//                spinner.setSelection(position);
//                // 这里可以执行其他操作，比如使用selectedSchool变量
//            }
//
//            @Override
//            public void onNothingSelected(AdapterView<?> parent) {
//                // 可以在这里处理未选择任何项的情况
//            }
//        });
//        //确定按钮操作
//        BtSubmit.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                if (userName.getText().toString().trim().equals(""))
//                {
//                   //弹出警告对话框
//                    AlertDialog.Builder builder=new AlertDialog.Builder(New.this);
//                    builder.setTitle("警告").setMessage("请输入姓名").setPositiveButton("确定",null).show();
//                }
//                else {
//                    StringBuilder userInfor = new StringBuilder().append(userName.getText().toString())
//                            .append(userBirth.getText().toString())
//                            .append(strSex).append(userBirth.getText().toString())
//                            .append(userHobby.getText().toString());
//                    userOutput.setText(userInfor);
//                }
//            }
//        });
//        //取消按钮操作
//        BtCancel.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                userName.setText("");
//                userBirth.setText("");
//                userOutput.setText("");
//                userSex.clearCheck();
//                userBirth.setText("");
//                userHobby.setText("");
//            }
//        });
//    }
//}
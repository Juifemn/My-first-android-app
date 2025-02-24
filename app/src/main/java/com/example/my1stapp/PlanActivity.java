package com.example.my1stapp;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.ContentValues;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.Calendar;

public class PlanActivity extends AppCompatActivity {
    EditText et_title,et_content,et_date,et_time;
    Button bt_clear,bt_insert,bt_query;
    private MyDBOpenHelper dbOpenHelper;//数据库对象
    TextView tv_Infor;
    private String userName;
    private static final int LOGIN_REQUEST_CODE = 2;
    @SuppressLint({"WrongViewCast", "MissingInflatedId"})
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_plan);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        et_title=findViewById(R.id.et_agendaTitle);
        et_content=findViewById(R.id.et_agendaContent);
        et_date=findViewById(R.id.et_agendaDate);
        et_time=findViewById(R.id.et_agendaTime);
        bt_clear=findViewById(R.id.bt_clear);
        bt_insert=findViewById(R.id.bt_insert);
        bt_query=findViewById(R.id.bt_query);
        tv_Infor=findViewById((R.id.tv_infor));

        //----------------读取用户登陆信息，获取用户名---------
        SharedPreferences userinfro = getSharedPreferences("UserInfor",MODE_PRIVATE);
        userName = userinfro.getString("logUser","");
        if (userName == null || userName.isEmpty()) {
            Log.e("PlanActivity", "User is not logged in or user name is empty");
            Intent intent = new Intent(PlanActivity.this, LoginActivity.class);
            startActivityForResult(intent, LOGIN_REQUEST_CODE);
            return;
        }


        //----------------实例化数据库，创建以用户名为文件名的数据库文件（如已创建，则打开数据库）--------
        dbOpenHelper = new MyDBOpenHelper(getApplicationContext(),userName,null,2);

        //----------------设置日期---------------------
        et_date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DatePickerDialog datePickerDialog=new DatePickerDialog(PlanActivity.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datePicker, int year, int month, int dayOfMonth) {
                        et_date.setText(new StringBuilder().append(year).append("_").append(month).append("_").append(dayOfMonth));
                    }
                }, Calendar.getInstance().get(Calendar.YEAR),Calendar.getInstance().get(Calendar.MONTH),Calendar.getInstance().get(Calendar.DAY_OF_MONTH));
                datePickerDialog.show();
            }
        });
        //-----------------设置时间---------------------
        et_time.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TimePickerDialog timePickerDialog=new TimePickerDialog(PlanActivity.this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int hourOfDay, int minute) {
                        et_time.setText(new StringBuilder().append(hourOfDay).append(":").append(minute));
                    }
                },0,0,true);
                timePickerDialog.show();

            }
        });
        //-----------------插入计划日程到数据库----------------
        bt_insert.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d("PlanActivity", "onClick: bt_insert");
                String state = "代办";//计划的默认状态为代办
                SQLiteDatabase dbWriter = dbOpenHelper.getWritableDatabase();//定义数据库写入对象，获取写入权限
                ContentValues cv = new ContentValues();//定义 ContenValues对象，创建数据缓存区
                cv.put("title", et_title.getText().toString());//写入计划标题
                cv.put("date", et_date.getText().toString());//写入日期
                cv.put("time", et_time.getText().toString());//写入时间
                cv.put("content", et_content.getText().toString());//写入内容
                cv.put("state", state);//写入计划状态
                if (dbWriter.insert("agenda", null, cv) < 0) {  //数据插入到数据库表中，如果返回值<0表明插入失败
                    //Toast.makeText(MainAcitvity.this,"递交失败",Toast.LENGTH_SHORT).show();
                    tv_Infor.setText("日程创建失败");
                } else {
                    SQLiteDatabase dbReader = dbOpenHelper.getReadableDatabase();//定义数据库对象，获取读的权限
                    //利用Cursor（游标）对象来查询某条记录，当前要找到最新一条记录
                    Cursor c = dbReader.query("agenda", null, null,
                            null, null, null, "_id desc", "0,1");//按降序排列desc，升序asc
                    c.moveToFirst();                                        //查询完后，游标返回到最顶端
                    @SuppressLint("Range") String date = c.getString(c.getColumnIndex("date"));//读取查询到的记录相关字段内容
                    @SuppressLint("Range") String time = c.getString(c.getColumnIndex("time"));
                    @SuppressLint("Range") String title = c.getString(c.getColumnIndex("title"));
                    tv_Infor.setText("创建一条新日程：\n" + date + " " + time + " " + title);

                }
            }
        });
        //--------------------跳转到查询界面-------------------
        bt_query.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d("PlanActivity", "onClick: bt_query");
                Intent intent = new Intent(PlanActivity.this,PlanListActivity.class);
                intent.putExtra("DBname",userName);//将当前数据库文件名发送给PlanListActivity
                startActivity(intent);
            }
        });
        //--------------------清空窗口内容---------------------
        bt_clear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                et_title.setText("");
                et_content.setText("");
                et_date.setText("");
                et_time.setText("");
            }
        });


    }
}
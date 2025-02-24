package com.example.my1stapp;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.CursorAdapter;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class PlanListActivity extends AppCompatActivity {

    ListView listView;
    MyDBOpenHelper dbOpenHelper;//MyDBOpenHelper数据库对象
    SQLiteDatabase dbReader,dbWrite;// 数据库对象，用于读和写
    SimpleCursorAdapter adapter;//游标适配器
    String[] args;
    String DBname;//数据库名称

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_plan_list);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        listView = findViewById(R.id.list_content);//列表控件初始化

        Intent intent = getIntent();               //获取数据库文件名
        DBname=intent.getStringExtra("DBname");

        dbOpenHelper = new MyDBOpenHelper(getApplicationContext(),DBname,null,2);
        dbReader = dbOpenHelper.getReadableDatabase();  //获取读权限
        dbWrite = dbOpenHelper.getWritableDatabase();   //获取写权限
        showAll();                                      //内容显示

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int posiyion, long id) {
                TextView itemID = view.findViewById(R.id.list_id);//获取某条信息的ID号
                args = new String[]{itemID.getText().toString()};
                                                                //通过ID号进行记录的查询
                Cursor c=dbReader.query("agenda",null,"_id=?",args,null,null,null);
                c.moveToFirst();
                @SuppressLint("Range") String title = c.getString(c.getColumnIndex("title"));
                @SuppressLint("Range") String content = c.getString(c.getColumnIndex("content"));
                                                                //弹出对话框，显示记录的具体内容
                AlertDialog.Builder builder = new AlertDialog.Builder(PlanListActivity.this);
                builder.setTitle("日程标题："+title);
                builder.setMessage("内容"+content);
                                                                //如果完成，刷新数据库中记录改写成“完成”
                builder.setPositiveButton("完成", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int which) {
                        ContentValues cv = new ContentValues();
                        cv.put("state","完成");
                        dbWrite.update("agenda",cv,"_id=?",args);
                        showAll();  //更新List显示内容
                    }
                });
                                                                //如果未完成，则将记录改成“代办”
                builder.setNegativeButton("代办", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int which) {
                        ContentValues cv = new ContentValues();
                        cv.put("state","未完成");
                        dbWrite.update("agenda",cv,"_id=?",args);
                        showAll();  //更新List显示内容
                    }
                });
                builder.show();  //显示对话框
            }
        });
    }
    //----------------自定义showAll()方法，用于将查询数据库的内容呈现出来----------------
    public void showAll(){
        Cursor cs = dbReader.query("agenda",null,null,null,
                null,null,"date,time");//查询表中的所有内容，按照日期和时间进行排序
        cs.moveToFirst();                                     //游标已到最顶端
        String[] from = {"_id","date","time","title","state"};//字段名称，设置为显示数据源
        int[] to = {R.id.list_id,R.id.list_date,R.id.list_time,R.id.list_title,R.id.list_state};//控件ID，设置为数据显示目的地
        adapter = new SimpleCursorAdapter(PlanListActivity.this,R.layout.listitem,cs,
                from,to, CursorAdapter.FLAG_REGISTER_CONTENT_OBSERVER);//利用SimpleCursorAdapter方法，将数据显示关系进行绑定
        listView.setAdapter(adapter);//显示数据
    }

    @Override
    protected void  onDestroy() {  //关闭全局变量打开的数据库对象
        super.onDestroy();
        dbReader.close();
        dbWrite.close();

    }
}
package com.example.my1stapp;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class LoginActivity extends AppCompatActivity {
    private EditText logUserName, logPW;
    private Button btReg, btSubmit;
    private int LogResultCode = 3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_login);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        logUserName = findViewById(R.id.username);
        logPW = findViewById(R.id.password);
        btReg = findViewById(R.id.registerButton);
        btSubmit = findViewById(R.id.loginButton);

        // 设置注册按钮的点击监听器
        btReg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, RegActivity.class);
                startActivity(intent);
            }
        });

        // 设置登录按钮的点击监听器
        btSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String username = logUserName.getText().toString().trim();
                String password = logPW.getText().toString().trim();

                // 检查用户名是否为空
                if (username.isEmpty()) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(LoginActivity.this);
                    builder.setTitle("警告")
                            .setMessage("请输入用户名")
                            .setPositiveButton("确定", null)
                            .show();
                } else {
                    // 查询SharedPreferences中的用户信息
                    SharedPreferences sharedPreferences = getSharedPreferences("UserInfor", MODE_PRIVATE);
                    String storedPassword = sharedPreferences.getString(username, "");

                    if (storedPassword.isEmpty()) {
                        Toast.makeText(LoginActivity.this, "用户名不存在", Toast.LENGTH_SHORT).show();
                    } else if (storedPassword.equals(password)) {
                        // 用户名和密码匹配
                        SharedPreferences.Editor editor = sharedPreferences.edit();
                        editor.putString("logUser", username);
                        editor.apply();

                        Intent intent = new Intent();
                        intent.putExtra("username", username);
                        setResult(LogResultCode, intent);
                        finish();
                    } else {
                        Toast.makeText(LoginActivity.this, "密码错误", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
    }
}

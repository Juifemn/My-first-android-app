package com.example.my1stapp;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class RegActivity extends AppCompatActivity {

    private EditText usernameEditText, passwordEditText, confirmPasswordEditText;
    private Button registerButton, loginButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reg);

        // 初始化控件
        usernameEditText = findViewById(R.id.registerUsername);
        passwordEditText = findViewById(R.id.registerPassword);
        confirmPasswordEditText = findViewById(R.id.registerConfirmPassword);
        registerButton = findViewById(R.id.registerButton);
        loginButton = findViewById(R.id.loginButton);

        // 设置注册按钮的点击监听器
        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String username = usernameEditText.getText().toString().trim();
                String password = passwordEditText.getText().toString().trim();
                String confirmPassword = confirmPasswordEditText.getText().toString().trim();

                // 检查用户名和密码是否为空
                if (username.isEmpty() || password.isEmpty()) {
                    Toast.makeText(RegActivity.this, "用户名或密码不能为空", Toast.LENGTH_SHORT).show();
                    return;
                }

                // 检查两次输入的密码是否一致
                if (!password.equals(confirmPassword)) {
                    Toast.makeText(RegActivity.this, "两次输入的密码不一致", Toast.LENGTH_SHORT).show();
                    return;
                }

                // 将用户信息写入SharedPreferences
                SharedPreferences.Editor editor = getSharedPreferences("UserInfor", MODE_PRIVATE).edit();
                editor.putString(username, password);
                editor.apply();
                finish();

                // 注册成功，显示提示信息并返回登录界面
                Toast.makeText(RegActivity.this, "注册成功", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(RegActivity.this, LoginActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                startActivity(intent);
                finish(); // 结束当前的RegisterActivity
            }
        });

        // 设置登录按钮的点击监听器，用于返回登录界面
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(RegActivity.this, LoginActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                startActivity(intent);
                finish(); // 结束当前的RegisterActivity
            }
        });
    }
}

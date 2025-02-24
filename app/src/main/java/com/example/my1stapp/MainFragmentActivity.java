package com.example.my1stapp;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

public class MainFragmentActivity extends AppCompatActivity {

    private ImageButton Bt1,Bt2,Bt3,Bt4;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main_fragment);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        Bt1=findViewById(R.id.fragment_b1);
        Bt2=findViewById(R.id.fragment_b2);
        Bt3=findViewById(R.id.fragment_b3);
        Bt4=findViewById(R.id.fragment_b4);
        Bt1.setOnClickListener(new MyClickListener());
        Bt2.setOnClickListener(new MyClickListener());
        Bt3.setOnClickListener(new MyClickListener());
        Bt4.setOnClickListener(new MyClickListener());

        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.add(R.id.fragment_top,new IndexFragment())
                .commit();
    }
    private class MyClickListener implements View.OnClickListener{
        @Override
        public void onClick(View view) {
            FragmentManager fragmentManager=getSupportFragmentManager();
            FragmentTransaction fragmentTransaction=fragmentManager.beginTransaction();
            if (view.getId()==R.id.fragment_b1){
                fragmentTransaction.add(R.id.fragment_top,new IndexFragment());
            } else if (view.getId()==R.id.fragment_b2) {
                fragmentTransaction.add(R.id.fragment_top,new SecondFragment());
            } else if (view.getId()==R.id.fragment_b3) {
                fragmentTransaction.add(R.id.fragment_top,new ThirdFragment());
            } else if (view.getId()==R.id.fragment_b4) {
                fragmentTransaction.add(R.id.fragment_top,new FourthFragment());
            }
            fragmentTransaction.commit();
        }
    }
}
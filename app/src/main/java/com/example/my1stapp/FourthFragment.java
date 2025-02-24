package com.example.my1stapp;

import android.Manifest;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import static android.Manifest.permission.CALL_PHONE;
import static android.Manifest.permission.SEND_SMS;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link FourthFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FourthFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private TextView userInfor;
    private ImageButton ibCall, ibMseeage, ibPlan;
    private int LogRequestCode = 2, LogResultCode = 3;
    private int CallRequestCode = 1, SendSMSCode = 4;
    private SharedPreferences sharedPreferences;

    public FourthFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment FourthFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static FourthFragment newInstance(String param1, String param2) {
        FourthFragment fragment = new FourthFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
        // 获取SharedPreferences实例
        sharedPreferences = getActivity().getSharedPreferences("UserInfor", getActivity().MODE_PRIVATE);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_fourth, container, false);

        userInfor = v.findViewById(R.id.userinfo_UserFragment);
        ibPlan = v.findViewById(R.id.ib_Plan); // 假设你已经在XML中定义了这个ImageButton的ID

        // 检查并显示登录状态
        checkLoginStatus();

        userInfor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), LoginActivity.class);
                startActivityForResult(intent, LogRequestCode);
            }
        });

        // 日程安排跳转按钮
        ibPlan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), PlanActivity.class);
                startActivity(intent);
            }
        });

        return v;
    }

    @Override
    public void onResume() {
        super.onResume();
        checkLoginStatus();//检查登陆状态
    }

    private void checkLoginStatus() {
        // 从SharedPreferences中读取用户名
        String username = sharedPreferences.getString("username", "");
        if (!username.isEmpty()) {
            userInfor.setText(username);
            userInfor.setVisibility(View.VISIBLE);
        } else {
            userInfor.setText("未登录");
            userInfor.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == LogRequestCode && resultCode == LogResultCode) {
            String userName = data.getStringExtra("username");
            // 将用户名存储到SharedPreferences中
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putString("logUser", userName);
            editor.apply();
            checkLoginStatus();
        }
    }
}

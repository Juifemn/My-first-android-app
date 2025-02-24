package com.example.my1stapp;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link SecondFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SecondFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private ListView musicListV;
    private String[] names={"步频160","步频170","步频180"};


    public SecondFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment SecondFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static SecondFragment newInstance(String param1, String param2) {
        SecondFragment fragment = new SecondFragment();
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
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v= inflater.inflate(R.layout.fragment_second, container, false);

        musicListV=v.findViewById(R.id.music_listView);
        List<Map<String,Object>> listitem = new ArrayList<Map<String,Object>>();
        for (int i=0;i<names.length;i++){
            Map<String,Object> map = new HashMap<String,Object>();
            map.put("musicName",names[i]);
            listitem.add(map);
        }
        SimpleAdapter simpleAdapter = new SimpleAdapter(getActivity()
                                          ,listitem,R.layout.music_listitem
                                          ,new String[]{"musicName"},new int[]{R.id.listItem_musicName});
        musicListV.setAdapter(simpleAdapter);
        musicListV.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                TextView musicName = view.findViewById(R.id.listItem_musicName);
                String getMusicName=musicName.getText().toString();

                Intent intent = new Intent(getActivity().getApplicationContext(),MusicPlayActivity.class);
                Bundle bundle = new Bundle();
                bundle.putString("musicName",getMusicName);
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });


        return v;
    }
}
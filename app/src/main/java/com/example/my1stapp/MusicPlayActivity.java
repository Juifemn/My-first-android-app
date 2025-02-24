package com.example.my1stapp;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.net.Uri;
import android.media.MediaPlayer;
import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import java.io.IOException;

public class MusicPlayActivity extends AppCompatActivity {
    private TextView tvMusicName;
    private ImageView ivMusicPic;
    private ImageButton ibPlay, ibPause;
    private String[] musicNameList = {"步频150", "步频160", "步频180"};
    private int[] musicPicId = {R.drawable.qingtian, R.drawable.bandaotiehe, R.drawable.guiji};
    private int[] musicFileId = {R.raw.qingtian, R.raw.bandaotiehe, R.raw.guiji};
    private int id = 0;
    private Intent intentActivity, intentService;
    private SeekBar seekBar;
    private TextView tvCurrentTime, tvTotalTime;
    private MediaPlayer mediaPlayer;
    private Handler handler = new Handler();
    private Runnable updateSeekBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_music_play);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        tvMusicName = findViewById(R.id.musicPlay_musicName);
        ivMusicPic = findViewById(R.id.musicPlay_pic);
        ibPlay = findViewById(R.id.musicPlay_play);
        ibPause = findViewById(R.id.musicPlay_pause);
        seekBar = findViewById(R.id.musicPlay_progress);
        tvCurrentTime = findViewById(R.id.musicPlay_currentTime);
        tvTotalTime = findViewById(R.id.musicPlay_totalTime);

        intentActivity = getIntent();
        String musicName = intentActivity.getStringExtra("musicName");
        tvMusicName.setText(musicName);

        for (int i = 0; i < musicNameList.length; i++) {
            if (musicNameList[i].equals(musicName)) {
                id = i;
                break;
            }
        }
        ivMusicPic.setImageResource(musicPicId[id]);

        intentService = new Intent(MusicPlayActivity.this, MusicService.class);
        intentService.putExtra("MusicName", musicNameList[id]);
        intentService.putExtra("MusicFile", musicFileId[id]);

        // 初始化MediaPlayer
        mediaPlayer = new MediaPlayer();
        try {
            mediaPlayer.setDataSource(getApplicationContext(), Uri.parse("android.resource://" + getPackageName() + "/" + musicFileId[id]));
            mediaPlayer.prepare();
        } catch (IOException e) {
            e.printStackTrace();
        }

        // 设置进度条的最大值为音频的总时长
        seekBar.setMax(mediaPlayer.getDuration());
        tvTotalTime.setText(getFormattedTime(mediaPlayer.getDuration()));

        // 更新进度条
        updateSeekBar = new Runnable() {
            @Override
            public void run() {
                if (mediaPlayer != null) {
                    int currentPosition = mediaPlayer.getCurrentPosition();
                    seekBar.setProgress(currentPosition);
                    tvCurrentTime.setText(getFormattedTime(currentPosition));
                    handler.postDelayed(this, 1000);
                }
            }
        };

        // 开始更新进度条
        handler.postDelayed(updateSeekBar, 1000);

        // 进度条的拖动监听器
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (fromUser) {
                    mediaPlayer.seekTo(progress);
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                // 拖动开始时暂停更新进度条
                handler.removeCallbacks(updateSeekBar);
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                // 拖动结束后恢复更新进度条
                handler.postDelayed(updateSeekBar, 1000);
            }
        });

        ibPlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                intentService.putExtra("Action", "play");
                startService(intentService);
                ibPlay.setVisibility(View.INVISIBLE);
                ibPause.setVisibility(View.VISIBLE);
            }
        });

        ibPause.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                intentService.putExtra("Action", "pause");
                startService(intentService);
                ibPause.setVisibility(View.INVISIBLE);
                ibPlay.setVisibility(View.VISIBLE);
            }
        });
    }

    // 毫秒转换为格式化
    private String getFormattedTime(int milliseconds) {
        int seconds = (milliseconds / 1000) % 60;
        int minutes = (milliseconds / (1000 * 60)) % 60;
        return String.format("%02d:%02d", minutes, seconds);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mediaPlayer != null) {
            mediaPlayer.stop();
            mediaPlayer.release();
            mediaPlayer = null;
        }
        handler.removeCallbacks(updateSeekBar);
        stopService(intentService);
    }
}

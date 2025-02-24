package com.example.my1stapp;

import android.app.Service;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.media.MediaPlayer;
import android.util.Log;

public class MusicService extends Service {
    private MediaPlayer mp;
    private Handler handler = new Handler();
    private Runnable updateSeekBar;

    public MusicService() {
    }

    @Override
    public void onCreate() {
        super.onCreate();
        mp = new MediaPlayer();
        // 初始化进度条更新
        updateSeekBar = new Runnable() {
            @Override
            public void run() {
                if (mp != null) {
                    int currentPosition = mp.getCurrentPosition();
                    // 更新进度条
                    Intent intent = new Intent("UPDATE_SEEKBAR");
                    intent.putExtra("currentPosition", currentPosition);
                    sendBroadcast(intent);
                    handler.postDelayed(this, 1000);
                }
            }
        };
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        String musicName = intent.getStringExtra("MusicName");
        int musicFileID = intent.getIntExtra("MusicFile", 0);
        String action = intent.getStringExtra("Action");

        switch (action) {
            case "play":
                try {
                    if (mp == null) {
                        mp = MediaPlayer.create(this, musicFileID);
                    }
                    mp.start();
                    // 开始更新进度条
                    handler.postDelayed(updateSeekBar, 1000);
                    Log.d("MusicService", "Playing music: " + musicName + " with ID: " + musicFileID);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
            case "pause":
                if (mp != null && mp.isPlaying()) {
                    mp.pause();
                    // 暂停更新进度条
                    handler.removeCallbacks(updateSeekBar);
                }
                break;
            case "seek":
                int seekPosition = intent.getIntExtra("SeekPosition", 0);
                if (mp != null) {
                    mp.seekTo(seekPosition);
                }
                break;
        }

        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mp != null) {
            mp.stop();
            mp.release();
            mp = null;
        }
        // 停止更新进度条
        handler.removeCallbacks(updateSeekBar);
    }

    // 获取当前播放位置
    public int getCurrentPosition() {
        if (mp != null) {
            return mp.getCurrentPosition();
        }
        return 0;
    }

    // 获取总时长
    public int getDuration() {
        if (mp != null) {
            return mp.getDuration();
        }
        return 0;
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }
}

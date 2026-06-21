package com.guanyintang.fourjingwen;

import android.app.Activity;
import android.media.MediaPlayer;
import android.media.PlaybackParams;
import android.os.Bundle;
import android.view.Gravity;
import android.widget.*;
import android.view.View;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.GradientDrawable;
import java.util.*;

public class MainActivity extends Activity {
    private MediaPlayer player;
    private int currentRes = 0;
    private float currentSpeed = 1.0f;

    static class Track {
        String title;
        int resId;
        float[] speeds;
        Track(String title, int resId, float[] speeds) {
            this.title = title; this.resId = resId; this.speeds = speeds;
        }
    }

    private final ArrayList<Track> tracks = new ArrayList<>();

    @Override public void onCreate(Bundle b) {
        super.onCreate(b);

        tracks.add(new Track("大悲咒", R.raw.dabeizhou, new float[]{1.0f,1.3f,1.5f,1.8f,2.0f,2.3f}));
        tracks.add(new Track("心经", R.raw.xinjing, new float[]{1.0f,1.3f,1.5f,1.8f,2.0f}));
        tracks.add(new Track("往生咒", R.raw.wangshengzhou, new float[]{1.0f,1.3f,1.5f,1.8f,2.0f}));
        tracks.add(new Track("七佛灭罪真言", R.raw.qifo, new float[]{1.0f,1.3f,1.5f,1.8f,2.0f}));

        ScrollView scroll = new ScrollView(this);
        LinearLayout root = new LinearLayout(this);
        root.setOrientation(LinearLayout.VERTICAL);
        root.setPadding(28, 28, 28, 40);
        root.setGravity(Gravity.CENTER_HORIZONTAL);
        root.setBackgroundColor(Color.rgb(255,250,242));
        scroll.addView(root);

        TextView title = new TextView(this);
        title.setText("四部经文播放器");
        title.setTextSize(26);
        title.setTypeface(null, Typeface.BOLD);
        title.setGravity(Gravity.CENTER);
        root.addView(title, new LinearLayout.LayoutParams(-1, -2));

        TextView sub = new TextView(this);
        sub.setText("完全离线 · 循环播放 · 可调速度");
        sub.setTextSize(16);
        sub.setTextColor(Color.rgb(100,100,100));
        sub.setGravity(Gravity.CENTER);
        sub.setPadding(0, 12, 0, 18);
        root.addView(sub, new LinearLayout.LayoutParams(-1, -2));

        for (Track t : tracks) addTrackCard(root, t);
        setContentView(scroll);
    }

    private void addTrackCard(LinearLayout root, Track t) {
        LinearLayout card = new LinearLayout(this);
        card.setOrientation(LinearLayout.VERTICAL);
        card.setPadding(20, 20, 20, 20);
        card.setGravity(Gravity.CENTER_HORIZONTAL);

        GradientDrawable bg = new GradientDrawable();
        bg.setColor(Color.WHITE);
        bg.setStroke(2, Color.rgb(225,205,160));
        bg.setCornerRadius(22);
        card.setBackground(bg);

        LinearLayout.LayoutParams cp = new LinearLayout.LayoutParams(-1, -2);
        cp.setMargins(0, 16, 0, 16);
        root.addView(card, cp);

        TextView tv = new TextView(this);
        tv.setText(t.title);
        tv.setTextSize(22);
        tv.setTypeface(null, Typeface.BOLD);
        tv.setGravity(Gravity.CENTER);
        card.addView(tv, new LinearLayout.LayoutParams(-1, -2));

        TextView speedLabel = new TextView(this);
        speedLabel.setText("速度：1.0x");
        speedLabel.setTextSize(15);
        speedLabel.setTextColor(Color.rgb(90,90,90));
        speedLabel.setGravity(Gravity.CENTER);
        speedLabel.setPadding(0, 8, 0, 8);
        card.addView(speedLabel, new LinearLayout.LayoutParams(-1, -2));

        LinearLayout row = new LinearLayout(this);
        row.setGravity(Gravity.CENTER);
        row.setOrientation(LinearLayout.HORIZONTAL);
        card.addView(row);

        Button play = new Button(this);
        play.setText("播放");
        row.addView(play);
        Button pause = new Button(this);
        pause.setText("暂停");
        row.addView(pause);
        Button restart = new Button(this);
        restart.setText("重播");
        row.addView(restart);

        LinearLayout speedRow = new LinearLayout(this);
        speedRow.setGravity(Gravity.CENTER);
        speedRow.setOrientation(LinearLayout.HORIZONTAL);
        speedRow.setPadding(0, 12, 0, 0);
        card.addView(speedRow, new LinearLayout.LayoutParams(-1, -2));

        for (float s : t.speeds) {
            Button b = new Button(this);
            b.setText(String.format(Locale.US, "%.1fx", s));
            b.setOnClickListener(v -> {
                currentSpeed = s;
                speedLabel.setText(String.format(Locale.US, "速度：%.1fx", s));
                if (player != null && currentRes == t.resId) applySpeed();
            });
            speedRow.addView(b);
        }

        play.setOnClickListener(v -> playTrack(t.resId));
        pause.setOnClickListener(v -> { if (player != null && player.isPlaying()) player.pause(); });
        restart.setOnClickListener(v -> { playTrack(t.resId); if (player != null) player.seekTo(0); });
    }

    private void playTrack(int resId) {
        if (player == null || currentRes != resId) {
            if (player != null) {
                player.stop();
                player.release();
            }
            player = MediaPlayer.create(this, resId);
            currentRes = resId;
            player.setLooping(true);
        }
        applySpeed();
        player.start();
    }

    private void applySpeed() {
        if (player == null) return;
        try {
            PlaybackParams params = player.getPlaybackParams();
            params.setSpeed(currentSpeed);
            player.setPlaybackParams(params);
        } catch (Exception e) {
            Toast.makeText(this, "此手机系统不支持调速", Toast.LENGTH_SHORT).show();
        }
    }

    @Override protected void onDestroy() {
        if (player != null) {
            player.release();
            player = null;
        }
        super.onDestroy();
    }
}

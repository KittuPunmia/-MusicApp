package com.kittu.mediaplayer;

import android.content.pm.ActivityInfo;
import android.os.IBinder;
import android.support.v7.widget.RecyclerView;
        import android.Manifest;
        import android.content.pm.PackageManager;
        import android.database.Cursor;
        import android.media.MediaPlayer;
        import android.net.Uri;
        import android.os.Build;
        import android.os.Bundle;
        import android.os.Handler;
        import android.provider.MediaStore;
        import android.support.annotation.NonNull;
        import android.support.v4.app.ActivityCompat;
        import android.support.v7.app.AppCompatActivity;
        import android.support.v7.widget.DividerItemDecoration;
        import android.support.v7.widget.LinearLayoutManager;
        import android.support.v7.widget.RecyclerView;
        import android.util.Log;
        import android.view.View;
        import android.widget.Button;
        import android.widget.SeekBar;
        import android.widget.Toast;

import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.appindexing.Thing;
import com.google.android.gms.common.api.GoogleApiClient;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private ArrayList<SongModel> _songs = new ArrayList<SongModel>();
    ;
    RecyclerView recycle;
    SeekBar seek;
    SongAdapter songAdapter;
    boolean pause = false;
    MediaPlayer mediaPlayer;
    private Handler myHandler = new Handler();
    int orientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT;
    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    private GoogleApiClient client;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        recycle = (RecyclerView) findViewById(R.id.recycle);
        seek = (SeekBar) findViewById(R.id.seek);
        songAdapter = new SongAdapter(this, _songs);
        recycle.setAdapter(songAdapter);
        final LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(recycle.getContext(),
                linearLayoutManager.getOrientation());
        recycle.setLayoutManager(linearLayoutManager);
        recycle.addItemDecoration(dividerItemDecoration);
        setRequestedOrientation(orientation);
        songAdapter.setOnItemClickListener(new SongAdapter.OnItemClickListener() {
            @Override
            public void OnItemClick(final Button b, View v, final SongModel obj, final int pos) {
                if (b.getText().equals("Stop")) {
                    mediaPlayer.stop();
                    mediaPlayer.reset();
                    mediaPlayer.release();
                    mediaPlayer = null;
                        b.setText("Play");
                        pause = false;


                } else {

                    Runnable runnable = new Runnable() {


                        @Override
                        public void run() {
                            try {

                                if (pause == false) {
                                    mediaPlayer = new MediaPlayer();
                                    mediaPlayer.setDataSource(obj.getSongUrl());
                                    mediaPlayer.prepareAsync();
                                    mediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                                        @Override
                                        public void onPrepared(MediaPlayer mp) {
                                            mp.start();
                                            seek.setProgress(0);
                                            seek.setMax(mediaPlayer.getDuration());
                                            Log.d("Prog", "run: " + mediaPlayer.getDuration());
                                            b.setText("STOP");
                                        }
                                    });

                                    pause = true;
                                } else {
                                            if (mediaPlayer.isPlaying()) {
                                        mediaPlayer.stop();
                                        mediaPlayer.reset();
                                        mediaPlayer.release();
                                                b.setText("PLAY");


                                    }
                                        mediaPlayer = new MediaPlayer();
                                    mediaPlayer.setDataSource(obj.getSongUrl());
                                        mediaPlayer.prepareAsync();
                                        mediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                                            @Override
                                            public void onPrepared(MediaPlayer mp) {
                                                mp.start();
                                                seek.setProgress(0);
                                                seek.setMax(mediaPlayer.getDuration());
                                                Log.d("Prog", "run: " + mediaPlayer.getDuration());
                                            }
                                        });
                                        b.setText("Stop");
                                        pause = true;



                                }

                            } catch (Exception e) {
                            }
                        }

                    };
                    myHandler.postDelayed(runnable, 100);

                }
            }

        });


        checkUserPermission();

        Thread t = new runThread();
        t.start();
        }


    public class runThread extends Thread {


        @Override
        public void run() {
            while (true) {

                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                Log.d("Runwa", "run: " + 1);
                if (mediaPlayer != null) {
                    seek.post(new Runnable() {
                        @Override
                        public void run() {
                            seek.setProgress(mediaPlayer.getCurrentPosition());

                        }
                    });

                    Log.d("Runwa", "run: " + mediaPlayer.getCurrentPosition());
                }

            }
        }

    }

    private void checkUserPermission() {
        if (Build.VERSION.SDK_INT >= 23) {
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE)
                    != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 123);
                return;
            }
        }
        loadSongs();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case 123:
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    loadSongs();
                } else {
                    Toast.makeText(this, "Permission Denied", Toast.LENGTH_SHORT).show();
                    checkUserPermission();
                }
                break;
            default:
                super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        }

    }

    private void loadSongs() {
        Uri uri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
        String selection = MediaStore.Audio.Media.IS_MUSIC + "!=0";
        Cursor cursor = getContentResolver().query(uri, null, selection, null, null);
        if (cursor != null) {
            if (cursor.moveToFirst()) {
                do {
                    String name = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.DISPLAY_NAME));
                    String artist = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.ARTIST));
                    String url = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.DATA));

                    SongModel s = new SongModel(name, artist, url);
                    _songs.add(s);

                } while (cursor.moveToNext());
            }

            cursor.close();
            songAdapter = new SongAdapter(MainActivity.this, _songs);

        }
    }


    @Override
    protected void onPause() {
        super.onPause();

        if (mediaPlayer.isPlaying()) {
            mediaPlayer.pause();
        }
    }


}



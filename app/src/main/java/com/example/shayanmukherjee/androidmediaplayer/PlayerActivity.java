package com.example.shayanmukherjee.androidmediaplayer;

import android.content.Intent;
import android.graphics.PorterDuff;
import android.media.MediaPlayer;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;


import java.io.File;
import java.util.ArrayList;

public class PlayerActivity extends AppCompatActivity {
    Button btn_next, btn_previous, btn_pause,btn_repeat,btn_repeat_one;
    TextView songTextLabel;
    ImageView image;
    SeekBar songSeekbar;

    static MediaPlayer myMediaPlayer;
    String sname;
    int position;
    ArrayList<File> mySongs;
    Thread updateseekBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_player);
        image=(ImageView)findViewById(R.id.songImage);
        btn_repeat_one=(Button)findViewById(R.id.repeat_one);
        btn_next = (Button) findViewById(R.id.next);
        btn_previous = (Button) findViewById(R.id.previous);
        btn_pause = (Button) findViewById(R.id.pause);
        btn_repeat=(Button)findViewById(R.id.repeat);

        songTextLabel = (TextView) findViewById(R.id.songLabel);

        songSeekbar = (SeekBar) findViewById(R.id.seekBar);

        getSupportActionBar().setTitle("Now Playing");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        updateseekBar = new Thread() {
            @Override
            public void run() {

                int totalDuration = myMediaPlayer.getDuration();
                int currentPosition = 0;

                while (currentPosition < totalDuration) {
                    try {
                        sleep(500);
                        currentPosition = myMediaPlayer.getCurrentPosition();
                        songSeekbar.setProgress(currentPosition);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        };

        if(myMediaPlayer!=null)
        {
            myMediaPlayer.stop();
            myMediaPlayer.release();
        }
        Intent i=getIntent();
        Bundle bundle=i.getExtras();

        mySongs=(ArrayList)bundle.getParcelableArrayList("songs");

        sname=mySongs.get(position).getName().toString();

        String songName=i.getStringExtra("songname");

        songTextLabel.setText(songName);
        songTextLabel.setSelected(true);

        position = bundle.getInt("pos",0);

        Uri u= Uri.parse(mySongs.get(position).toString());

        myMediaPlayer=MediaPlayer.create(getApplicationContext(),u);

        myMediaPlayer.start();
        songSeekbar.setMax(myMediaPlayer.getDuration());

        updateseekBar.start();
        songSeekbar.getProgressDrawable().setColorFilter(getResources().getColor(R.color.colorPrimary), PorterDuff.Mode.MULTIPLY);

        songSeekbar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                myMediaPlayer.seekTo(seekBar.getProgress());
            }
        });


        btn_pause.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                songSeekbar.setMax(myMediaPlayer.getDuration());

                if(myMediaPlayer.isPlaying())
                {
                    btn_pause.setBackgroundResource(R.drawable.icon_play);
                    myMediaPlayer.pause();
                }
                else
                {
                    btn_pause.setBackgroundResource(R.drawable.icon_pause);
                    myMediaPlayer.start();
                }
            }
        });

        btn_repeat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                songSeekbar.setMax(myMediaPlayer.getDuration());

                if(myMediaPlayer.isPlaying())
                {
                    Toast.makeText(getApplicationContext(),"This song is't on repeating mode",Toast.LENGTH_SHORT).show();
                    myMediaPlayer.setLooping(false);
                    btn_repeat.setBackgroundResource(R.drawable.icon_repeat_color);
                    btn_repeat_one.setBackgroundResource(R.drawable.icon_repeat_one);
                }
            }
        });
        btn_repeat_one.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                songSeekbar.setMax(myMediaPlayer.getDuration());

                if(myMediaPlayer.isPlaying())
                {
                    Toast.makeText(getApplicationContext(),"This song is on repeating mode",Toast.LENGTH_SHORT).show();
                    myMediaPlayer.setLooping(true);
                    btn_repeat_one.setBackgroundResource(R.drawable.icon_repeat_one_color);
                    btn_repeat.setBackgroundResource(R.drawable.icon_repeat);
                }
            }
        });

        btn_next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                songSeekbar.setMax(myMediaPlayer.getDuration());

                if(position==mySongs.size()-1){
                    Toast.makeText(getApplicationContext(),"No More Next Songs",Toast.LENGTH_SHORT).show();
                }

                else {
                    btn_repeat.setBackgroundResource(R.drawable.icon_repeat);
                    btn_repeat_one.setBackgroundResource(R.drawable.icon_repeat_one);
                    myMediaPlayer.stop();
                    myMediaPlayer.release();
                    position = ((position + 1) % mySongs.size());

                    Uri u = Uri.parse(mySongs.get(position).toString());

                    myMediaPlayer = MediaPlayer.create(getApplicationContext(), u);

                    sname = mySongs.get(position).getName().toString();
                    songTextLabel.setText(sname);

                    myMediaPlayer.start();
                }
            }
        });

        btn_previous.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                songSeekbar.setMax(myMediaPlayer.getDuration());
                if(position==0){
                    Toast.makeText(getApplicationContext(),"No More Previous Songs",Toast.LENGTH_SHORT).show();
                }

                else {
                    btn_repeat.setBackgroundResource(R.drawable.icon_repeat);
                    btn_repeat_one.setBackgroundResource(R.drawable.icon_repeat_one);
                    myMediaPlayer.stop();
                    myMediaPlayer.release();

                    position = ((position - 1) < 0) ? (mySongs.size() - 1) : (position - 1);
                    Uri u = Uri.parse(mySongs.get(position).toString());
                    myMediaPlayer = MediaPlayer.create(getApplicationContext(), u);

                    sname = mySongs.get(position).getName().toString();
                    songTextLabel.setText(sname);

                    myMediaPlayer.start();
                }
            }
        });

    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId())
        {
            case R.id.facebook:
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("http://www.fb.com/shayan.mukherjee.359")));
                break;

            case R.id.insta:
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.instagram.com/ace_shayan/")));
                break;

            case R.id.git:
                startActivity(new Intent(Intent.ACTION_VIEW,Uri.parse("https://github.com/Shayan43211234")));
                break;
            case R.id.exit:
                myMediaPlayer.stop();
                finish();
                break;
        }

        if(item.getItemId() == android.R.id.home)
        {
            onBackPressed();
        }

        return super.onOptionsItemSelected(item);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.option_menu, menu);
        return true;
    }

}
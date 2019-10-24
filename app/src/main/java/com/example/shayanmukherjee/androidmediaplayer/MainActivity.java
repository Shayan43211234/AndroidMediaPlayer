package com.example.shayanmukherjee.androidmediaplayer;

import android.Manifest;
import android.content.Intent;
import android.net.Uri;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;

public class MainActivity extends AppCompatActivity {
    ListView myListViewForSongs;
    String[] items;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        myListViewForSongs=(ListView)findViewById(R.id.mySongListView);
        runtimePermission();
        registerForContextMenu(myListViewForSongs);
    }
    public void runtimePermission()
    {
        Dexter.withActivity(this).withPermission(Manifest.permission.READ_EXTERNAL_STORAGE).withListener(new PermissionListener() {
            @Override
            public void onPermissionGranted(PermissionGrantedResponse response) {
                display();
            }

            @Override
            public void onPermissionDenied(PermissionDeniedResponse response) {
                finish();
            }

            @Override
            public void onPermissionRationaleShouldBeShown(PermissionRequest permission, PermissionToken token) {
                token.continuePermissionRequest();
            }
        }).check();
    }
    public ArrayList<File> findSong(File file)
    {
        ArrayList<File> arrayList=new ArrayList<>();
        File[] files=file.listFiles();
        for(File singleFile: files)
        {
            if(singleFile.isDirectory() && !singleFile.isHidden())
            {
                arrayList.addAll(findSong(singleFile));
            }
            else
            {
                if(singleFile.getName().endsWith(".mp3") || singleFile.getName().endsWith(".wav"))
                {
                    arrayList.add(singleFile);
                }
            }
        }
        Collections.sort(arrayList);
        return arrayList;
    }

    void display()
    {
        final ArrayList<File> mySongs=findSong(Environment.getExternalStorageDirectory());
        items=new String[mySongs.size()];
        for (int i=0;i<mySongs.size();i++)
        {
            items[i]=mySongs.get(i).getName().toString().replace(".mp3","").replace(".wav","").replace("._","");
        }
        ArrayAdapter<String> myAdapter=new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,items);
        myListViewForSongs.setAdapter(myAdapter);
        myListViewForSongs.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String songName=myListViewForSongs.getItemAtPosition(position).toString();
                startActivity(new Intent(getApplicationContext(),PlayerActivity.class)
                        .putExtra("songs",mySongs).putExtra("songname",songName)
                        .putExtra("pos",position));
            }
        });
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.option_menu, menu);
        return true;
    }
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
                finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}
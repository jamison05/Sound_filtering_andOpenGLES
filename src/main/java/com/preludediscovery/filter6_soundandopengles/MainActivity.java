package com.preludediscovery.filter6_soundandopengles;

import android.content.Intent;
import android.media.AudioFormat;
import android.media.AudioManager;
import android.media.AudioTrack;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;

import java.io.IOException;
import java.io.InputStream;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        Intent intent = new Intent(this, Soundfiltering_Service.class);

//       for (int i= 0; i <5; i++) {
//           startService(intent);
//        try {
//            Thread.sleep(1000);
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }
//    }

            playSound();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


     //  AudioTrack audioTrack;



    private  float[] bytesToFloats(byte[] bytes) {
        float[] floats = new float[bytes.length / 2];
        for(int i=0; i < bytes.length; i+=2) {
            floats[i] = bytes[i]/32768;

        }
        return floats;
    }

    float[][] large_file = new float[1000][512];

    private void playSound(){

        int i;
        int bufferSize = 512;
        float [] f_buffer = new float[bufferSize];
        byte [] buffer = new byte[bufferSize];
        int num_iterate=0;
//
//        // Convert the buffer to floats. (before resampling)
//        float div = (1.0f/32768.0f);
//        for( int i = 0; i < SZ; i++) {
//            pFloatBuf[i] = div * (float)pIntBuf1[i];
//        }
        InputStream inputStream = getResources().openRawResource(R.raw.sample_audiowaveread_4);
       int k=0;
        try {
            while((i = inputStream.read(buffer)) != -1) {
                // audioTrack.write(buffer, 0, i)
                large_file[k++]=bytesToFloats(buffer);
            }

        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        try {
            inputStream.close();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }



        AudioTrack audioTrack= new AudioTrack(AudioManager.STREAM_MUSIC, 44100,AudioFormat.CHANNEL_OUT_MONO,
                AudioFormat.ENCODING_PCM_FLOAT,400000, AudioTrack.MODE_STATIC);

           for (int itr1 = 0; itr1 < k; itr1++) {

               audioTrack.write(large_file[itr1], 0, 512, AudioTrack.WRITE_BLOCKING);
               audioTrack.play();


           }


    }
}

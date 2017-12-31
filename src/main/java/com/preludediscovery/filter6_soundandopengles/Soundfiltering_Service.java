package com.preludediscovery.filter6_soundandopengles;

import android.app.Fragment;
import android.app.IntentService;
import android.app.Service;
import android.content.Intent;
import android.content.Context;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.IBinder;
import android.os.Looper;
import android.os.Message;
import android.widget.Toast;

/**
 * An {@link IntentService} subclass for handling asynchronous task requests in
 * a service on a separate handler thread.
 * <p>
 * TODO: Customize class - update intent actions, extra parameters and static
 * helper methods.
 */
public class Soundfiltering_Service extends Service {
    private Looper mServiceLooper;
    private ServiceHandler mServiceHandler;
    private Object mPauseLock;
    public boolean mPaused;
    private boolean mFinished;
    // Handler that receives messages from the thread
    private final class ServiceHandler extends Handler {
        public ServiceHandler(Looper looper) {
            super(looper);
        }
        @Override
        public void handleMessage(Message msg) {
            // Normally we would do some work here, like download a file.
            // For our sample, we just sleep for 5 seconds.
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                // Restore interrupt status.
                Thread.currentThread().interrupt();
            }
            // Stop the service using the startId, so that we don't stop
            // the service in the middle of handling another job
            stopSelf(msg.arg1);
        }
    }

    @Override
    public void onCreate() {
        // Start up the thread running the service.  Note that we create a
        // separate thread because the service normally runs in the process's
        // main thread, which we don't want to block.  We also make it
        // background priority so CPU-intensive work will not disrupt our UI.
        HandlerThread thread = new HandlerThread("ServiceStartArguments");
        mPauseLock= new Object();
        mPaused = false;
        mFinished= false;
        thread.start();


        // Get the HandlerThread's Looper and use it for our Handler
        mServiceLooper = thread.getLooper();
        mServiceHandler = new ServiceHandler(mServiceLooper);
    }
public int iter_1=0;
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        iter_1++;
        Toast.makeText(this, "service starting " + iter_1, Toast.LENGTH_SHORT).show();


        //Here is where if the onPause method is called then the function goes to sleep.

        synchronized (mPauseLock){
            while (mPaused){
                try{
                    mPauseLock.wait();
                }catch(InterruptedException e) {

                }
                }
            }
        onPause();
        onResume();



        // For each start request, send a message to start a job and deliver the
        // start ID so we know which request we're stopping when we finish the job
        Message msg = mServiceHandler.obtainMessage();
        msg.arg1 = startId;
        mServiceHandler.sendMessage(msg);

        // If we get killed, after returning from here, restart
        return START_STICKY;
    }
    public void onPause(){
        synchronized(mPauseLock) {

            mPaused= true;
        }
    }

    public void onResume(){

        synchronized (mPauseLock){

            mPaused= false;
            mPauseLock.notifyAll();
        }
    }

    @Override
    public IBinder onBind(Intent intent) {
        // We don't provide binding, so return null
        return null;
    }

    @Override
    public void onDestroy() {
        Toast.makeText(this, "service done", Toast.LENGTH_SHORT).show();
    }
}

package com.preludediscovery.filter6_soundandopengles;

import android.app.Fragment;
import android.app.IntentService;
import android.app.Service;
import android.content.Intent;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.IBinder;
import android.os.Looper;
import android.os.Message;
import android.support.annotation.NonNull;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import static android.content.ContentValues.TAG;

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
    public Context context1;
    void loadg() {


        String filename = "imajges6589786987767655.jpg";
        File filesDir = getFilesDir();
        save_file_dir=filesDir.getAbsolutePath();
        try {
            loadBitmap(context1, filename);
            downloadFile(src2,filesDir.getAbsolutePath());
        }catch(Exception ex){
            int filefailure =1;
        }
        String string = "Hello world!";

    }


    private StorageReference mStorageRef;
    FirebaseStorage storage = FirebaseStorage.getInstance();
    String src1 ="https://storage.googleapis.com/trialobject/1920x1200_wallpaper_hubble.jpg";
    String src2 = "https://storage.googleapis.com/trialobject/step5_2.mp3";
    String save_file_dir= "";

    public void getBitmapFromURL() {






        Runnable task1 = new Runnable(){

            @Override
            public void run(){
                try {
                    java.net.URL url = new java.net.URL(src1);
                    HttpURLConnection connection = (HttpURLConnection) url
                            .openConnection();
                    connection.setDoInput(true);
                    connection.connect();
                    InputStream input = connection.getInputStream();
                    Bitmap myBitmap = BitmapFactory.decodeStream(input);

                    String filename = "imajges6589786987767655.jpg";
                    //FileOutputStream outputStream;
                    saveFile(context1,myBitmap,filename);
//						try {
//							outputStream = openFileOutput(filename, Context.MODE_PRIVATE);
//							outputStream.write(input.);
//							outputStream.close();
//						} catch (Exception e) {
//							e.printStackTrace();
//						}



                    // return myBitmap;
                } catch (IOException e) {
                    e.printStackTrace();
                    // return null;
                }
            }
        };


        Thread thread1 = new Thread(task1);
        thread1.start();







    }

    private static final int BUFFER_SIZE = 4096;

    /**
     * Downloads a file from a URL
     * @param fileURL HTTP URL of the file to be downloaded
     * @param saveDir path of the directory to save the file
     * @throws IOException
     */
    public void downloadFile(String fileURL, String saveDir)
            throws IOException {


        Runnable task1 = new Runnable(){

            @Override
            public void run(){
                try {
                    URL url = new URL(src2);
                    HttpURLConnection httpConn = (HttpURLConnection) url.openConnection();
                    int responseCode = httpConn.getResponseCode();

                    //  always check HTTP response code first
                    if (responseCode == HttpURLConnection.HTTP_OK) {
                        String fileName = "";
                        String disposition = httpConn.getHeaderField("Content-Disposition");
                        String contentType = httpConn.getContentType();
                        int contentLength = httpConn.getContentLength();

                        if (disposition != null) {
                            // extracts file name from header field
                            int index = disposition.indexOf("filename=");
                            if (index > 0) {
                                fileName = disposition.substring(index + 10,
                                        disposition.length() - 1);
                            }
                        } else {
                            // extracts file name from URL
                            fileName = src2;
                        }

                        System.out.println("Content-Type = " + contentType);
                        System.out.println("Content-Disposition = " + disposition);
                        System.out.println("Content-Length = " + contentLength);
                        System.out.println("fileName = " + fileName);

                        // opens input stream from the HTTP connection
                        InputStream inputStream = httpConn.getInputStream();
                        String saveFilePath = save_file_dir + "file1.mp3";

                        // opens an output stream to save into file
                        FileOutputStream outputStream = new FileOutputStream(saveFilePath);

                        int bytesRead = -1;
                        byte[] buffer = new byte[BUFFER_SIZE];
                        while ((bytesRead = inputStream.read(buffer)) != -1) {
                            outputStream.write(buffer, 0, bytesRead);
                        }

                        outputStream.close();
                        inputStream.close();

                        System.out.println("File downloaded");
                    } else {
                        System.out.println("No file to download. Server replied HTTP code: " + responseCode);
                    }
                    httpConn.disconnect();
                } catch (IOException e) {
                    e.printStackTrace();
                    // return null;
                }
            }
        };



        Thread thread1 = new Thread(task1);
        thread1.start();
    }

    FileInputStream fis;
    FileOutputStream fos;
    public  void saveFile(Context context, Bitmap b, String picName){

        try {
            fos = context.openFileOutput(picName, Context.MODE_PRIVATE);
            b.compress(Bitmap.CompressFormat.PNG, 100, fos);
        }
        catch (FileNotFoundException e) {
            Log.d(TAG, "file not found");
            e.printStackTrace();
        }
        catch (IOException e) {
            Log.d(TAG, "io exception");
            e.printStackTrace();
        } finally {
            try {
                fos.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
    public Bitmap loadBitmap(Context context, String picName){
        Bitmap b = null;

        try {
            fis = context.openFileInput(picName);
            b = BitmapFactory.decodeStream(fis);
        }
        catch (FileNotFoundException e) {
            Log.d(TAG, "file not found");
            e.printStackTrace();
        }
        catch (IOException e) {
            Log.d(TAG, "io exception");
            e.printStackTrace();
        } finally {
            try {
                fis.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return b;
    }

    public void getimagefile() {


        mStorageRef = storage.getReference();
        StorageReference m1StorageRef = mStorageRef.child("images1/Untitled-16.jpg");

        final long ONE_MEGABYTE = 1024 * 1024;
        m1StorageRef.getBytes(ONE_MEGABYTE).addOnSuccessListener(new OnSuccessListener<byte[]>() {
            @Override
            public void onSuccess(byte[] bytes) {
                int x =1;

                // Data for "images/island.jpg" is returns, use this as needed
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                int x =8;
// Handle any errors
            }
        });

        StorageReference m1StorageRef2 = mStorageRef.child("images1/Untitled-16.jpg");
        m1StorageRef2 = mStorageRef.child("images1/Untitled-16.jpg");




        File localFile = null;
        try {
            localFile = File.createTempFile("images6555", "jpg");
        } catch (IOException e) {
            e.printStackTrace();
        }

        m1StorageRef2.getFile(localFile).addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                int x =1;
                //
                // Local temp file has been created
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {

                int x =8;


                // Handle any errors
            }
        });


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

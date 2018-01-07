package com.preludediscovery.filter6_soundandopengles;

import android.os.AsyncTask;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.provider.Telephony;

import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by deepj on 1/6/2018.
 */




public class Download_files_collections implements Runnable {
        Handler mHandler;
        private static final int BUFFER_SIZE = 4096;
        private StorageReference mStorageRef;
        FirebaseStorage storage = FirebaseStorage.getInstance();
        String src1 = "https://storage.googleapis.com/trialobject/1920x1200_wallpaper_hubble.jpg";
        String src2 = "https://storage.googleapis.com/trialobject/step5_2.mp3";
        String save_file_dir = "";
        String[] str1_array = new String[100];  //May add the URL links from another class


        // Defines a Handler object that's attached to the UI thread
        public int current_activity = 0;  //This will be accessed outside of the class
        public int isAuthorized;

        void initialize_handler() {
                mHandler = new Handler(Looper.getMainLooper());

        }


        @Override
        public void run() {
                //OOP programming to avoid sending messages.

                if (get_current_activity(current_activity) == 1) {
                        run_downoad_sequence();
                }
                ;
                // This will check the current actiuvity
                //Authorization may be gained from if user purchased particular part of the game.

        }

        public int get_current_activity(int get_activity1) {
                int return_authorization = 0;
                if (isAuthorized == 1) {
                        return_authorization = 1;
                } else {
                        return_authorization = -1;
                }

                return return_authorization;
        }

        public void run_downoad_sequence(){
                try {
                        downloadFile(str1_array[current_activity], str1_array[current_activity]);
                }catch(Exception ex){

                }
        }

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

}

package com.gallery.tclgallery.utils;

import android.content.Context;
import android.os.AsyncTask;


import com.gallery.tclgallery.bean.AlbumFile;
import com.gallery.tclgallery.bean.AlbumFolder;

import java.util.ArrayList;


/**
 * Created by liddo on 2017/9/22.
 */

public class MediaScanTask extends AsyncTask<ArrayList<AlbumFile>, Void, ArrayList<AlbumFolder>> {

    public interface Callback {
        /**
         * Album scan result
         * @param folders
         */
        void onScanCallback(ArrayList<AlbumFolder> folders);
    }

    private Context mContext;
    private int function;
    private Callback callback;
    private ArrayList<AlbumFile> mCheckedFile;

    public MediaScanTask(Context mContext, int function, Callback callback, ArrayList<AlbumFile> mCheckedFile) {
        this.mContext = mContext;
        this.function = function;
        this.callback = callback;
        this.mCheckedFile = mCheckedFile;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        /**
         * can show scan waiting dialog
         */
    }

    @Override
    protected void onPostExecute(ArrayList<AlbumFolder> folders) {
        super.onPostExecute(folders);
        callback.onScanCallback(folders);
    }

    @Override
    protected ArrayList<AlbumFolder> doInBackground(ArrayList<AlbumFile>... arrayLists) {
        ArrayList<AlbumFolder> albumFolders;
        switch (function) {
            case 0:
                albumFolders = new MediaReader(mContext).getAllImage();
                break;
            case 1:
                albumFolders = new MediaReader(mContext).getAllVideo();
                break;
            case 2:
            default:
                albumFolders = new MediaReader(mContext).getAllMedia();
                break;
        }

        //ArrayList<AlbumFile> checkFile = arrayLists[0];
        //if (checkFile != null && checkFile.size()>0){
        //    List<AlbumFile> albumFiles = albumFolders.get(0).getmAlbumFile();
        //    for (AlbumFile checkAlbumFile: checkFile) {
        //        for (int i=0; i < albumFiles.size(); i++) {
       //             AlbumFile albumFile = albumFiles.get(i);
       //             if(checkAlbumFile.equals(albumFile)){
       //                 albumFile.setChecked(true);
       //                 mCheckedFile.add(albumFile);
       //            }
       //         }
        //    }
        //}
        return albumFolders;
    }

}

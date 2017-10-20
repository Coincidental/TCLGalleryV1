package com.gallery.tclgallery.utils;

import android.content.Context;
import android.os.AsyncTask;

import com.gallery.tclgallery.bean.LocalMediaBean;

import java.util.ArrayList;

/**
 * Created by liddo on 2017/10/11.
 */

public class LocalMediaScanTask extends AsyncTask<ArrayList<LocalMediaBean>, Void, ArrayList<LocalMediaBean>>{

    public interface Callback {
        /**
         * Album scan result
         * @param localMediaList
         */
        void onScanCallback(ArrayList<LocalMediaBean> localMediaList);
    }

    private Context mContext;
    private Callback callback;

    public LocalMediaScanTask(Context context, Callback callback){
        mContext = context;
        this.callback = callback;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    @Override
    protected ArrayList<LocalMediaBean> doInBackground(ArrayList<LocalMediaBean>... arrayLists) {
        ArrayList<LocalMediaBean> localMediaList = new ArrayList<>();
        localMediaList.addAll(new MediaReader2(mContext).getAllMedia());
        return localMediaList;
    }

    @Override
    protected void onPostExecute(ArrayList<LocalMediaBean> localMediaBean) {
        super.onPostExecute(localMediaBean);
        callback.onScanCallback(localMediaBean);
    }

}

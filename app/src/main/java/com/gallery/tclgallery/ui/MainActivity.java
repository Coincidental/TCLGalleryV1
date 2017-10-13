package com.gallery.tclgallery.ui;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.View;

import com.gallery.tclgallery.R;
import com.gallery.tclgallery.interfaces.LocalMediaDao;
import com.gallery.tclgallery.model.LocalMediaDaoImpl;

public class MainActivity extends Activity {
    private Context mContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mContext = getApplicationContext();
    }

    public void scanLocalMedia(View view) {
        LocalMediaDao localMediaDao = new LocalMediaDaoImpl(mContext);
        localMediaDao.initLocalMedia();
    }
}

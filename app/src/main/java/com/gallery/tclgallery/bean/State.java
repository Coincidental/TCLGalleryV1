package com.gallery.tclgallery.bean;

import android.app.Service;
import android.util.Log;

import com.gallery.tclgallery.contract.GooglePhotoContract;

/**
 * Created by Administrator on 2017/9/26.
 */
public class State  {
    public  static  boolean SelectState =false;
    public  static  int  orientation =0;//屏幕方向 1位竖屏 2为横屏
    public  static  int num = 0;
    public static GooglePhotoContract.Presenter mPresenter;
   /* public static int getNum() {
        if (mPresenter.getViewType()== GooglePhotoActivity.ViewType.DAY){if (orientation==1)num=4;else if (orientation==2)num=6;else num=4;
            Log.d("mm", "getNum: ViewType.DAY = "+num);}
        else if (mPresenter.getViewType()== GooglePhotoActivity.ViewType.MONTH){if (orientation==1)num=6;else if (orientation==2)num=8;else num=6;
            Log.d("mm", "getNum: ViewType.MONTH = "+num);}
        return num;
    }*/
}

package com.gallery.tclgallery.sectionedrecyclerviewadapter;

import java.io.BufferedOutputStream;

/**
 * Created by Administrator on 2017/9/14.
 */
public class DataSynEvent {
    private int section;
//    private  boolean isSelect;
    public int getSection() {
        return section;
    }

    public  DataSynEvent(int section) {
        this.section = section;
    }

    /*public  boolean getIsSelect(){
        return isSelect;
    }

    public  void setIsSelect(boolean isSelect){
        this.isSelect=isSelect;
    }*/
}

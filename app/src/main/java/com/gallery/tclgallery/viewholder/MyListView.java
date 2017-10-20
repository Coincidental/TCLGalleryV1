package com.gallery.tclgallery.viewholder;

import android.content.Context;
import android.database.DataSetObserver;
import android.graphics.Color;
import android.media.Image;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;


import com.gallery.tclgallery.R;
import com.gallery.tclgallery.bean.CameraItem;
import com.gallery.tclgallery.data.GooglePhotoScanner;
import com.gallery.tclgallery.ui.GooglePhotoActivity;
import com.gallery.tclgallery.utils.Format;
import com.gallery.tclgallery.utils.ImageLoader;
import com.gallery.tclgallery.utils.UIUtils;
import com.gallery.tclgallery.viewholder.base.BaseHolder;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2017/10/3 0003.
 */

public class MyListView extends BaseHolder {
    private static String TAG = "666";

    protected List<Integer> one;
    protected List<Integer> two;
    private List<String> list = null;
    private List<String> groupkey = new ArrayList<String>();
    private ListView myListView;
    private LinkedHashMap<String, List<CameraItem>> mAllPhotos;
    private Context myContext;
    protected List<String> mTitles;                             // 日期集合
    protected List<List<CameraItem>> mSectionPhotos;      // 照片集合
    protected List<CameraItem> items;                       // 把上面照片集合转成一维集合，方便取值
    protected List<Integer> mTitlesIndex;

    public MyListView(Context context) {
        super(context);
        myContext = context;
    }

    @Override
    public View initView() {
        View view = View.inflate(context, R.layout.collage_view, null);
        myListView = (ListView) view.findViewById(R.id.list);
//        myListView.setBackgroundColor(Color.BLUE);
        GooglePhotoScanner.startScan();
        mAllPhotos = GooglePhotoScanner.getPhotoSections(GooglePhotoActivity.ViewType.DAY);
        Log.d(TAG, "initView: mAllPhotos" + mAllPhotos.keySet() + "\tmAllPhotos  == " + mAllPhotos.size());
        //添加 数据
        setAllPhotos(mAllPhotos);
        //添加状态为1和2的行数
//        classify();
        /*for (int i = 0; i < 20; i++) {
            getRow(i);
            getGrop(i);
            Log.d("1111", "initView: i = " + i + "\tgetGrop= " + getGrop(i) + "\tgetRow = " + getRow(i));
        }*/
        Log.d(TAG, "initView: mSectionPhotos.get(0).size()" + mSectionPhotos.get(0).size());
        MYAdapter myAdapter = new MYAdapter(context);
        myListView.setAdapter(myAdapter);
        return view;
    }


    /**
     * 根据行数获取组的行数
     */
    public int getGrop(int hang) {
        int sum = 0;
        for (int i = 0; i < mSectionPhotos.size(); i++) {
            if (i == 0) {
                sum += (mSectionPhotos.get(i).size() - 1) / 3 + ((mSectionPhotos.get(i).size() - 1) % 3 != 0 ? 1 : 0);
            } else {
                sum += mSectionPhotos.get(i).size() / 3 + (mSectionPhotos.get(i).size() % 3 != 0 ? 1 : 0);
            }
            if ((sum - hang) >= 0) {
//                Log.d("1111", "getGrop: " + i);
                return i;
            }
        }
        return -1;
    }

    public int getRow(int hang) {
        int i = getGrop(hang);
        int sum = 0;
        int row = 0;
        if (i == 0) {
            row = hang;
        } else if (i > 0) {
            for (int a = 1; a < i; a++) {
                sum += mSectionPhotos.get(a).size() / 3 + (mSectionPhotos.get(a).size() % 3 != 0 ? 1 : 0);
            }
            sum = sum + (mSectionPhotos.get(0).size() - 1) / 3 + ((mSectionPhotos.get(0).size() - 1) % 3 != 0 ? 1 : 0);
            row = hang - sum - 1;
        }
//        Log.d("1111", "getRow: sum = "+sum+"\t row = "+row+"\t hang "+hang);
        return row;
    }

    /**
     * 计算状态为1和2的行数
     */
    private void classify() {
        int typehang = 0;
        int yu = 0;
        List<CameraItem> list;
        one = new ArrayList<>();
        two = new ArrayList<>();
        //判断是今天
        if (true) {
            Log.d(TAG, "classify: " + one + "\t\t" + two);
            one.add(0);
            list = mSectionPhotos.get(0);
            yu = (list.size() - 1) % 3;
            typehang += 1 + list.size() / 3 + ((list.size() - 1) % 3 != 0 ? 1 : 0);
            switch (yu) {
                case 0:
                    break;
                case 1:
                    one.add(typehang);
                    break;
                case 2:
                    two.add(typehang);
                    break;
                default:
                    break;
            }
        } else {
            list = mSectionPhotos.get(0);
            yu = list.size() % 3;
            typehang += list.size() / 3 + (list.size() % 3 != 0 ? 1 : 0);
            switch (yu) {
                case 0:
                    break;
                case 1:
                    one.add(typehang);
                    break;
                case 2:
                    two.add(typehang);
                    break;
                default:
                    break;
            }
        }
        for (int section = 1; section < mSectionPhotos.size(); section++) {
            list = mSectionPhotos.get(section);
            yu = list.size() % 3;
            typehang += list.size() / 3 + (list.size() % 3 != 0 ? 1 : 0);
            switch (yu) {
                case 0:
                    break;
                case 1:
                    one.add(typehang);
                    break;
                case 2:
                    two.add(typehang);
                    break;
                default:
                    break;
            }

        }
    }

    @Override
    public void refreshView() {
        if (Format.isEmpty(mAllPhotos)) return;
        setAllPhotos(mAllPhotos);
    }

    private class MYAdapter extends BaseAdapter {
        int index = 0;
        private LayoutInflater mInflater = null;

        public MYAdapter(Context context) {
            this.mInflater = LayoutInflater.from(context);
        }

        @Override
        public void registerDataSetObserver(DataSetObserver dataSetObserver) {

        }

        @Override
        public void unregisterDataSetObserver(DataSetObserver dataSetObserver) {

        }

        @Override
        public int getCount() {
//            return mAllPhotos.keySet().size();
//            List<CameraItem> list = mAllPhotos.get(mAllPhotos.keySet().toArray()[0]);
//            mSectionPhotos
            //是否特殊行
            //判断是不是今天
            //添加余数为1的状态行
            //添加余数为2的状态
            int zonghang = 0;
            int hang = 0;
            List<CameraItem> list;
            mTitlesIndex = new ArrayList<>();
            //判断是否是今天
            if (true) {
                Log.d("2222", "getCount: "+zonghang);mTitlesIndex.add(new Integer(zonghang));
                list = mSectionPhotos.get(0);
                hang = 1 + (list.size() - 1) / 3 + ((list.size() - 1) % 3 != 0 ? 1 : 0);
                zonghang = hang;
            } else {
                Log.d("2222", "getCount: "+zonghang);   mTitlesIndex.add(new Integer(zonghang));
                list = mSectionPhotos.get(0);
                hang = list.size() / 3 + (list.size() % 3 != 0 ? 1 : 0);
                zonghang = hang;
            }


            for (int section = 1; section < mSectionPhotos.size(); section++) {
                Log.d("2222", "getCount: "+zonghang);
                mTitlesIndex.add(new Integer(zonghang));
                list = mSectionPhotos.get(section);
                hang = list.size() / 3 + (list.size() % 3 != 0 ? 1 : 0);
                zonghang += hang;
            }
//            Log.d(TAG, "getCount: "+items.size());
            Log.d("777", "getCount: " + zonghang);
            return zonghang ;
        }


        @Override
        public Object getItem(int i) {
            return i;
        }

        @Override
        public long getItemId(int i) {
            return i;
        }

        @Override
        public boolean hasStableIds() {
            return true;
        }

        @Override
        public View getView(int i, View convertView, ViewGroup viewGroup) {
            Log.d("777", "getView: i" + i);
            ViewHolder holder = null;
            if (convertView == null) {

                holder = new ViewHolder();

                convertView = viewGroup.inflate(context, R.layout.collage_item, null);

                convertView.setTag(holder);


            } else {

                holder = (ViewHolder) convertView.getTag();
            }

            holder.textView = (TextView) convertView.findViewById(R.id.collage_text);
            holder.imageView1 = (ImageView) convertView.findViewById(R.id.collage_image1);
            holder.imageView2 = (ImageView) convertView.findViewById(R.id.collage_image2);
            holder.imageView3 = (ImageView) convertView.findViewById(R.id.collage_image3);

            holder.imageView1.setVisibility(View.INVISIBLE);
            holder.imageView2.setVisibility(View.INVISIBLE);
            holder.imageView3.setVisibility(View.INVISIBLE);

//            if (mTitlesIndex.contains(new Integer(i))) {
                for (Integer a : mTitlesIndex) {
                    Log.d("qqq", "getView: 1");
                    if (a == i) {
                        Log.d("qqq", "getView: 2"+i);
                        holder.textView.setText("" + mTitles.get(getGrop(i)));
                        holder.textView.setVisibility(View.VISIBLE);
                        break;
                    } else {
                        holder.textView.setVisibility(View.GONE);
                    }
                }
//            }
                /*if (mTitles.size() > 0) {
                    Log.d(TAG, "getView: mTitles.size()" + mTitles.size());
//                    if (i <= mTitles.size()) {
                        holder.textView.setText("" + mTitles.get(getGrop(i)));
                    Log.d("2222", "getView: holder.textView  = "+mTitles.get(getGrop(i)));
//                    }
//                    CameraItem item=items.get(i);

//                    holder.imageView.
//                    mTitles.remove(i);
                }

            } else {
                holder.textView.setVisibility(View.GONE);
//                CameraItem item=items.get(i);

//                holder.textView.setText(item.getPath());
            }*/
//第一轮
//            setPhoto
            //判断当前循环的状态

            int type = 0;    /*(mSectionPhotos.get(0).size()-1)%3;*///1为图片1屏幕宽度 2为 图片1三分之二宽度 0位全部三分之一宽度
//            Log.d(TAG, "getView: one = "+one+"\t\ttwo = "+two);
         /*  for(Integer a:two){
                if(a==i){
                    type=2;
                }else break;
            }
            for(Integer a:one){
                if(a==i){
                    type=1;
                }else break;
            }*/
            ViewGroup.LayoutParams lp = null;
            type = /*mSectionPhotos.get(getGrop(i)).size() % 3;*/0;
            Log.d(TAG, "getView: type" + type);
            List<CameraItem> list = mSectionPhotos.get(0);
            int index = 3 * i - 2;
            //today is true
            if (getGrop(i) == 0) {
                if (true) {
                    type = (mSectionPhotos.get(0).size() - 1) % 3;
                    if (i == 0) {
                        Log.d("aaa", "getView: i = "+i+"\ttype = "+type);
                        if (index <= list.size() - 1) {
                            Log.i("666", "getView: do 1 in 1");
                            holder.setData1(list.get(0));
//                            ViewGroup.LayoutParams lp;
                            lp = holder.imageView1.getLayoutParams();
                            lp.width = UIUtils.getScreenWidth();
                            lp.height = UIUtils.getScreenWidth() / 3;
                            holder.imageView1.setLayoutParams(lp);
                            holder.imageView1.setVisibility(View.VISIBLE);
                        }

                    } else if ((type == 1) && (i == ((mSectionPhotos.get(0).size() - 1) / 3 + (type != 0 ? 1 : 0)))) {
                        Log.d("aaa", "getView: i = "+i+"\ttype = "+type);
                        if (index <= list.size() - 1) {
                            Log.i("666", "getView: do 1 in 1");
                            holder.setData1(list.get(index));
//                            ViewGroup.LayoutParams lp;
                            lp = holder.imageView1.getLayoutParams();
                            lp.width = UIUtils.getScreenWidth();
                            lp.height = UIUtils.getScreenWidth() / 3;
                            holder.imageView1.setLayoutParams(lp);
                            holder.imageView1.setVisibility(View.VISIBLE);
                            index++;
                        }
                    } else if ((type == 2) && (i == ((mSectionPhotos.get(0).size() - 1) / 3 + ((mSectionPhotos.get(0).size() - 1) % 3 != 0 ? 1 : 0)) - 1)) {
                        Log.d("aaa", "getView: i = "+i+"\ttype = "+type);
                        if (index <= list.size() - 1) {
                            Log.d(TAG, "getView: 1");
                            holder.setData1(list.get(index));
//                            ViewGroup.LayoutParams lp;
                            lp = holder.imageView1.getLayoutParams();
                            lp.width = UIUtils.getScreenWidth() / 3 * 2;
                            lp.height = UIUtils.getScreenWidth() / 3;
                            holder.imageView1.setLayoutParams(lp);
                            holder.imageView1.setVisibility(View.VISIBLE);
                            index++;
                            Log.d(TAG, "getView: 2");
                            holder.setData2(list.get(index));
                            lp = holder.imageView2.getLayoutParams();
                            lp.width = UIUtils.getScreenWidth() / 3;
                            lp.height = UIUtils.getScreenWidth() / 3;
                            holder.imageView2.setLayoutParams(lp);
                            holder.imageView2.setVisibility(View.VISIBLE);
                            index++;
                        }
                    } else {

                        if (index <= list.size() - 1) {

                            Log.d("aaa", "getView: i = "+i+"\ttype = "+type);
                            Log.i("666", "getView: do 1 in 3");
                            holder.setData1(list.get(index));
//                    holder.imageView1.setMinimumWidth(UIUtils.getScreenWidth()/3);

                            Log.i("imagepath", "setData: " + index + "--" + items.get(index).getPath());
//                            ViewGroup.LayoutParams lp;
                            lp = holder.imageView1.getLayoutParams();
                            lp.width = UIUtils.getScreenWidth() / 3;
                            lp.height = UIUtils.getScreenWidth() / 3;
                            holder.imageView1.setLayoutParams(lp);
                            holder.imageView1.setVisibility(View.VISIBLE);
                            index++;
                        }
                        Log.d("aaa", "getView: i = "+i+"\ttype = "+type);
                        if (index <= list.size() - 1) {
                            Log.i("666", "getView: do 2 in 3");
                            holder.setData2(list.get(index));
                            Log.i("imagepath", "setData: " + index + "--" + items.get(index).getPath());
//                            ViewGroup.LayoutParams lp;
                            lp = holder.imageView2.getLayoutParams();
                            lp.width = UIUtils.getScreenWidth() / 3;
                            lp.height = UIUtils.getScreenWidth() / 3;
                            holder.imageView2.setLayoutParams(lp);

                            holder.imageView2.setVisibility(View.VISIBLE);
                            index++;
                        }

                        Log.i("668", "getView: do 3 in 3");
                        if (index <= list.size() - 1) {
                            Log.d("aaa", "getView: i = "+i+"\ttype = "+type);
                            holder.setData3(list.get(index));
                            Log.i("imagepath", "setData: " + index + "--" + items.get(index).getPath());
//                            ViewGroup.LayoutParams lp;
                            lp = holder.imageView3.getLayoutParams();
                            lp.width = UIUtils.getScreenWidth() / 3;
                            lp.height = UIUtils.getScreenWidth() / 3;
                            holder.imageView3.setLayoutParams(lp);

                            holder.imageView3.setVisibility(View.VISIBLE);
                            index++;
                        }

                    }
                } else {
                    index = 3 * i;
                    type = mSectionPhotos.get(0).size() % 3;
                    if ((type == 1) && (i == (mSectionPhotos.get(0).size() / 3 + (type != 0 ? 1 : 0)))) {
                        if (index <= list.size() - 1) {
                            Log.i("666", "getView: do 1 in 1");
                            holder.setData1(list.get(0));
//                            ViewGroup.LayoutParams lp;
                            lp = holder.imageView1.getLayoutParams();
                            lp.width = UIUtils.getScreenWidth();
                            lp.height = UIUtils.getScreenWidth() / 3;
                            holder.imageView1.setLayoutParams(lp);
                            holder.imageView1.setVisibility(View.VISIBLE);
                            index++;
                        }
                    } else if ((type == 2) && (i == (mSectionPhotos.get(0).size() / 3 + (mSectionPhotos.get(0).size() % 3 != 0 ? 1 : 0)) - 1)) {
                        if (index <= list.size() - 1) {
                            Log.d(TAG, "getView: 1");
                            holder.setData1(list.get(index));
//                            ViewGroup.LayoutParams lp;
                            lp = holder.imageView1.getLayoutParams();
                            lp.width = UIUtils.getScreenWidth() / 3 * 2;
                            lp.height = UIUtils.getScreenWidth() / 3;
                            holder.imageView1.setLayoutParams(lp);
                            holder.imageView1.setVisibility(View.VISIBLE);
                            index++;
                            Log.d(TAG, "getView: 2");
                            holder.setData2(list.get(index));
                            lp = holder.imageView2.getLayoutParams();
                            lp.width = UIUtils.getScreenWidth() / 3;
                            lp.height = UIUtils.getScreenWidth() / 3;
                            holder.imageView2.setLayoutParams(lp);
                            holder.imageView2.setVisibility(View.VISIBLE);
                            index++;
                        }
                    } else {

                        if (index <= list.size() - 1) {


                            Log.i("666", "getView: do 1 in 3");
                            holder.setData1(list.get(index));
//                    holder.imageView1.setMinimumWidth(UIUtils.getScreenWidth()/3);

                            Log.i("imagepath", "setData: " + index + "--" + items.get(index).getPath());
//                            ViewGroup.LayoutParams lp;
                            lp = holder.imageView1.getLayoutParams();
                            lp.width = UIUtils.getScreenWidth() / 3;
                            lp.height = UIUtils.getScreenWidth() / 3;
                            holder.imageView1.setLayoutParams(lp);
                            holder.imageView1.setVisibility(View.VISIBLE);
                            index++;
                        }

                        if (index <= list.size() - 1) {
                            Log.i("666", "getView: do 2 in 3");
                            holder.setData2(list.get(index));
                            Log.i("imagepath", "setData: " + index + "--" + items.get(index).getPath());
//                            ViewGroup.LayoutParams lp;
                            lp = holder.imageView2.getLayoutParams();
                            lp.width = UIUtils.getScreenWidth() / 3;
                            lp.height = UIUtils.getScreenWidth() / 3;
                            holder.imageView2.setLayoutParams(lp);

                            holder.imageView2.setVisibility(View.VISIBLE);
                            index++;
                        }

                        Log.i("668", "getView: do 3 in 3");
                        if (index <= list.size() - 1) {
                            holder.setData3(list.get(index));
                            Log.i("imagepath", "setData: " + index + "--" + items.get(index).getPath());
//                            ViewGroup.LayoutParams lp;
                            lp = holder.imageView3.getLayoutParams();
                            lp.width = UIUtils.getScreenWidth() / 3;
                            lp.height = UIUtils.getScreenWidth() / 3;
                            holder.imageView3.setLayoutParams(lp);

                            holder.imageView3.setVisibility(View.VISIBLE);
                            index++;
                        }

                    }
                }
            } else {
                list = mSectionPhotos.get(getGrop(i));
                   /* int grop = 0;
                    grop = getGrop(i);*/
                   /* int sum = 0;
                    for (int i1 = 0; i1 < grop; i1++) {
                        sum += mSectionPhotos.get(i1).size();
                    }*/
                index = 3 * getRow(i);
                type = mSectionPhotos.get(getGrop(i)).size() % 3;
                Log.d("1111", "getView: getGrop" + getGrop(i) + "\tindex" + index + "\ttype" + type + "\tlist = " + list);
                if ((type == 1) && (getRow(i) == (mSectionPhotos.get(getGrop(i)).size() / 3 + (type != 0 ? 1 : 0)-1))) {
                    if (index <= list.size() - 1) {

                        Log.i("1111", "getView: do 1 in 1");
                        holder.setData1(list.get(index));
//                        ViewGroup.LayoutParams lp;
                        lp = holder.imageView1.getLayoutParams();
                        lp.width = UIUtils.getScreenWidth();
                        lp.height = UIUtils.getScreenWidth() / 3;
                        holder.imageView1.setLayoutParams(lp);
                        holder.imageView1.setVisibility(View.VISIBLE);
                        Log.d("aaa", "getView: i = "+i+"\ttype = "+type+"\t  lp.h = "+lp.height+"\t lp.w = "+lp.width+"\tindex = "+index);
                        index++;
                    }
                } else if ((type == 2) && (getRow(i) == (mSectionPhotos.get(getGrop(i)).size() / 3 + (mSectionPhotos.get(getGrop(i)).size() % 3 != 0 ? 1 : 0)) - 1)) {
                    if (index <= list.size() - 1) {
                        Log.d("1111", "getView: 1");
                        Log.d("aaa", "getView: i = "+i+"\ttype = "+type);
                        holder.setData1(list.get(index));
//                        ViewGroup.LayoutParams lp;
                        lp = holder.imageView1.getLayoutParams();
                        lp.width = UIUtils.getScreenWidth() / 3 * 2;
                        lp.height = UIUtils.getScreenWidth() / 3;
                        holder.imageView1.setLayoutParams(lp);
                        holder.imageView1.setVisibility(View.VISIBLE);
                        Log.d("aaa", "getView: i = "+i+"\ttype = "+type+"\t  lp.h = "+lp.height+"\t lp.w = "+lp.width+"\tindex = "+index);
                        index++;
                        Log.d("1111", "getView: 2");
                        holder.setData2(list.get(index));
                        lp = holder.imageView2.getLayoutParams();
                        lp.width = UIUtils.getScreenWidth() / 3;
                        lp.height = UIUtils.getScreenWidth() / 3;
                        holder.imageView2.setLayoutParams(lp);
                        holder.imageView2.setVisibility(View.VISIBLE);
                        Log.d("aaa", "getView: i = "+i+"\ttype = "+type+"\t  lp.h = "+lp.height+"\t lp.w = "+lp.width+"\tindex = "+index);
                        index++;
                    }
                } else {
                    if (index <= list.size() - 1) {

                        Log.i("1111", "getView: do 1 in 3");
                        holder.setData1(list.get(index));
//                    holder.imageView1.setMinimumWidth(UIUtils.getScreenWidth()/3);

                        Log.i("imagepath", "setData: " + index + "--" + items.get(index).getPath());
//                        ViewGroup.LayoutParams lp;
                        lp = holder.imageView1.getLayoutParams();
                        lp.width = UIUtils.getScreenWidth() / 3;
                        lp.height = UIUtils.getScreenWidth() / 3;
                        holder.imageView1.setLayoutParams(lp);
                        holder.imageView1.setVisibility(View.VISIBLE);
                        Log.d("aaa", "getView: i = "+i+"\ttype = "+type+"\t  lp.h = "+lp.height+"\t lp.w = "+lp.width+"\tindex = "+index);
                        index++;
                    }

                    if (index <= list.size() - 1) {
                        Log.i("1111", "getView: do 2 in 3");
                        holder.setData2(list.get(index));
                        Log.i("imagepath", "setData: " + index + "--" + items.get(index).getPath());
//                        ViewGroup.LayoutParams lp;
                        lp = holder.imageView2.getLayoutParams();
                        lp.width = UIUtils.getScreenWidth() / 3;
                        lp.height = UIUtils.getScreenWidth() / 3;
                        holder.imageView2.setLayoutParams(lp);

                        holder.imageView2.setVisibility(View.VISIBLE);
                        Log.d("aaa", "getView: i = "+i+"\ttype = "+type+"\t  lp.h = "+lp.height+"\t lp.w = "+lp.width+"\tindex = "+index);

                        index++;
                    }

                    Log.i("1111", "getView: do 3 in 3");
                    if (index <= list.size() - 1) {
                        holder.setData3(list.get(index));
                        Log.i("imagepath", "setData: " + index + "--" + items.get(index).getPath());
//                        ViewGroup.LayoutParams lp;
                        lp = holder.imageView3.getLayoutParams();
                        lp.width = UIUtils.getScreenWidth() / 3;
                        lp.height = UIUtils.getScreenWidth() / 3;
                        holder.imageView3.setLayoutParams(lp);

                        holder.imageView3.setVisibility(View.VISIBLE);
                        Log.d("aaa", "getView: i = "+i+"\ttype = "+type+"\t  lp.h = "+lp.height+"\t lp.w = "+lp.width+"\tindex = "+index);

                        index++;
                    }

                }

            }
            return convertView;
        }


        @Override
        public int getItemViewType(int i) {
            return 0;
        }

        @Override
        public int getViewTypeCount() {
            return 1;
        }

        @Override
        public boolean isEmpty() {
            return false;
        }
    }

    public static Object[][] ListToArray(List<LinkedHashMap<String, Object>> list, int KeyLenght)

    {

        Object[][] array = new Object[list.size()][KeyLenght];

        for (int i = 0; i < list.size(); i++)

        {

            array[i] = list.get(i).values().toArray();

        }

        return array;

    }

    public void setAllPhotos(LinkedHashMap<String, List<CameraItem>> allPhotos) {
        mAllPhotos = allPhotos;
        mTitles = new ArrayList<>(mAllPhotos.size());
        mSectionPhotos = new ArrayList<>(mAllPhotos.size());
        items = new ArrayList<>();
//        mTitlesIndex = new ArrayList<>();
        for (Map.Entry<String, List<CameraItem>> entry : mAllPhotos.entrySet()) {
            mTitles.add(entry.getKey());
            mSectionPhotos.add(entry.getValue());
        }
//        int count = 0;
        for (List<CameraItem> photoSection : mSectionPhotos) {
//            mTitlesIndex.add(new Integer(count));
//            count += photoSection.size();
//            Log.d("2222", "setAllPhotos: count  = "+count);
            for (CameraItem cameraItem : photoSection) {
                items.add(cameraItem);
            }
        }

    }

    class ViewHolder {
        TextView textView;
        ImageView imageView1, imageView2, imageView3;

        public void setData1(CameraItem cameraItem) {
            String path = TextUtils.isEmpty(cameraItem.getThumbnail()) ? cameraItem.getPath() : cameraItem.getThumbnail();
            ImageLoader.loadGalleryImage(myContext, path, imageView1);
        }

        public void setData2(CameraItem cameraItem) {
            String path = TextUtils.isEmpty(cameraItem.getThumbnail()) ? cameraItem.getPath() : cameraItem.getThumbnail();
            ImageLoader.loadGalleryImage(myContext, path, imageView2);
        }

        public void setData3(CameraItem cameraItem) {
            String path = TextUtils.isEmpty(cameraItem.getThumbnail()) ? cameraItem.getPath() : cameraItem.getThumbnail();

            ImageLoader.loadGalleryImage(myContext, path, imageView3);
        }
    }
}

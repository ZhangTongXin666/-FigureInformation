package com.example.kys_31.figureinformation;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ResolveInfo;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnLongClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.GridView;
import android.widget.SlidingDrawer;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import data.CollectEssayMessage;
import data.HandleCollectEssayMessage;
import util.ViewUtil;

import static com.example.kys_31.figureinformation.R.layout.dialog_collection_view;
import static com.example.kys_31.figureinformation.R.layout.dialog_special_view;

/**
 * Created by kys_7 on 2017/11/17.
 */

public class CollectionActivity extends AppCompatActivity{
    private GridView bookShelf;

    private Button back;
    private List<CollectEssayMessage> mListCollectionMessage=new ArrayList<>();


    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.layout_collection);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null){
            actionBar.hide();
        }
        /*设置状态栏颜色*/
        Window window = getWindow();
        window.setStatusBarColor(Color.parseColor("#d5a175"));

        mListCollectionMessage = HandleCollectEssayMessage.readAllEssayMessage();
        back=(Button)findViewById(R.id.btn_leftTop);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        bookShelf = (GridView) findViewById(R.id.bookShelf);
        ShlefAdapter adapter=new ShlefAdapter(this);
        bookShelf.setAdapter(adapter);

    }

    class ShlefAdapter extends BaseAdapter {
        private Context mContext;
        public ShlefAdapter(Context context) {
            mContext=context;
        }
        @Override
        public int getCount() {
            return mListCollectionMessage.size();
        }

        @Override
        public Object getItem(int arg0) {
            return arg0;
        }

        @Override
        public long getItemId(int arg0) {
            return arg0;
        }

        @Override
        public View getView(final int position, View contentView, ViewGroup arg2) {

            contentView= LayoutInflater.from(getApplicationContext()).inflate(R.layout.item, null);

            TextView view=(TextView) contentView.findViewById(R.id.imageView1);
            view.setText(mListCollectionMessage.get(position).title);
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(CollectionActivity.this, DetailMessageActivity.class);
                    intent.putExtra("URL", mListCollectionMessage.get(position).url);
                    intent.putExtra("title", mListCollectionMessage.get(position).title);
                    intent.putExtra("timeandauthor", mListCollectionMessage.get(position).time);
                    startActivity(intent);
                }
            });
            view.setOnLongClickListener(new OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    final Dialog dialogWarn = new AlertDialog.Builder(mContext).create();
                    final View viewDialog = LayoutInflater.from(mContext).inflate(R.layout.dialog_collection_view,null);
                    dialogWarn.show();
                    dialogWarn.setCanceledOnTouchOutside(true);
                    dialogWarn.setContentView(viewDialog);
                    ViewUtil.setDialogWindowAttr(dialogWarn, 800, 550);
                    Button btCancleCollection = (Button)viewDialog.findViewById(R.id.bt_cancleCollection);
                    Button btNoCancleCollection = (Button)viewDialog.findViewById(R.id.bt_noCancle);

                    btCancleCollection.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            HandleCollectEssayMessage.deleteSingleEssay(mListCollectionMessage.get(position).title);
                            mListCollectionMessage.remove(position);
                            notifyDataSetChanged();
                            dialogWarn.dismiss();
                        }
                    });
                    btNoCancleCollection.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            dialogWarn.dismiss();
                        }
                    });
                    return true;
                }
            });
            return contentView;
        }

    }


}

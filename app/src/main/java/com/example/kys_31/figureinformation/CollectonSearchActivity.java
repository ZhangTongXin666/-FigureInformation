package com.example.kys_31.figureinformation;

import android.content.Intent;
import android.os.Message;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Observable;

import util.SVProgressHUDUtil;

/**
 * Created by kys_7 on 2017/11/18.
 */

public class CollectonSearchActivity extends BaseActivity{
    private TextView titleName_tv;
    private LinearLayout titleBack_persionMessage_ll;
    private EditText et_search;
    private TextView tv_search;
    private Button bt_StandardLiterature;
    private Button bt_BookMatrix;
    private Button bt_CorpusCompileMatrix;
    private Button bt_ReportMatrix;
    private Button bt_Patent;
    private Button bt_DegreePaper;
    private Button bt_ProceedingsSet;
    private Button bt_JournalPaper;
    private Button bt_Document;
    private String type="Document";

    @Override
    protected int getLayoutID() {
        return R.layout.activity_collection_search;
    }

    @Override
    protected void initControl() {
        titleName_tv=(TextView)findViewById(R.id.titleName_tv);
        titleName_tv.setText("馆藏搜索");
        titleBack_persionMessage_ll=(LinearLayout)findViewById(R.id.titleBack_persionMessage_ll);
        et_search=(EditText)findViewById(R.id.et_search);
        tv_search=(TextView)findViewById(R.id.tv_search);
        bt_Document=(Button)findViewById(R.id.bt_Document);
        bt_JournalPaper=(Button)findViewById(R.id.bt_JournalPaper);
        bt_ProceedingsSet=(Button)findViewById(R.id.bt_ProceedingsSet);
        bt_DegreePaper=(Button)findViewById(R.id.bt_DegreePaper);
        bt_Patent=(Button)findViewById(R.id.bt_Patent);
        bt_ReportMatrix=(Button)findViewById(R.id.bt_ReportMatrix);
        bt_CorpusCompileMatrix=(Button)findViewById(R.id.bt_CorpusCompileMatrix);
        bt_BookMatrix=(Button)findViewById(R.id.bt_BookMatrix);
        bt_StandardLiterature=(Button)findViewById(R.id.bt_StandardLiterature);
    }

    @Override
    public void setControlListener() {
        titleBack_persionMessage_ll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        tv_search.setOnClickListener(this);
        bt_Document.setOnClickListener(this);
        bt_JournalPaper.setOnClickListener(this);
        bt_ProceedingsSet.setOnClickListener(this);
        bt_DegreePaper.setOnClickListener(this);
        bt_Patent.setOnClickListener(this);
        bt_ReportMatrix.setOnClickListener(this);
        bt_CorpusCompileMatrix.setOnClickListener(this);
        bt_BookMatrix.setOnClickListener(this);
        bt_StandardLiterature.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.tv_search:
                if (et_search.getText().length()>0){
                    String message=et_search.getText().toString();
                    Intent intent=new Intent(CollectonSearchActivity.this,WebViewActivity.class);
                    intent.putExtra("message",message);
                    intent.putExtra("type",type);
                    startActivity(intent);
                }else {
                    Toast.makeText(this,"您输入的信息为空！",Toast.LENGTH_SHORT).show();
                }

                break;
            case R.id.bt_Document:
                resetButton();
                bt_Document.setEnabled(false);
                bt_Document.setBackgroundResource(R.drawable.shelves_editbook_d);
                type="Document";
                break;
            case R.id.bt_JournalPaper:
                resetButton();
                bt_JournalPaper.setEnabled(false);
                bt_JournalPaper.setBackgroundResource(R.drawable.shelves_editbook_d);
                type="JournalPaper";
                break;
            case R.id.bt_ProceedingsSet:
                resetButton();
                bt_ProceedingsSet.setEnabled(false);
                bt_ProceedingsSet.setBackgroundResource(R.drawable.shelves_editbook_d);
                type="ProceedingsSet";
                break;
            case R.id.bt_DegreePaper:
                resetButton();
                bt_DegreePaper.setEnabled(false);
                bt_DegreePaper.setBackgroundResource(R.drawable.shelves_editbook_d);
                type="DegreePaper";
                break;
            case R.id.bt_Patent:
                resetButton();
                bt_Patent.setEnabled(false);
                bt_Patent.setBackgroundResource(R.drawable.shelves_editbook_d);
                type="Patent";
                break;
            case R.id.bt_ReportMatrix:
                resetButton();
                bt_ReportMatrix.setEnabled(false);
                bt_ReportMatrix.setBackgroundResource(R.drawable.shelves_editbook_d);
                type="ReportMatrix";
                break;
            case R.id.bt_CorpusCompileMatrix:
                resetButton();
                bt_CorpusCompileMatrix.setEnabled(false);
                bt_CorpusCompileMatrix.setBackgroundResource(R.drawable.shelves_editbook_d);
                type="CorpusCompileMatrix";
                break;
            case R.id.bt_BookMatrix:
                resetButton();
                bt_BookMatrix.setEnabled(false);
                bt_BookMatrix.setBackgroundResource(R.drawable.shelves_editbook_d);
                type="BookMatrix";
                break;
            case R.id.bt_StandardLiterature:
                resetButton();
                bt_StandardLiterature.setEnabled(false);
                bt_StandardLiterature.setBackgroundResource(R.drawable.shelves_editbook_d);
                type="StandardLiterature";
                break;
        }
    }

    private void resetButton() {
        bt_Document.setBackgroundResource(R.drawable.shelves_editbook);
        bt_JournalPaper.setBackgroundResource(R.drawable.shelves_editbook);
        bt_ProceedingsSet.setBackgroundResource(R.drawable.shelves_editbook);
        bt_DegreePaper.setBackgroundResource(R.drawable.shelves_editbook);
        bt_Patent.setBackgroundResource(R.drawable.shelves_editbook);
        bt_ReportMatrix.setBackgroundResource(R.drawable.shelves_editbook);
        bt_CorpusCompileMatrix.setBackgroundResource(R.drawable.shelves_editbook);
        bt_BookMatrix.setBackgroundResource(R.drawable.shelves_editbook);
        bt_StandardLiterature.setBackgroundResource(R.drawable.shelves_editbook);
        bt_Document.setEnabled(true);
        bt_JournalPaper.setEnabled(true);
        bt_ProceedingsSet.setEnabled(true);
        bt_DegreePaper.setEnabled(true);
        bt_Patent.setEnabled(true);
        bt_ReportMatrix.setEnabled(true);
        bt_CorpusCompileMatrix.setEnabled(true);
        bt_BookMatrix.setEnabled(true);
        bt_StandardLiterature.setEnabled(true);
    }

    @Override
    public void update(Observable o, Object arg) {

    }
}

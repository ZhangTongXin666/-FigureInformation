package service;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.util.Log;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.TimeUnit;

import database.DBUtil;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import interfaces.LoadDataSuccess;
import util.NetWorkUtil;
import util.StringUtil;
import variable.StartIntentServiceVariable;
import variable.UpdataNetDataVariable;

/**
 * @author : 老头儿
 * @email : 527672827@qq.com
 * @org : 河北北方学院 移动开发工程部 C508
 * @function : （功能）
 */
public class LoadNetDataService extends IntentService {

    private String url1 = "http://portal.nstl.gov.cn/STMonitor/bianyiRss.htm?serverId=";//编译报道
    private String url2 = "http://portal.nstl.gov.cn/STMonitor/qingbaoRss.htm?serverId=";//情报资源
    private String url3 = "http://portal.nstl.gov.cn/STMonitor/bgRss.htm?serverId=";//重要报告
    private List<HashMap<String, String>> mListCardContent = new ArrayList<>();
    private HashMap<String, String> mMap;
    private HashMap<String, String> mKindMap;
    private List<HashMap<String, String>> mListClassName = new ArrayList<>();
    private Document mDocument;
    private static LoadDataSuccess mLoadDataSuccess;
    private OkHttpClient client;
    public static final String LOADPOSITION = "loadposition";
    private static final String TAG = "loadNetDataService";

    /**
     * Creates an IntentService.  Invoked by your subclass's constructor.
     *
     */
    public LoadNetDataService() {
        super("服务线程");
    }

    public static void initLoadSuccess(LoadDataSuccess loadDataSuccess){
        mLoadDataSuccess = loadDataSuccess;
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        client = new OkHttpClient.Builder()
                .connectTimeout(10, TimeUnit.SECONDS)
                .readTimeout(10,TimeUnit.SECONDS)
                .writeTimeout(10, TimeUnit.SECONDS)
                .build();
        LoadOne(UpdataNetDataVariable.osLoadPosition);
    }

    private void LoadOne(int position) {
        for (int i = position; i < 161; i++) {
            if (NetWorkUtil.initNetWorkUtil(this).isNetworkConnected()){
                UpdataNetDataVariable.osLoadPosition = i;
                final Request request = new Request.Builder().url(url1+i).build();
                try {
                    Response response = client.newCall(request).execute();
                    parseHtml(response.body().string(), "编译报道", (url1+i));
                } catch (IOException e) {
                    e.printStackTrace();
                }
                if (i % 20 == 0){
                    LoadTwo(i);
                }
            }else {
                StartIntentServiceVariable.osStartIntentService = false;
                onDestroy();
            }
        }
    }

    /*加载情报资源*/
    private void LoadTwo(int position) {
        for (int i = position - 19; i < 161; i++) {
            if (NetWorkUtil.initNetWorkUtil(this).isNetworkConnected()) {
                final Request request = new Request.Builder().url(url2 + i).build();
                Response response = null;
                try {
                    response = client.newCall(request).execute();
                    parseHtml(response.body().string(), "情报资源", (url2 + i));
                } catch (IOException e) {
                    e.printStackTrace();
                }
                if (i % 20 == 0) {
                   LoadThree(position);
                }
            }else {
                StartIntentServiceVariable.osStartIntentService = false;
                onDestroy();
            }
        }
    }

    /*加载重要报告*/
    private void LoadThree(int position) {
        for (int i = position - 19; i < 161; i++) {
            if (NetWorkUtil.initNetWorkUtil(this).isNetworkConnected()) {
                final Request request = new Request.Builder().url(url3 + i).build();
                Response response = null;
                try {
                    response = client.newCall(request).execute();
                    parseHtml(response.body().string(), "重要报告", (url3 + i));
                } catch (IOException e) {
                    e.printStackTrace();
                }
                if (i % 20 == 0) {
                    saveData();
                    if (mLoadDataSuccess!=null){
                        mLoadDataSuccess.loadSuccess();
                    }
                    LoadOne(position + 1);
                }
            }else {
                StartIntentServiceVariable.osStartIntentService = false;
                onDestroy();
            }
        }
    }

    /*保存数据*/
    private void saveData(){
        DBUtil.getInstance(getApplicationContext()).addClassName(mListClassName);
        DBUtil.getInstance(getApplicationContext()).addCardContent(mListCardContent);
        mListCardContent.clear();
        mListClassName.clear();
    }

    /*解析数据*/
    private void parseHtml(String html, String kind, String url){
        mDocument = Jsoup.parse(html);
        Elements elements = mDocument.select("item");
        int contentSize = elements.size();
        if (contentSize > 0){
            mKindMap = new HashMap<>();
            mKindMap.put("kind",kind);
            String classname =  mDocument.title();
            mKindMap.put("classname", classname.substring(0, classname.length()-3));
            mKindMap.put("url", url);
            mListClassName.add(mKindMap);
            for (int i = 0; i < contentSize; i++) {
                mMap = new HashMap<>();
                mMap.put("title", elements.get(i).select("title").first().text());
                Log.d("TAG","标题："+elements.get(i).select("title").first().text());
                if (StringUtil.subcribtionHtml(elements.get(i).html()) == null){
                    return;
                }
                mMap.put("kind", kind);
                mMap.put("link", StringUtil.subcribtionHtml(elements.get(i).html()));
                mMap.put("description", elements.get(i).select("description").first().text());
                mMap.put("classname", classname.substring(0, classname.length()-3));
                mMap.put("timeandauthor", elements.get(i).select("pubDate").first().text());
                mMap.put("pubdate", StringUtil.subcribtionPubDate(mDocument.select("pubDate").first().text()));
                mListCardContent.add(mMap);
            }
        }
    }

    public static Intent startIntent(Context context){
        Intent intent = new Intent(context, LoadNetDataService.class);
        return intent;
    }
}

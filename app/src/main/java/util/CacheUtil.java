package util;

import android.util.LruCache;

import java.util.HashMap;
import java.util.List;

/**
 * @author : 老头儿
 * @email : 527672827@qq.com
 * @org : 河北北方学院 移动开发工程部 C508
 * @function : （功能）
 */
public class CacheUtil {

    private static CacheUtil msCacheUtil;
    private static LruCache<String, List<HashMap<String, String>>> msLreCache;
    private static String msLock = "lock";
    private static final int MSF_CACHESIZE = (int) (Runtime.getRuntime().maxMemory()/1204);

    private CacheUtil(){}

    public static CacheUtil initCache(){
        if (msCacheUtil == null) {
            synchronized (msLock){
                if (msCacheUtil == null){
                    msCacheUtil = new CacheUtil();
                    msLreCache = new LruCache<>(MSF_CACHESIZE);
                }
            }
        }
        return msCacheUtil;
    }

    public void saveToCache(String key,  List<HashMap<String, String>> value){
        if (getFromCache(key) == null){
            msLreCache.put(key, value);
        }
    }

    public   List<HashMap<String, String>> getFromCache(String key){
        List<HashMap<String, String>> list = msLreCache.get(key);
        return list;
    }

    public void clearCache(){
        msLreCache.evictAll();
    }

        public int getCount(){
        int useCacheSize = msLreCache.putCount();
        return useCacheSize;
    }

}

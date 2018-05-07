package util;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author : 老头儿
 * @email : 527672827@qq.com
 * @org : 河北北方学院 移动开发工程部 C508
 * @function : （功能）
 */
public class StringUtil {

    /*MD5加密算法*/
   public static String hashKeyForDisk(String key){
       String cacehKey;
       try {
           final MessageDigest mDigest = MessageDigest.getInstance("MD5");
           mDigest.update(key.getBytes());
           cacehKey = bytesToHexString(mDigest.digest());
       } catch (NoSuchAlgorithmException e) {
           cacehKey = String.valueOf(key.hashCode());
       }
       return cacehKey;
   }

    private static String bytesToHexString(byte[] bytes){
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < bytes.length; i++){
            String hex = Integer.toHexString(0xFF & bytes[i]);
            if (hex.length() == 1){
                sb.append("0");
            }
            sb.append(hex);
        }
        return sb.toString();
    }

    public static String subcribtionHtml(String html){
        String url = null;
        try {
            if (html.contains("<link />")){
                url = html.substring(html.indexOf("<link />")+8, html.indexOf("<description>"));
            }else {
                url = html.substring(html.indexOf("<link>")+6, html.indexOf("<description>"));
            }
        }catch (Exception e){
            return null;
        }
        return url.replace("amp;", "");
    }

    /*截取字符串，截取的是发布的日期*/
    public static String subcribtionPubDate(String strPubDate){
        return strPubDate.substring(0, strPubDate.indexOf("GMT")+3);
    }

    /*裁剪字符串， 将获取的网址内容截取*/
    public static String[] splitStringContent(String content){
        String newString = content;
        if (content.contains("")){
            newString = content.replace("   ", "");
        }
        String[] newStringArray = newString.split("，");
        if (newStringArray == null || newStringArray.length == 0){
            return null;
        }
        return newStringArray;
    }

    /*去掉字符串中的转义字符*/
    public static String trimString(String  content){
        String regEx = "[&nbsp</>lrdquo;]";
        Pattern pattern = Pattern.compile(regEx);
        Matcher matcher = pattern.matcher(content);
        return matcher.replaceAll("");
    }
}

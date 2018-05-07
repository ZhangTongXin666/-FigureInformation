package data;

import java.io.Serializable;

/**
 * @author : 老头儿
 * @email : 527672827@qq.com
 * @org : 河北北方学院 移动开发工程部 C508
 * @function : （功能）
 */
public class CollectEssayMessage implements Serializable {

    public String title;
    public String author;
    public String time;
    public String clickNumber;
    public String content;
    public String url;
    public String pictureUrl;
    public String phoneNumber;

    public CollectEssayMessage(String title, String author, String time, String clickNumber, String  content, String url, String pictureUrl, String phoneNumber) {
        this.title = title;
        this.author = author;
        this.time = time;
        this.clickNumber = clickNumber;
        this.content = content;
        this.url = url;
        this.pictureUrl = pictureUrl;
        this.phoneNumber = phoneNumber;
    }


}

package data;

import java.io.Serializable;

/**
 * @author : 老头儿
 * @email : 527672827@qq.com
 * @org : 河北北方学院 移动开发工程部 C508
 * @function : （功能）
 */
public class ClassDataMessage implements Serializable{

    public String className;
    public String phoneNumber;
    public String picture;

    public ClassDataMessage(String className, String phoneNumber, String picture) {
        this.className = className;
        this.phoneNumber = phoneNumber;
        this.picture = picture;
    }
}

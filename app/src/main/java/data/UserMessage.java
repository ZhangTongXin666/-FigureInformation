package data;

import java.io.Serializable;

/**
 *@author : 老头儿
 *@email : 527672827@qq.com
 *@org : 河北北方学院 移动开发工程部 C508
 *@function : （功能） 用户信息
 */

public class UserMessage implements Serializable {

    public String oStrHead;
    public String oStrName;
    public String oStrBirsday;
    public String oStrEmail;
    public String oStrPhoneNumber;
    public String oStrPassword;
    public String oStrUpLookTime;
    public int oIntSex;
    public int oIntLookCount;

    public UserMessage(String oStrPhoneNumber, String oStrPassword ,String oStrHead, String oStrName, String oStrBirsday, String oStrEmail ,int oIntSex, int oIntLookCount, String oStrUpLookTime){
        this.oStrPhoneNumber = oStrPhoneNumber;
        this.oStrPassword = oStrPassword;
        this.oStrHead = oStrHead;
        this.oStrName = oStrName;
        this.oStrBirsday = oStrBirsday;
        this.oStrEmail = oStrEmail;
        this.oIntSex = oIntSex;
        this.oIntLookCount = oIntLookCount;
        this.oStrUpLookTime = oStrUpLookTime;
    }
}

package variable;

import data.UserMessage;

/**
 * Created by 张同心 on 2017/9/27.
 * @function  用户信息
 */

public class UserMessageVariable {
    public static volatile UserMessage osUserMessage; //用户信息
    public static int osFirstLogin = 0;//用户是否是第一次登录
    public static boolean mBrShowNewMessage = false;//是否显示新的信息
    public static boolean mBrShowDoGrade = false;//对APP进行打分

}

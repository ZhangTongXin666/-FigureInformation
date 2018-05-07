package data;

import android.os.Environment;
import android.util.Log;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.List;

import variable.UserMessageVariable;

/**
 * @author : 老头儿
 * @email : 527672827@qq.com
 * @org : 河北北方学院 移动开发工程部 C508
 * @function : （功能）
 */
public class HandleClassDataMessage {

    public static final String osStrDir = Environment.getExternalStorageDirectory()+"/FigureInformation/attentionClass";//目录

    /**
     * 保存数据
     */
    public static void saveData(ClassDataMessage classDataMessage) {
        File dir = new File(osStrDir);
        File file = new File(dir, "class_"+classDataMessage.phoneNumber+classDataMessage.className+".txt");
        if (!dir.exists()){
            Log.d("TAG", "创建兴趣文件夹："+dir.mkdirs());
        }
        try {
            ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(file));
            oos.writeObject(classDataMessage);
            oos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * o判断专题是否存在
     * @param classname
     * @return 判断结果
     */
    public static boolean classExist(String classname){
        File dir = new File(osStrDir);
        File file = new File(dir, "class_"+ UserMessageVariable.osUserMessage.oStrPhoneNumber+classname+".txt");
        if (file.exists()){
            return true;
        }else {
            return false;
        }
    }

    /**
     * 读取专题信息
     * @return 全部关注信息
     */
    public static List<ClassDataMessage> readAllAttentionMessage(){
        File dir = new File(osStrDir);
        File[] files = dir.listFiles();
        List<ClassDataMessage> list = new ArrayList<>();
        if (files != null && files.length != 0){
            for (int i = 0; i < files.length; i++) {
                if (files[i].getName().contains(UserMessageVariable.osUserMessage.oStrPhoneNumber)){
                    try {
                        ObjectInputStream ois = new ObjectInputStream(new FileInputStream(files[i]));
                        ClassDataMessage classMessage = (ClassDataMessage) ois.readObject();
                        list.add(classMessage);
                    } catch (IOException e) {
                        e.printStackTrace();
                    } catch (ClassNotFoundException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
        return list;
    }

    /*单个专题*/
    public static ClassDataMessage readSignleClassMessage(String title){
        File dir = new File(osStrDir);
        File[] files = dir.listFiles();
        ClassDataMessage classMessage = null;
        for (int i = 0; i < files.length; i++) {
            if (files[i].getName().contains(title)){
                try {
                    ObjectInputStream ois = new ObjectInputStream(new FileInputStream(files[i]));
                    classMessage = (ClassDataMessage) ois.readObject();
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                }
            }
        }
        return classMessage;
    }

    /*删除单个专题*/
    public static void deleteSingleClass(String className){
        File dir = new File(osStrDir);
        File[] files = dir.listFiles();
        for (int i = 0; i < files.length; i++) {
            if (files[i].getName().contains(className)){
                files[i].delete();
            }
        }
    }

    /*删除全播收藏专题*/
    public static void deleteAllClass(){
        File dir = new File(osStrDir);
        File[] files = dir.listFiles();
        for (int i = 0; i < files.length; i++) {
            if (files[i].getName().contains(UserMessageVariable.osUserMessage.oStrPhoneNumber)){
                files[i].delete();
            }
        }
    }

}

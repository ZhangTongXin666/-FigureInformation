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
public class HandleCollectEssayMessage {
    public static final String osStrDir = Environment.getExternalStorageDirectory()+"/FigureInformation/essayContent";//目录

    /**
     * 保存数据
     */
    public static void saveData(CollectEssayMessage essayContentMessage) {
        File dir = new File(osStrDir);
        File file = new File(dir, "essay_"+essayContentMessage.phoneNumber+essayContentMessage.title+".txt");
        if (!dir.exists()){
            Log.d("TAG", ""+dir.mkdirs());
        }
        try {
            ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(file));
            oos.writeObject(essayContentMessage);
            oos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * o判断收藏是否存在
     * @param title 标题
     * @return 判断结果
     */
    public static boolean essayExist(String title){
        File dir = new File(osStrDir);
        File file = new File(dir, "essay_"+ UserMessageVariable.osUserMessage.oStrPhoneNumber+title+".txt");
        if (file.exists()){
            return true;
        }else {
            return false;
        }
    }

    /**
     * 读取收藏信息
     * @param phoneNumber 手机号
     * @return 全部收藏
     */
    public static List<CollectEssayMessage> readAllEssayMessage(){
        File dir = new File(osStrDir);
        File[] files = dir.listFiles();
        List<CollectEssayMessage> list = new ArrayList<>();
        if (files != null && files.length != 0){
            for (int i = 0; i < files.length; i++) {
                if (files[i].getName().contains(UserMessageVariable.osUserMessage.oStrPhoneNumber)){
                    try {
                        ObjectInputStream ois = new ObjectInputStream(new FileInputStream(files[i]));
                        CollectEssayMessage essayMessage = (CollectEssayMessage) ois.readObject();
                        list.add(essayMessage);
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

    /*单个文章*/
    public static CollectEssayMessage readSignleEssayMessage(String title){
        File dir = new File(osStrDir);
        File[] files = dir.listFiles();
        CollectEssayMessage essayMessage = null;
        for (int i = 0; i < files.length; i++) {
            if (files[i].getName().contains(title)){
                try {
                    ObjectInputStream ois = new ObjectInputStream(new FileInputStream(files[i]));
                    essayMessage = (CollectEssayMessage) ois.readObject();
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                }
            }
        }
        return essayMessage;
    }

    /*删除单个收藏文章*/
    public static void deleteSingleEssay(String title){
        File dir = new File(osStrDir);
        File[] files = dir.listFiles();
        for (int i = 0; i < files.length; i++) {
            if (files[i].getName().contains(title)){
                 files[i].delete();
            }
        }
    }

    /*删除全播收藏文章*/
    public static void deleteAllEssay(){
        File dir = new File(osStrDir);
        File[] files = dir.listFiles();
        for (int i = 0; i < files.length; i++) {
            if (files[i].getName().contains(UserMessageVariable.osUserMessage.oStrPhoneNumber)){
                 files[i].delete();
            }
        }
    }
}

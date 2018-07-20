package adapter;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.constraint.solver.Goal;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.example.kys_31.figureinformation.DetailMessageActivity;
import com.example.kys_31.figureinformation.HotSpecialActivity;
import com.example.kys_31.figureinformation.LoginActivity;
import com.example.kys_31.figureinformation.R;

import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

import data.ClassDataMessage;
import data.HandleClassDataMessage;
import util.SVProgressHUDUtil;
import util.StringUtil;
import util.TimeUtil;
import util.ViewUtil;
import variable.FirstPageShowCountVariable;
import variable.ImageUrlVariable;
import variable.LoginStateVariable;
import variable.NoInterestVariable;
import variable.PreShowDataVariable;
import variable.UserMessageVariable;

/**
 * @author : 老头儿
 * @email : 527672827@qq.com
 * @org : 河北北方学院 移动开发工程部 C508
 * @function : （功能） 首页ListView的适配器
 */
public class FirstPageAdapter extends BaseAdapter {

    private List<HashMap<String, String>> listItemContnet;
    private String mStrWhere;
    private HashMap<String, String> mMapClassName;
    private String tem;
    private int temInt;

    public FirstPageAdapter(List<HashMap<String, String>> listItemContnet, String where) {
        this.listItemContnet = listItemContnet;
        mStrWhere = where;
    }

    @Override
    public int getCount() {
        if (mStrWhere.equals("firstpage")){
            return FirstPageShowCountVariable.osIntFirstPageShowCount;
        }else {
            return listItemContnet.size();
        }
    }

    @Override
    public Object getItem(int position) {
        return listItemContnet.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, final ViewGroup parent) {
        final ViewHolder viewHolder;
        if (convertView == null){
            viewHolder = new ViewHolder();
            convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.interest_item_view, null);
            viewHolder.oLlDetailMessage = (LinearLayout)convertView.findViewById(R.id.ll_detailMessage);
            viewHolder.oTvClassName = (TextView)convertView.findViewById(R.id.tv_className);
            viewHolder.oTvDescription = (TextView)convertView.findViewById(R.id.tv_description);
            viewHolder.oTvTimeAndAuthor = (TextView)convertView.findViewById(R.id.tv_timeAndAuthor);
            viewHolder.oTvTitle = (TextView)convertView.findViewById(R.id.tv_titleName);
            viewHolder.oIvClose = (ImageView)convertView.findViewById(R.id.iv_close);
            convertView.setTag(viewHolder);
        }else {
            viewHolder = (ViewHolder)convertView.getTag();
        }
        viewHolder.oTvClassName.setText(listItemContnet.get(position).get("classname"));
        viewHolder.oTvTitle.setText(listItemContnet.get(position).get("title"));
        viewHolder.oTvDescription.setText(StringUtil.trimString(listItemContnet.get(position).get("description")));
        viewHolder.oTvTimeAndAuthor.setText(listItemContnet.get(position).get("timeandauthor"));
        viewHolder.oTvClassName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (parent.getContext().getClass() != HotSpecialActivity.class){
                    mMapClassName = PreShowDataVariable.osPreShowData.get(PreShowDataVariable.osPreShowData.size()-1);
                    for (String key : mMapClassName.keySet()){
                        if (key.contains(viewHolder.oTvClassName.getText().toString())){
                            showDialogView(mMapClassName.get(key), viewHolder.oTvClassName.getText().toString() ,parent.getContext());
                            break;
                        }
                    }
                }
            }
        });
        if (parent.getContext().getClass() == HotSpecialActivity.class){
            viewHolder.oIvClose.setVisibility(View.GONE);
        }

            viewHolder.oLlDetailMessage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(parent.getContext(), DetailMessageActivity.class);
                intent.putExtra("URL", listItemContnet.get(position).get("link"));
                intent.putExtra("title", listItemContnet.get(position).get("title"));
                intent.putExtra("timeandauthor", listItemContnet.get(position).get("timeandauthor"));
                parent.getContext().startActivity(intent);
            }
        });

        viewHolder.oIvClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                View popView = LayoutInflater.from(parent.getContext()).inflate(R.layout.dialog_nointerest, null);
                final PopupWindow popupWindow = new PopupWindow(parent.getContext());
                popupWindow.setWidth(ViewGroup.LayoutParams.WRAP_CONTENT);
                popupWindow.setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);
                popupWindow.setContentView(popView);
                popupWindow.setBackgroundDrawable(new ColorDrawable(Color.WHITE));
                popupWindow.setOutsideTouchable(true);
                TextView tvSure = (TextView)popView.findViewById(R.id.tv_noInterest);
                tvSure.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        NoInterestVariable.osNotInterest += " "+viewHolder.oTvClassName.getText();
                        msUpdateNoInterest.updateNoInterest();
                        SharedPreferences saveNoInterest = parent.getContext().getSharedPreferences("nointerest", 0);
                        SharedPreferences.Editor editor = saveNoInterest.edit();
                        editor.putString("nointerest", NoInterestVariable.osNotInterest);
                        editor.commit();
                        popupWindow.dismiss();
                    }
                });
                popupWindow.showAsDropDown(viewHolder.oIvClose);

            }
        });
        return convertView;
    }

    static class  ViewHolder{
        public TextView oTvClassName;
        public TextView oTvTitle;
        public TextView oTvDescription;
        public TextView oTvTimeAndAuthor;
        public LinearLayout oLlDetailMessage;
        public ImageView oIvClose;
    }

    public static class CustomComparator implements Comparator<HashMap<String, String>> {
        @Override
        public int compare(HashMap<String, String> o1, HashMap<String, String> o2) {
            return TimeUtil.compatatorTime(TimeUtil.formatTimeOne(o1.get("timeandauthor")), TimeUtil.formatTimeOne(o2.get("timeandauthor")))? -1 : 1;
        }
    }

    private void showDialogView(final String strSpecialUrl, final String strSpecial, final Context context){
        final Dialog dialog = new AlertDialog.Builder(context).create();
        View dialogView = LayoutInflater.from(context).inflate(R.layout.dialog_special_view, null);
        dialog.show();
        dialog.getWindow().setContentView(dialogView);
        ViewUtil.setDialogWindowAttr(dialog, 500, 400);

        TextView tvGoLook = (TextView)dialogView.findViewById(R.id.tv_goLook);
        final TextView tvAttentionSpecial = (TextView)dialogView.findViewById(R.id.tv_attentionSpecial);
        tvGoLook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                context.startActivity(HotSpecialActivity.startIntnet(context,strSpecialUrl));
                dialog.dismiss();
            }
        });
        if (LoginStateVariable.osLoginState){
            if (HandleClassDataMessage.classExist(strSpecial)){
                tvAttentionSpecial.setText("取消关注");
                tvAttentionSpecial.setTextColor(R.color.tomato);
            }else {
                tvAttentionSpecial.setText("关注专题");
                tvAttentionSpecial.setTextColor(R.color.black);
            }
        }
        tvAttentionSpecial.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!LoginStateVariable.osLoginState){
                    context.startActivity(new Intent(context, LoginActivity.class));
                }else {
                    if (tvAttentionSpecial.getText().equals("关注专题")){
                        ClassDataMessage classDataMessage = new ClassDataMessage(strSpecial, UserMessageVariable.osUserMessage.oStrPhoneNumber,
                                ImageUrlVariable.mStrUrl[new Random().nextInt(ImageUrlVariable.mStrUrl.length-1)] );
                        HandleClassDataMessage.saveData(classDataMessage);
                        Toast.makeText(context, "关注成功", Toast.LENGTH_SHORT).show();
                    }else {
                        HandleClassDataMessage.deleteSingleClass(strSpecial);
                        SVProgressHUDUtil.showErrorWithStatus(context, "取消关注", SVProgressHUDUtil.SVProgressHUDMaskType.GradientCancel);
                    }

                }
                dialog.dismiss();
            }
        });
    }

    /*根据不感兴趣更新视图*/
    public interface UpdateNoInterest{
        void updateNoInterest();
    }

    private static UpdateNoInterest msUpdateNoInterest;
    public static void initUpdateNoInterest(UpdateNoInterest updateNoInterest){
        msUpdateNoInterest = updateNoInterest;
    }
}

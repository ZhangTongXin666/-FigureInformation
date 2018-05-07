package adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.kys_31.figureinformation.R;

import java.util.HashMap;
import java.util.List;

import data.ClassDataMessage;
import com.example.kys_31.figureinformation.HotSpecialActivity;
import variable.PreShowDataVariable;
import variable.RemeberRandomIntVariable;

/**
 * @author : 老头儿
 * @email : 527672827@qq.com
 * @org : 河北北方学院 移动开发工程部 C508
 * @function : （功能）
 */
public class InterestAdapter extends BaseAdapter {

    private Context mContext;
    private List<ClassDataMessage> mList;
    private HashMap<String, String> mSearchMap;

    public InterestAdapter(Context context, List<ClassDataMessage> list){
        mContext = context;
        mList = list;
    }

    @Override
    public int getCount() {
        return mList.size();
    }

    @Override
    public Object getItem(int position) {
        return mList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        final ViewHolder viewHolder;
        if (convertView == null){
            convertView = LayoutInflater.from(mContext).inflate(R.layout.item_interest, null);
            viewHolder = new ViewHolder();
            viewHolder.oIvRandom = (ImageView)convertView.findViewById(R.id.iv_randomPicture);
            viewHolder.oTvContentTheme = (TextView)convertView.findViewById(R.id.tv_contentTheme);
            viewHolder.mLlClick = (LinearLayout)convertView.findViewById(R.id.ll_click);
            convertView.setTag(viewHolder);
        }else {
            viewHolder = (ViewHolder)convertView.getTag();
        }

        Log.e("TAG", "position："+position+"      "+mList.get(position).picture);

        Glide.with(mContext).load(mList.get(position).picture).asBitmap().placeholder(R.drawable.picture_load).error(R.drawable.picture_error).into(viewHolder.oIvRandom);
        viewHolder.oTvContentTheme.setText(mList.get(position).className);
        viewHolder.oIvRandom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RemeberRandomIntVariable.osPicture = mList.get(position).picture;
                mContext.startActivity(HotSpecialActivity.startIntnet(mContext, PreShowDataVariable.osPreShowData.get(PreShowDataVariable.osPreShowData.size()-1)
                        .get(viewHolder.oTvContentTheme.getText().toString()+"RSS")));
            }
        });
        return convertView;
    }

    static class ViewHolder{
        public TextView oTvContentTheme;
        public ImageView oIvRandom;
        public LinearLayout mLlClick;
    }
}

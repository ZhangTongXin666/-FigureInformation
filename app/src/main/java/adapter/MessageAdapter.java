package adapter;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.kys_31.figureinformation.DetailMessageActivity;
import com.example.kys_31.figureinformation.R;

import java.util.List;

import data.CollectEssayMessage;
import data.HandleCollectEssayMessage;
import util.DisplaySizeUtil;
import util.ViewUtil;

/**
 * Created by kot32 on 15/9/25.
 */
public class MessageAdapter extends SimpleBaseAdapter<String> {

    private List<CollectEssayMessage> mList;


    public MessageAdapter(Context context,List<CollectEssayMessage> mListCollectionMessage) {
        super(context, null);
        this.mList = mListCollectionMessage;
    }

    @Override
    public int getCount(){
        super.getCount();
        return mList.size();
    }

    @Override
    public Object getItem(int position) {
        if (position >= mList.size())
            return null;
        return mList.get(position).title;
    }

    @Override
    public int getItemResource() {
        return R.layout.message_item;
    }

    @Override
    public View getItemView(final int position, final View convertView, ViewHolder holder) {

        TextView textView = holder.getView(R.id.text);
        RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(DisplaySizeUtil.dip2px(context,300), ViewGroup.LayoutParams.WRAP_CONTENT);
        textView.setText(getItem(position)+"");

        if (position % 2 == 0) {
            textView.setBackgroundResource(R.drawable.others_message);
            layoutParams.addRule(RelativeLayout.ALIGN_PARENT_LEFT, RelativeLayout.TRUE);
        } else {
            textView.setBackgroundResource(R.drawable.my_message);
            layoutParams.addRule(RelativeLayout.ALIGN_PARENT_RIGHT, RelativeLayout.TRUE);
        }
        textView.setLayoutParams(layoutParams);
        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, DetailMessageActivity.class);
                intent.putExtra("URL", mList.get(position).url);
                intent.putExtra("title", mList.get(position).title);
                intent.putExtra("timeandauthor", mList.get(position).time);
                context.startActivity(intent);
            }
        });
        textView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                final Dialog dialogWarn = new AlertDialog.Builder(convertView.getContext()).create();
                final View viewDialog = LayoutInflater.from(convertView.getContext()).inflate(R.layout.dialog_collection_view,null);
                dialogWarn.show();
                dialogWarn.setCanceledOnTouchOutside(true);
                dialogWarn.setContentView(viewDialog);
                ViewUtil.setDialogWindowAttr(dialogWarn, 800, 550);
                Button btCancleCollection = (Button)viewDialog.findViewById(R.id.bt_cancleCollection);
                Button btNoCancleCollection = (Button)viewDialog.findViewById(R.id.bt_noCancle);

                btCancleCollection.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        HandleCollectEssayMessage.deleteSingleEssay((String) getItem(position));
                        mList.remove(position);
                        notifyDataSetChanged();
                        dialogWarn.dismiss();
                    }
                });
                btNoCancleCollection.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialogWarn.dismiss();
                    }
                });
                return true;
            }
        });
        return convertView;
    }
}

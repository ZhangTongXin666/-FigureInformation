package nokonw;

import android.animation.TypeEvaluator;
import android.content.Context;
import android.util.TypedValue;

import java.util.ArrayList;
import java.util.List;

/**
 * 杨铭 Created by kys_35 on 2017/10/24.
 * <p>Email:771365380@qq.com</p>
 * <p>Mobile phone:15133350726</p>
 */

public class ExitRatioFrameEvaluator implements TypeEvaluator
{
    public static final int OFFSET_DISTANCE = 80;

    private Context mContext;

    private int mOffsetDistance;

    public ExitRatioFrameEvaluator(Context context){
        this.mContext = context;
        mOffsetDistance = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,OFFSET_DISTANCE,mContext.getResources().getDisplayMetrics());
    }
    @Override
    public Object evaluate(float fraction, Object startValue, Object endValue)
    {
        List<RatioFrame> startRatioFrameList = (List<RatioFrame>) startValue;
        List<RatioFrame> endRatioFrameList = (List<RatioFrame>) endValue;
        List<RatioFrame> ratioFrameList = new ArrayList<>();
        for (int i = 0;i < startRatioFrameList.size();i++){
            RatioFrame startRatioFrame = startRatioFrameList.get(i);
            RatioFrame endRatioFrame = endRatioFrameList.get(i);
            double t = ( -2 * Math.pow(fraction,2) + 2 * fraction);//倾斜变化率
            int temp = (int)((mOffsetDistance) * t);
            double rightAngle = Math.PI / 2;
            int moveX = 0,moveY = 0;
            moveX = (int)(temp * Math.cos(startRatioFrame.mAngle));
            moveY = (int)(temp * Math.sin(startRatioFrame.mAngle));
            int left = (int)(startRatioFrame.mLeft - ((startRatioFrame.mLeft - endRatioFrame.mLeft) * fraction) - moveX);
            int top = (int)(startRatioFrame.mTop - ((startRatioFrame.mTop - endRatioFrame.mTop) * fraction) - moveY);
            int right = (int)(startRatioFrame.mRight - ((startRatioFrame.mRight - endRatioFrame.mRight) * fraction) - moveX);
            int bottom = (int)(startRatioFrame.mBottom - ((startRatioFrame.mBottom - endRatioFrame.mBottom) * fraction) - moveY);
            ratioFrameList.add(new RatioFrame(left,top,right,bottom));
        }
        return ratioFrameList;

    }
}

package nokonw;

import android.animation.TypeEvaluator;

/**
 * 杨铭 Created by kys_35 on 2017/10/24.
 * <p>Email:771365380@qq.com</p>
 * <p>Mobile phone:15133350726</p>
 */

public class HomingRatioFrameEvaluator implements TypeEvaluator
{
    @Override
    public Object evaluate(float fraction, Object startValue, Object endValue)
    {
       RatioFrame startRatioFrame = (RatioFrame) startValue;
        RatioFrame endRatioFrame = (RatioFrame) endValue;
        int left = (int)(startRatioFrame.mLeft + (endRatioFrame.mLeft - startRatioFrame.mLeft) * fraction);
        int top = (int)(startRatioFrame.mTop + (endRatioFrame.mTop - startRatioFrame.mTop) * fraction);
        int right = (int)(startRatioFrame.mRight + (endRatioFrame.mRight - startRatioFrame.mRight) * fraction);
        int bottom = (int)(startRatioFrame.mBottom + (endRatioFrame.mBottom - startRatioFrame.mBottom) * fraction);
        return new RatioFrame(left,top,right,bottom);
    }
}

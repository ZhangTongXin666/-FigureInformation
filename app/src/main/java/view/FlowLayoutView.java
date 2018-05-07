package view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.kys_31.figureinformation.R;

import java.util.ArrayList;
import java.util.List;


public class FlowLayoutView extends RelativeLayout {



    //水平间距，单位为dp
    private int horizontalSpacing = dp2x(10);
    // 竖直间距，单位为dp
    private int verticalSpacing = dp2x(10);
    // 行的集合
    private List<Line>lines = new ArrayList<>();
    // 当前的行
    private Line line;
    // 当前行使用的空间
    private int lineSize = 0;
    //关键字大小，单位为sp
    private int textSize = sp2x(15);
    //关键字颜色
    private int textColor = Color.BLACK;
    // 关键字背景框
    private int backgroundResource = R.drawable.bg_frame;
    // 关键字水平padding，单位为dp
    private int textPaddingH = dp2x(7);
    // 关键字竖直padding，单位为dp
    private int textPaddingV = dp2x(4);



    public FlowLayoutView(Context context)
    {
        this(context,null);
    }

    public FlowLayoutView(Context context, AttributeSet attrs)
    {
        this(context, attrs,0);
    }

    public FlowLayoutView(Context context, AttributeSet attrs, int defStyleAttr)
    {

        super(context, attrs, defStyleAttr);
        TypedArray typedArray = context.getTheme().obtainStyledAttributes(attrs, R.styleable.FlowLayoutAttrs,defStyleAttr,0);

        int count = typedArray.getIndexCount();
        for (int i = 0;i<count;i++){
            int attr = typedArray.getIndex(i);
            switch (attr){
                case R.styleable.FlowLayoutAttrs_horizontalSpacing:
                    horizontalSpacing = typedArray.getDimensionPixelOffset(attr,dp2x(10));
                    break;

                case R.styleable.FlowLayoutAttrs_verticalSpacing:
                    verticalSpacing = typedArray.getDimensionPixelOffset(attr,dp2x(10));
                    break;

                case R.styleable.FlowLayoutAttrs_itemSize:
                    textSize = typedArray.getDimensionPixelSize(attr,dp2x(15));
                    break;

                case R.styleable.FlowLayoutAttrs_itemColor:
                    textColor = typedArray.getColor(attr,Color.BLACK);
                    break;

                case R.styleable.FlowLayoutAttrs_backgroundResource:
                    backgroundResource = typedArray.getResourceId(attr,R.drawable.bg_frame);
                    break;

                case R.styleable.FlowLayoutAttrs_textPaddingH:
                    textPaddingH = typedArray.getDimensionPixelSize(attr,dp2x(7));
                    break;

                case R.styleable.FlowLayoutAttrs_textPaddingV:
                    textPaddingV = typedArray.getDimensionPixelSize(attr,dp2x(4));
                    break;
            }
        }
        typedArray.recycle();

    }


    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec)
    {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        // 实际可以用的宽和高
        int width = MeasureSpec.getSize(widthMeasureSpec)-getPaddingLeft()-getPaddingRight();
        int height = MeasureSpec.getSize(heightMeasureSpec)-getPaddingTop()-getPaddingBottom();
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int heightmode = MeasureSpec.getMode(heightMeasureSpec);

        //Line初始化
        restoreLine();

        for (int i=0;i<getChildCount();i++){
            View child = getChildAt(i);
            // 测量所有的childView
            int childWidthMeasureSpec = MeasureSpec.makeMeasureSpec(width,widthMode == MeasureSpec.EXACTLY?MeasureSpec.AT_MOST:widthMode);
            int childHeightMeasureSpec = MeasureSpec.makeMeasureSpec(height,heightmode == MeasureSpec.EXACTLY?MeasureSpec.AT_MOST:heightmode);
            child.measure(childWidthMeasureSpec,childHeightMeasureSpec);

            if (line == null){
                //创建新一行
                line = new Line();
            }

            // 计算当前行已使用的宽度
            int measuredWidth = child.getMeasuredWidth();
            lineSize += measuredWidth;

            //// 如果使用的宽度小于可用的宽度，这时候childView能够添加到当前的行上
            if (lineSize <= width){
                line.addChild(child);
                lineSize += horizontalSpacing;
            }else {
                //换行
                newLine();
                line.addChild(child);
                lineSize += child.getMeasuredWidth();
                lineSize += horizontalSpacing;
            }
        }

        //把把最后一行记录到集合中
        if (line !=null&&!lines.contains(line)){
            lines.add(line);
        }

        int totalHeight = 0;
        // 把所有行的高度加上
        for (int i = 0;i<lines.size();i++){
          totalHeight += lines.get(i).getHeight();
        }
        // 加上行的竖直间距
        totalHeight += verticalSpacing*(lines.size()-1);
        // 加上上下padding
        totalHeight += getPaddingBottom();
        totalHeight += getPaddingTop();

        // 设置自身尺寸
        // 设置布局的宽高，宽度直接采用父view传递过来的最大宽度，而不用考虑子view是否填满宽度
        // 因为该布局的特性就是填满一行后，再换行
        // 高度根据设置的模式来决定采用所有子View的高度之和还是采用父view传递过来的高度
        setMeasuredDimension(MeasureSpec.getSize(widthMeasureSpec),resolveSize(totalHeight,heightMeasureSpec));
    }

    private void restoreLine()
    {
        lines.clear();
        line = new Line();
        lineSize = 0;
    }

    private void newLine()
    {
        // 把之前的行记录下来
        if (line !=null){
            lines.add(line);
        }
        //创建新的一行
         line = new Line();
        lineSize = 0;
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b)
    {
        super.onLayout(changed, l, t, r, b);
        int left = getPaddingLeft();
        int top = getPaddingTop();
        for (int i = 0;i <lines.size();i++){
            Line line = lines.get(i);
            line.layout(left,top);
            // 计算下一行的起点y轴坐标
            top = top + line.getHeight()+verticalSpacing;

        }
    }

    /**
     * 管理每行上的View对象
     */
     class Line
    {
        //子控件集合
        private List<View> children = new ArrayList<>();
        //行高
        int height;

        /**
         * 添加childView
         *
         * @param childView 子控件
         */
        public void addChild(View childView)
        {
            children.add(childView);

            // 让当前的行高是最高的一个childView的高度
            if (height < childView.getMeasuredHeight())
            {
                height = childView.getMeasuredHeight();
            }
        }


        /**
         * 指定childView的绘制区域
         *
         * @param left 左上角x轴坐标
         * @param top  左上角y轴坐标
         */
        public void layout(int left, int top)
        {
            int totalWidth = getMeasuredWidth() - getPaddingLeft() - getPaddingRight();
            // 当前childView的左上角x轴坐标
            int currentLeft = left;

            for (int i = 0; i < children.size(); i++)
            {
                View view = children.get(i);
                // 指定childView的绘制区域
                view.layout(currentLeft, top, currentLeft + view.getMeasuredWidth(), top + view.getMeasuredHeight());
                // 计算下一个childView的位置
                currentLeft = currentLeft + view.getMeasuredWidth() + horizontalSpacing;
            }
        }

        public int getHeight()
        {
            return height;
        }

    }

        public void setFlowlayout(List<String>list, final OnItemClickListener onItemClickListener,int backgroundResource){
            for (int i = 0;i<list.size();i++){
                    final TextView tv = new TextView(getContext());
                // 设置TextView属性
                tv.setText(list.get(i));
                tv.setTextSize(TypedValue.COMPLEX_UNIT_PX,textSize);
                tv.setTextColor(textColor);
                tv.setBackgroundResource(R.drawable.bg_frame);
                tv.setGravity(Gravity.CENTER);
                tv.setPadding(textPaddingH,textPaddingV,textPaddingH,textPaddingV);
                tv.setClickable(true);
                tv.setBackgroundResource(backgroundResource);
                this.addView(tv, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
                        ViewGroup.LayoutParams.WRAP_CONTENT));
                tv.setOnClickListener(new OnClickListener()
                {
                    @Override
                    public void onClick(View view)
                    {
                        onItemClickListener.onItemClick(tv.getText().toString());
                    }
                });
            }
        }

    public interface OnItemClickListener
    {
        void onItemClick(String content);
    }

    public void setBackgroundResource(int backgroundResource){
        this.backgroundResource=backgroundResource;
    }


    private int sp2x(float sp)
    {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP,sp,getResources().getDisplayMetrics());
    }

    private int dp2x(float dp)
    {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,dp,getResources().getDisplayMetrics());
    }

}

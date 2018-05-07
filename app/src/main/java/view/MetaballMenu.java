package view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.LayerDrawable;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.RoundRectShape;
import android.os.Build;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewCompat;
import android.util.AttributeSet;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.BounceInterpolator;
import android.view.animation.Transformation;
import android.widget.LinearLayout;

import com.example.kys_31.figureinformation.R;

public class MetaballMenu extends LinearLayout {


    private static final String TAG = "MetaballMenu";

    private int mnMetaballColor;
    private Paint mMetaballDestination = new Paint(Paint.ANTI_ALIAS_FLAG);
    private boolean mbShowAnimation = false;
    private float mfInterpolatedTime = 0;
    private TransitionAnimation mTransitionAnimation = null;

    private Circle mTransitionalCircle = null;
    private Point mDestinationPoint = null;
    private Point mOriginPoint = null;
    private float mfSelectorRadius = 60.0f;
    private View mSelectedView = null;
    private float mfTransitionDistance = 0.0f;
    private MetaballMenuClickListener mMenuClickListener = null;

    public MetaballMenu(Context context)    {
        super(context);
        init(context, null);
    }

    public MetaballMenu(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public MetaballMenu(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(context, attrs);
    }

    private void init( Context context, AttributeSet attrs ) {
        if(attrs != null) {
            TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.MetaballMenu, 0, 0);
            mnMetaballColor = a.getColor(R.styleable.MetaballMenu_metaballColor, ContextCompat.getColor(context, android.R.color.white));
            mMetaballDestination.setColor(mnMetaballColor);
            mMetaballDestination.setStyle(Paint.Style.FILL);
            a.recycle();
        }
    }

    public void setMenuClickListener(MetaballMenuClickListener menuClickListener) {
        mMenuClickListener = menuClickListener;
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        super.onLayout(changed, l, t, r, b);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    @Override
    protected void onFinishInflate() {
        Log.e(TAG, "onFinishInflate: "+getMeasuredHeight() );
        for(int nCtr = 0; nCtr < getChildCount(); ++nCtr) {
            getChildAt(nCtr).setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(mbShowAnimation)
                       clearValues();

                    mbShowAnimation = true;
                    mOriginPoint = getCenter(mSelectedView);
                    mSelectedView.setSelected(false);      // unselect the previoud selection if any
                    mSelectedView = v;
                    mSelectedView.setSelected(true);       // select the new selection
                    mDestinationPoint = getCenter(mSelectedView);
                    mfTransitionDistance = mDestinationPoint.getX() - mOriginPoint.getX();
                    startAnimation();
                }
            });
        }

        //Set the first child as the selected View  during initialization
        mSelectedView = getChildAt(0);
        mSelectedView.setSelected(true);
        invalidate();
        super.onFinishInflate();
    }

    /**
     * Override onDraw to draw the selector cicle. We will also use this function to draw the Metaball Animation
     *
     * @author Melvin Lobo
     */
    @Override
    protected void onDraw(Canvas canvas) {
        if(!mbShowAnimation)
            drawSelector(canvas);
        else
            drawMetaballTransition(canvas);
        super.onDraw(canvas);
    }

    private void drawSelector(Canvas canvas) {
        Point center = getCenter(mSelectedView);
        canvas.drawCircle(center.getX(), center.getY(), mfSelectorRadius, mMetaballDestination);
    }

    private Point getCenter(View view) {
        return new Point(view.getLeft() + (view.getMeasuredWidth() / 2), view.getTop() + (view.getMeasuredHeight() / 2));
    }

    public void drawMetaballTransition(Canvas canvas) {
        mDestinationPoint = getCenter(mSelectedView);
        float originRadius, destinationRadius = 0.0f;
        originRadius = mfSelectorRadius - (mfSelectorRadius * mfInterpolatedTime);       // This circle will reduce in size based on the interpolator value
        destinationRadius = (mfSelectorRadius * mfInterpolatedTime);                     // This circle will increase in size based on the interpolator value
        if(mTransitionalCircle == null) {
			mTransitionalCircle = new Circle();
            mTransitionalCircle.setCenterPoint(new Point(0, mDestinationPoint.getY()));    //Get a center. The x-co-ordinate will change anyways
        }
        mTransitionalCircle.setRadius(originRadius);
        mTransitionalCircle.setCenterX(mOriginPoint.getX() + (mfTransitionDistance * mfInterpolatedTime));
        canvas.drawCircle(mTransitionalCircle.getCenterX(), mTransitionalCircle.getCenterY(), originRadius, mMetaballDestination);
        canvas.drawCircle(mDestinationPoint.getX(), mDestinationPoint.getY(), destinationRadius, mMetaballDestination);
    }

    private void startAnimation() {
        if((getVisibility() == View.GONE) || (getVisibility() == View.INVISIBLE))
            return;
        if(mTransitionAnimation == null) {
            mTransitionAnimation = new TransitionAnimation();
            mTransitionAnimation.setDuration(100);
        }
        mTransitionAnimation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                clearValues();
                if(mMenuClickListener != null)
                    mMenuClickListener.onClick(mSelectedView);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        startAnimation(mTransitionAnimation);
    }

    private void clearValues() {
        clearAnimation();
        mTransitionAnimation = null;
        mbShowAnimation = false;
        mTransitionalCircle = null;
        mDestinationPoint = null;
        mOriginPoint = null;
        mfTransitionDistance = 0.0f;
    }

    public class Point {
        float mnX;
        float mnY;
        public Point() {}
        public Point(float nX, float nY) {
            mnX = nX;
            mnY = nY;
        }

        public float getX() {
            return mnX;
        }

        public void setX(float nX) {
            mnX = nX;
        }

        public float getY() {
            return mnY;
        }

        public void setY(float nY) {
            mnY = nY;
        }
    }

    public class Circle {

        Point mPoint;
        float mnRadius;
        public Circle() {}

        public Circle(float nRadius) {
            mnRadius = nRadius;
        }
        public float getRadius() {
            return mnRadius;
        }
        public void setRadius(float radius) {
            mnRadius = radius;
        }
        public Point getCenterPoint() {
            return mPoint;
        }
        public void setCenterPoint(Point point) {
            mPoint = point;
        }
        public float getCenterX() {
            return mPoint.getX();
        }
        public void setCenterX(float nX) {
            mPoint.setX(nX);
        }
        public float getCenterY() {
            return mPoint.getY();
        }
        public void setCenterY(float nY) {
            mPoint.setY(nY);
        }
    }

    public class TransitionAnimation extends Animation {
        @Override
        protected void applyTransformation(float interpolatedTime, Transformation t) {
            super.applyTransformation(interpolatedTime, t);
            mfInterpolatedTime = interpolatedTime;
            invalidate();
        }
    }

    public interface MetaballMenuClickListener {
        public void onClick(View view);
    }
}

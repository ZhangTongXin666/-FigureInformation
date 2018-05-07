package view;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.DecelerateInterpolator;
import android.widget.AbsListView;
import android.widget.ListView;

/**
 * Created by kot32 on 15/9/25.
 */
public class IOSMessageListView extends ListView {

    private double maxSpeed = 0;

    private int currentListViewState;

    //listView中第一项的索引
    private int mListViewFirstItem = 0;
    //listView中第一项的在屏幕中的位置
    private int mScreenY = 0;
    //是否向上滚动
    boolean isScrollToUp = false;
    //最大移动量
    private static int MAX_TRANSLATION = 120;

    //动画的时长
    private int startDuration = 400;
    private int endDuration = 600;

    private static final int START_ANIMATION = 0;
    private Handler animationHandler;

    /*构造代码块*/
    {
        animationHandler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                switch (msg.what) {
                    case START_ANIMATION:
                        View child = (View) msg.obj;
                        int ratioUp = msg.arg1;// 上升比率
                        int ratioDown = msg.arg2;//下降比率

                        AnimatorSet animatorSet = new AnimatorSet();

                        if (child != null) {
                            float translation = 0;
                            if (maxSpeed <= 40 && maxSpeed >= 20) {
                                translation = (float) maxSpeed * 4;

                            } else if (maxSpeed > 40 && maxSpeed <= 90) {
                                translation = (float) (maxSpeed * 2);
                            } else if (maxSpeed < 20) {
                                translation = (float) maxSpeed;
                            }
                            if (translation <= 4) translation = 4;

                            if (isScrollToUp) {
                                translation = translation * ratioUp;
                                translation = -translation;

                                if (translation < -MAX_TRANSLATION) {
                                    //limit
                                    translation = -MAX_TRANSLATION;
                                }

                            } else {
                                translation = translation * ratioDown;

                                if (translation > MAX_TRANSLATION) {
                                    //limit
                                    translation = MAX_TRANSLATION;
                                }
                            }


                            ObjectAnimator go = ObjectAnimator.ofFloat(child, "translationY", translation);
                            go.setDuration(startDuration);
                            go.setInterpolator(new DecelerateInterpolator());

                            ObjectAnimator back = ObjectAnimator.ofFloat(child, "translationY", 0);
                            back.setDuration(endDuration);
                            back.setInterpolator(new DecelerateInterpolator());

                            animatorSet.play(go).before(back);
                            animatorSet.start();
                        }
                        break;
                }
            }
        };
    }

    public IOSMessageListView(Context context) {
        super(context);
        initView();
    }

    public IOSMessageListView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView();
    }

    private void initView() {
        setDivider(null);//设置没有分割线
        setOnScrollListener(onScrollListener);//设置滚动监听
    }

    private void startAnimation(final int firstIndex, final int lastIndex) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                for (int i = 1; i < lastIndex - firstIndex; i++) {
                    View child = getChildAt(i);
                    if (isScrollToUp) {
                        child = getChildAt(lastIndex - firstIndex - i);
                    }
                    Message message = new Message();
                    message.what = START_ANIMATION;
                    message.obj = child;
                    message.arg1 = i;
                    message.arg2 = lastIndex - firstIndex - i;

                    animationHandler.sendMessage(message);
                    try {
                        Thread.sleep(100);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }).start();


    }


    private OnScrollListener onScrollListener = new OnScrollListener() {

        private int previousFirstVisibleItem = 0;
        private long previousEventTime = 0;
        private double speed = 0;

        @Override
        public void onScrollStateChanged(AbsListView view, int scrollState) {
            currentListViewState = scrollState;
            switch (scrollState) {
                case SCROLL_STATE_IDLE:
                    maxSpeed = 0;
                    speed = 0;
                    break;
            }

        }

        @Override
        public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
            if (previousFirstVisibleItem != firstVisibleItem) {
                long currTime = System.currentTimeMillis();
                long timeToScrollOneElement = currTime - previousEventTime;
                speed = ((double) 1 / timeToScrollOneElement) * 1000;
                previousFirstVisibleItem = firstVisibleItem;
                previousEventTime = currTime;
                if (speed >= maxSpeed) {
                    maxSpeed = speed;
                } else if (speed > 0 && speed < maxSpeed) {
                    startAnimation(getFirstVisiblePosition(), getLastVisiblePosition());
                }

            }


            //判断滑动方向
            if (view.getChildCount() > 0) {
                View childAt = view.getChildAt(firstVisibleItem);
                int[] location = new int[2];
                if (childAt != null)
                    childAt.getLocationOnScreen(location);

                if (firstVisibleItem != mListViewFirstItem) {
                    if (firstVisibleItem > mListViewFirstItem) {
                        isScrollToUp = true;
                    } else {
                        isScrollToUp = false;
                    }
                    mListViewFirstItem = firstVisibleItem;
                    mScreenY = location[1];
                } else {
                    if (mScreenY > location[1]) {
                        isScrollToUp = true;
                    } else if (mScreenY < location[1]) {
                        isScrollToUp = false;
                    }
                    mScreenY = location[1];
                }
            }
        }

    };

}

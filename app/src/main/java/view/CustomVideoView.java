package view;

import android.content.Context;
import android.media.MediaPlayer;
import android.net.Uri;
import android.util.AttributeSet;
import android.view.View;
import android.widget.VideoView;


public class CustomVideoView extends VideoView {

    public CustomVideoView(Context context) {
        super(context);
    }

    public CustomVideoView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public CustomVideoView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        // 其实就是在这里做了一些处理。
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    /**
     * 播放视频
     */
    public void playVideo(Uri uri){
        if (uri == null){
            throw new IllegalArgumentException("Uri can't be null");
        }

        /**设置播放路径*/
        setVideoURI(uri);
        start();
        setOnPreparedListener(new MediaPlayer.OnPreparedListener()
        {
            @Override
            public void onPrepared(MediaPlayer mediaPlayer)
            {
                //设置循环播放
                mediaPlayer.setLooping(true);
            }
        });
        setOnErrorListener(new MediaPlayer.OnErrorListener()
        {
            @Override
            public boolean onError(MediaPlayer mediaPlayer, int i, int i1)
            {
                return true;
            }
        });
    }

}

package fragment;

import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.kys_31.figureinformation.R;

import view.CustomVideoView;


public class GuildFragment extends Fragment
{
    private CustomVideoView customVideoView;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState)
    {
        customVideoView = new CustomVideoView(getContext());
        /**获取参数，根据不同的参数播放不同的视频**/
//        int index = getArguments().getInt("index");
        int index = getArguments().getInt("index");
        Uri uri;
        if (index == 1){
            uri = Uri.parse("android.resource://" + getActivity().getPackageName() + "/" + R.raw.guide_1);
        }else if (index == 2){
            uri = Uri.parse("android.resource://" + getActivity().getPackageName() + "/" + R.raw.guide_2);
        }else {
            uri = Uri.parse("android.resource://" + getActivity().getPackageName() + "/" + R.raw.guide_3);
        }
        //播放视频
        customVideoView.playVideo(uri);
        return customVideoView;
    }

    //销毁时让播放的视频终止

    @Override
    public void onDestroy()
    {
        super.onDestroy();
        if (customVideoView != null && customVideoView.isPlaying()){
            customVideoView.stopPlayback();
            customVideoView = null;
        }
    }
}

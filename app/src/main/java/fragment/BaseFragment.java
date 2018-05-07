package fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.android.debug.hv.ViewServer;

import java.util.Observer;

import util.AutoLoginUtil;
import util.ViewUtil;
import variable.SystemSetVariable;

/**
 *@author : 老头儿
 *@email : 527672827@qq.com
 *@org : 河北北方学院 移动开发工程部 C508
 *@function : （功能） 基类
 */

public abstract class BaseFragment extends Fragment implements View.OnClickListener,Observer{

    public View view;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup viewGroup, Bundle saveInstanceState){
        view = inflater.inflate(getLayoutID(),null);
        initControl();
        setListener();
        ViewServer.get(getActivity()).addWindow(getActivity());
        return view;
    }

    protected abstract int getLayoutID();

    protected abstract void initControl();

    protected abstract void setListener();

    public void showToast(String content, boolean show){
        Toast.makeText(getActivity(),content,show?Toast.LENGTH_LONG:Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onResume(){
        super.onResume();
        ViewServer.get(getActivity()).setFocusedWindow(getActivity());
        /*屏幕亮度*/
        SystemSetVariable.osScreenBrightValue = ViewUtil.getScreenBrightness(getActivity());
        if (SystemSetVariable.osNightModel){
            ViewUtil.setScreenBrightness(getActivity(), 10);
        }else {
            ViewUtil.setScreenBrightness(getActivity(), SystemSetVariable.osScreenBrightValue);
        }
        /*自动登录*/
        AutoLoginUtil.startAutoLogin(getActivity());
        /*根据登录状态显示*/
    }

    @Override
    public void onDestroy(){
        super.onDestroy();
        ViewServer.get(getActivity()).removeWindow(getActivity());
    }

}

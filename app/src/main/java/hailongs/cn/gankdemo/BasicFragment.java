package hailongs.cn.gankdemo;

import android.support.v4.app.Fragment;
import android.view.View;

/**
 * Created by dhl on 2016/11/29.
 */

public abstract class BasicFragment extends Fragment {
    public abstract void findView();

    public abstract void initViews(View view);

    public abstract void getDatas();

    public abstract void lazyLoad();

    public boolean isVisible = false;

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);

        if (getUserVisibleHint()) {
            isVisible = true;
            onVisible();
        } else {
            isVisible = false;
            onInVisible();
        }
    }

    public void onVisible() {
        lazyLoad();
    }

    public void onInVisible() {
    }


}

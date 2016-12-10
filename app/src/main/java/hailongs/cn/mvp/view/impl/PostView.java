package hailongs.cn.mvp.view.impl;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;

import hailongs.cn.gankdemo.BasicFragment;
import hailongs.cn.mvp.presenter.iimpl.IPostPresenter;
import hailongs.cn.mvp.view.iimpl.IPostView;

/**
 * Created by dhl on 2016/11/29.
 */

public class PostView extends BasicFragment implements IPostView {

    IPostPresenter presenter;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }



    @Override
    public void loadMore() {

    }

    @Override
    public void findView() {

    }

    @Override
    public void initViews(View view) {

    }

    @Override
    public void getDatas() {

    }

    @Override
    public void lazyLoad() {

    }

    @Override
    public void dismissRefreshLayout(boolean dismiss) {

    }
}

package hailongs.cn.mvp.presenter.iimpl;

import android.support.v7.widget.RecyclerView;

import hailongs.cn.mvp.presenter.BasicPresenter;

/**
 * Created by dhl on 2016/11/29.
 */

public interface IPostPresenter extends BasicPresenter {
    void getPostList(String type, RecyclerView recyclerView, boolean isReload);
}
